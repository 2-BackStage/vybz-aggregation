package com.vybz.aggregation_service.follow.batch.processor;

import com.vybz.aggregation_service.common.entity.BaseResponseStatus;
import com.vybz.aggregation_service.exception.BaseException;
import com.vybz.aggregation_service.follow.batch.dto.BuskerFollowerCountDto;
import com.vybz.aggregation_service.follow.domain.BuskerFollowerCount;
import com.vybz.aggregation_service.follow.infrastructure.BuskerFollowerCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuskerFollowerCountProcessor implements ItemProcessor<String, BuskerFollowerCountDto> {

    private final BuskerFollowerCountRepository buskerFollowerCountRepository;

    @Override
    public BuskerFollowerCountDto process(String buskerUuid) {
        BuskerFollowerCount count = buskerFollowerCountRepository.findByBuskerUuid(buskerUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_BUSKER));

        if (count == null || count.getFollowerCount() == null || count.getFollowerCount() <= 0) {
            log.warn("❌ 팔로워 수 없음 또는 유효하지 않은 버스커: {}", buskerUuid);
            return null;
        }

        log.info("✅ 집계 대상 버스커 처리: {}, 팔로워 수: {}", buskerUuid, count.getFollowerCount());
        return BuskerFollowerCountDto.from(count);
    }
}
