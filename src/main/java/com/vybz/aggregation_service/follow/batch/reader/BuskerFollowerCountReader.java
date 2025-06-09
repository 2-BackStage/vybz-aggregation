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
public class BuskerFollowerCountReader implements ItemReader<String> {

    private final StringRedisTemplate redisTemplate;
    private Iterator<String> buskerUuidIterator;
    private static final String BUSKER_FOLLOWER_KEY = "busker:follower:queue";

    @Override
    public String read() {
        if (buskerUuidIterator == null) {
            Set<String> buskerUuids = redisTemplate.opsForSet().members(BUSKER_FOLLOWER_KEY);
            if (buskerUuids == null || buskerUuids.isEmpty()) {
                log.info("📭 업데이트 대상 버스커 없음");
                return null;
            }
            buskerUuidIterator = buskerUuids.iterator();
            log.info("📭 업데이트 대상 버스커 UUIDs: {}", buskerUuids);
        }

        if (buskerUuidIterator.hasNext()) {
            String uuid = buskerUuidIterator.next();
            redisTemplate.opsForSet().remove(BUSKER_FOLLOWER_KEY, uuid);
            return uuid;
        }

        return null;
    }

}
