package com.vybz.aggregation_service.live.infrastructure;

import com.vybz.aggregation_service.live.domain.LiveViewCount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LiveViewCountRepository extends MongoRepository<LiveViewCount, String> {

}
