package com.vybz.aggregation_service.follow.batch.writer;

import com.vybz.aggregation_service.follow.batch.dto.UserFollowingCountDto;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFollowingCountWriter implements ItemWriter<UserFollowingCountDto> {

    private final MongoTemplate mongoTemplate;

    @Override
    public void write(Chunk<? extends UserFollowingCountDto> items) {
        if (items.isEmpty())
            return;

        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "user_my_page_read");

        for (UserFollowingCountDto item : items) {
            Query query = new Query(Criteria.where("userUuid").is(item.getUserUuid()));
            Update update = new Update()
                    .set("totalFollowingCount", item.getTotalFollowingCount())
                    .set("displayFollowingCount", item.getDisplayFollowingCount())
                    .set("updatedAt", item.getUpdatedAt());
            bulkOps.upsert(query, update);

        }
        bulkOps.execute();

    }

}
