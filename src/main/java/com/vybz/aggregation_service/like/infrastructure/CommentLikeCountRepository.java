package com.vybz.aggregation_service.like.infrastructure;

import com.vybz.aggregation_service.like.domain.CommentLikeCount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CommentLikeCountRepository extends MongoRepository<CommentLikeCount, String> {

    /**
     * 주어진 댓글 ID에 해당하는 좋아요 수를 반환합니다.
     *
     * @param commentId 댓글의 ID
     * @return 해당 댓글에 달린 총 좋아요 수
     */
    long countByCommentId(String commentId);

    /**
     * 댓글 ID로 CommentLikeCount 조회
     *
     * @param commentId 댓글의 ID
     * @return 해당 댓글의 좋아요 수 정보
     */
    Optional<CommentLikeCount> findByCommentId(String commentId);
}
