package com.vybz.aggregation_service.comment.infrastructure;

import com.vybz.aggregation_service.comment.domain.CommentCount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommentCountRepository extends MongoRepository<CommentCount,String> {

    /**
     * 주어진 피드 ID에 해당하는 댓글 수를 반환합니다.
     *
     * @param feedId 피드의 ID
     * @return 해당 피드에 달린 총 댓글 수
     */
    long countByFeedId(String feedId);

    /**
     * 피드 ID로 CommentCount 조회
     */
    Optional<CommentCount> findByFeedId(String feedId);
}




