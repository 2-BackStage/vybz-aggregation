package com.vybz.aggregation_service.like.infrastructure;

import com.vybz.aggregation_service.like.domain.LiveLikeCount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LiveLikeRepository extends MongoRepository<LiveLikeCount, String> {
}
