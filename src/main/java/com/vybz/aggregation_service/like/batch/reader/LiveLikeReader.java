package com.vybz.aggregation_service.like.batch.reader;

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
public class LiveLikeReader implements ItemReader<String> {

    private final StringRedisTemplate stringRedisTemplate;
    private Iterator<String> streamKeyIterator;
    private static final String LIVE_LIKE_BATCH_SET_KEY = "live:like:batch:set";

    @Override
    public String read() {
        if (streamKeyIterator == null) {
            Set<String> streamKeys = stringRedisTemplate.opsForSet().members(LIVE_LIKE_BATCH_SET_KEY);
            if (streamKeys == null || streamKeys.isEmpty()) {
                log.info("📭 배치 대상 streamKey 없음");
                return null;
            }
            streamKeyIterator = streamKeys.iterator();
            log.info("📥 배치 대상 streamKey 목록 → {}", streamKeys);
        }

        if (streamKeyIterator.hasNext()) {
            String streamKey = streamKeyIterator.next();
            stringRedisTemplate.opsForSet().remove(LIVE_LIKE_BATCH_SET_KEY, streamKey);
            return streamKey;
        }

        return null;
    }
}

