package com.vybz.aggregation_service.live.batch.reader;

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
public class LiveViewCountReader implements ItemReader<String> {

    private final StringRedisTemplate stringRedisTemplate;
    private Iterator<String> liveViewCountIterator;
    private static final String LIVE_VIEW_BATCH_SET_KEY = "live:view:batch:set";

    @Override
    public String read() {
        if (liveViewCountIterator == null) {
            Set<String> liveViewCountSet = stringRedisTemplate.opsForSet().members(LIVE_VIEW_BATCH_SET_KEY);

            if (liveViewCountSet == null || liveViewCountSet.isEmpty()) {
                log.info("📭 ViewCount 배치 대상 없음");
                return null;
            }

            liveViewCountIterator = liveViewCountSet.iterator();
            log.info("📥 ViewCount 배치 대상 streamKeys: {}", liveViewCountSet);
        }

        if (liveViewCountIterator.hasNext()) {
            String streamKey = liveViewCountIterator.next();
            stringRedisTemplate.opsForSet().remove(LIVE_VIEW_BATCH_SET_KEY, streamKey);
            log.info("➡️ ViewCount 배치 처리: {}", streamKey);
            return streamKey;
        }

        return null;
    }
}
