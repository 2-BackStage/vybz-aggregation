package com.vybz.aggregation_service.follow.batch.writer;

import com.vybz.aggregation_service.follow.batch.dto.BuskerFollowerCountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class BuskerFollowerCountWriter implements ItemWriter<BuskerFollowerCountDto> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void write(Chunk<? extends BuskerFollowerCountDto> items) {
        if (items.isEmpty())
            return;

        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "busker_my_page_read");

        for (BuskerFollowerCountDto item : items) {
            Query query = new Query(Criteria.where("buskerUuid").is(item.getBuskerUuid()));
            Update update = new Update()
                    .set("totalFollowerCount", item.getTotalFollowerCount())
                    .set("displayFollowerCount", item.getDisplayFollowerCount())
                    .set("updatedAt", Instant.now());

            bulkOps.upsert(query, update);
        }

        bulkOps.execute();

    }
}
