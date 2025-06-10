package com.vybz.aggregation_service.kafka.consumer;

import com.vybz.aggregation_service.follow.domain.BuskerFollowerCount;
import com.vybz.aggregation_service.follow.domain.UserFollowingCount;
import com.vybz.aggregation_service.follow.infrastructure.BuskerFollowerCountRepository;
import com.vybz.aggregation_service.follow.infrastructure.UserFollowingCountRepository;
import com.vybz.aggregation_service.kafka.event.FollowCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowCountEventConsumer {

    private final BuskerFollowerCountRepository buskerFollowerCountRepository;
    private final UserFollowingCountRepository userFollowingCountRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String BUSKER_FOLLOWER_KEY = "busker:follower:queue";
    private static final String USER_FOLLOWING_KEY = "user:following:queue";

    @KafkaListener(
            topics = "create-follow",
            groupId = "follow-count-group",
            containerFactory = "followKafkaListenerContainerFactory"
    )
    public void consumeFollowEvent(FollowCountEvent followCountEvent) {
        log.info("🔥 Kafka 팔로우 메시지 수신: {}", followCountEvent);

        String buskerUuid = followCountEvent.getBuskerUuid();
        String userUuid = followCountEvent.getUserUuid();

        // 1. 버스커 팔로워 수 증가
        BuskerFollowerCount followerCount = buskerFollowerCountRepository.findByBuskerUuid(buskerUuid)
                .orElse(BuskerFollowerCount.builder()
                        .buskerUuid(buskerUuid)
                        .followerCount(0)
                        .build());

        followerCount.increaseCount();
        buskerFollowerCountRepository.save(followerCount);
        log.info("✅ [버스커] uuid: {}, 팔로워 수: {}", followerCount.getBuskerUuid(), followerCount.getFollowerCount());

        // 2. 사용자 팔로잉 수 증가
        UserFollowingCount followingCount = userFollowingCountRepository.findByUserUuid(userUuid)
                .orElse(UserFollowingCount.builder()
                        .userUuid(userUuid)
                        .followingCount(0)
                        .build());

        followingCount.increaseCount();
        userFollowingCountRepository.save(followingCount);
        log.info("✅ [사용자] uuid: {}, 팔로잉 수: {}", followingCount.getUserUuid(), followingCount.getFollowingCount());

        redisTemplate.opsForSet().add(BUSKER_FOLLOWER_KEY, buskerUuid);
        redisTemplate.opsForSet().add(USER_FOLLOWING_KEY, userUuid);
    }

    @KafkaListener(
            topics = "delete-follow",
            groupId = "follow-count-group",
            containerFactory = "followKafkaListenerContainerFactory"
    )
    public void consumeUnfollowEvent(FollowCountEvent followCountEvent) {
        log.info("💨 Kafka 언팔로우 메시지 수신: {}", followCountEvent);

        String buskerUuid = followCountEvent.getBuskerUuid();
        String userUuid = followCountEvent.getUserUuid();

        // 1. 버스커 팔로워 수 감소
        BuskerFollowerCount followerCount = buskerFollowerCountRepository.findByBuskerUuid(buskerUuid)
                .orElseGet(() -> BuskerFollowerCount.builder()
                        .buskerUuid(buskerUuid)
                        .followerCount(0)
                        .build());

        followerCount.decreaseCount();
        buskerFollowerCountRepository.save(followerCount);
        log.info("🛑 [버스커] uuid: {}, 팔로워 수 감소 후: {}", followerCount.getBuskerUuid(), followerCount.getFollowerCount());

        // 2. 사용자 팔로잉 수 감소
        UserFollowingCount followingCount = userFollowingCountRepository.findByUserUuid(userUuid)
                .orElseGet(() -> UserFollowingCount.builder()
                        .userUuid(userUuid)
                        .followingCount(0)
                        .build());

        followingCount.decreaseCount();
        userFollowingCountRepository.save(followingCount);
        log.info("🛑 [사용자] uuid: {}, 팔로잉 수 감소 후: {}", followingCount.getUserUuid(), followingCount.getFollowingCount());

        redisTemplate.opsForSet().add(BUSKER_FOLLOWER_KEY, buskerUuid);
        redisTemplate.opsForSet().add(USER_FOLLOWING_KEY, userUuid);
    }

}
