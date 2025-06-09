package com.vybz.aggregation_service.follow.batch.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class UserFollowingCountReader implements ItemReader<String> {

    private final StringRedisTemplate redisTemplate;
    private Iterator<String> userUuidIterator;
    private static final String USER_FOLLOWING_KEY = "user:following:queue";

    @Override
    public String read() {
        if (userUuidIterator == null) {
            Set<String> userUuids = redisTemplate.opsForSet().members(USER_FOLLOWING_KEY);
            if (userUuids == null || userUuids.isEmpty()) {
                log.info("📭 업데이트 대상 사용자 없음");
                return null;
            }
            userUuidIterator = userUuids.iterator();
            log.info("📬 업데이트 대상 사용자 UUIDs: {}", userUuids);
        }

        if (userUuidIterator.hasNext()) {
            String uuid = userUuidIterator.next();
            redisTemplate.opsForSet().remove(USER_FOLLOWING_KEY, uuid);
            return uuid;
        }

        return null;
    }

}
