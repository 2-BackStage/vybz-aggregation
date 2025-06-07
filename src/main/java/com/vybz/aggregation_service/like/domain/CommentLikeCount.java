package com.vybz.aggregation_service.like.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("comment_like_count")
public class CommentLikeCount {

    @Id
    private String id;

    /**
     * 댓글 ID
     */
    private String commentId;

    /**
     * 총 좋아요 수
     */
    private Integer totalLikeCount;

    /**
     * 사용자에게 보여줄 가공된 좋아요 수 (ex. "1.2k")
     */
    private String displayLikeCount;

    /**
     * 마지막 집계 시각
     */
    private Instant updatedAt;

    @Builder
    public CommentLikeCount(String id,
                            String commentId,
                            Integer totalLikeCount,
                            String displayLikeCount,
                            Instant updatedAt) {
        this.id = id;
        this.commentId = commentId;
        this.totalLikeCount = totalLikeCount != null ? totalLikeCount : 0;
        this.displayLikeCount = displayLikeCount;
        this.updatedAt = updatedAt;
    }

    public void updateCount(int delta) {
        this.totalLikeCount = Math.max(0, (this.totalLikeCount == null ? 0 : this.totalLikeCount) + delta);
        this.displayLikeCount = formatCount(this.totalLikeCount);
        this.updatedAt = Instant.now();
    }

    private String formatCount(int count) {
        if (count >= 1_000_000) {
            return String.format("%.1fM", count / 1_000_000.0);
        } else if (count >= 1_000) {
            return String.format("%.1fk", count / 1_000.0);
        } else {
            return String.valueOf(count);
        }
    }

}
