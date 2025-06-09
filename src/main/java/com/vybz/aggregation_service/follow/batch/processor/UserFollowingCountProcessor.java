package com.vybz.aggregation_service.follow.batch.processor;

import com.vybz.aggregation_service.common.entity.BaseResponseStatus;
import com.vybz.aggregation_service.exception.BaseException;
import com.vybz.aggregation_service.follow.batch.dto.UserFollowingCountDto;
import com.vybz.aggregation_service.follow.domain.UserFollowingCount;
import com.vybz.aggregation_service.follow.infrastructure.UserFollowingCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFollowingCountProcessor implements ItemProcessor<String, UserFollowingCountDto> {

    private final UserFollowingCountRepository userFollowingCountRepository;

    @Override
    public UserFollowingCountDto process(String userUuid) {
        UserFollowingCount userFollowingCount = userFollowingCountRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_USER));

        if (userFollowingCount.getTotalFollowingCount() == null || userFollowingCount.getTotalFollowingCount() <= 0) {
            log.warn("❌ 팔로잉 수 0 이하인 사용자 제외: {}", userUuid);
            return null;
        }

        log.info("✅ 사용자 팔로잉 수 처리 완료: {}, display={}", userUuid, userFollowingCount);
        return UserFollowingCountDto.from(userFollowingCount);
    }

}
