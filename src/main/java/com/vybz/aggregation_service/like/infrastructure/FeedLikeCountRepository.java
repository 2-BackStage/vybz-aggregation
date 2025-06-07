package com.vybz.aggregation_service.like.infrastructure;

import com.vybz.aggregation_service.like.domain.FeedLikeCount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FeedLikeCountRepository extends MongoRepository<FeedLikeCount, String> {

    /**
     * 주어진 피드 ID에 해당하는 좋아요 수를 반환합니다.
     *
     * @param feedId 피드의 ID
     * @return 해당 피드에 달린 총 좋아요 수
     */
    long countByFeedId(String feedId);


    /**
     * 피드 ID로 FeedLikeCount 조회
     *
     * @param feedId 피드의 ID
     * @return 해당 피드의 좋아요 수 정보
     */
   Optional<FeedLikeCount> findByFeedId(String feedId);
}
