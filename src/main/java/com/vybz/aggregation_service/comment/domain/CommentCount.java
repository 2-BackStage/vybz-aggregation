package com.vybz.aggregation_service.comment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("comment_count")
public class CommentCount {

    @Id
    private String id;
    /**
     * 피드 ID
     */
    @Indexed(unique = true)
    private String feedId;
    /**
     * 피드 타입
     */
    private FeedType feedType;
    /**
     * 총 댓글 수
     */
    private Integer totalCommentCount;

    /**
     * 사용자에게 보여줄 가공된 댓글 수 (ex. "1.2k")
     */
    private String displayCommentCount;

    /**
     * 마지막 집계 시각
     */
    private Instant updatedAt;

    @Builder
    public CommentCount(String id,
                        String feedId,
                        FeedType feedType,
                        Integer totalCommentCount,
                        String displayCommentCount,
                        Instant updatedAt) {
        this.id = id;
        this.feedId = feedId;
        this.feedType = feedType;
        this.totalCommentCount = totalCommentCount != null ? totalCommentCount : 0;
        this.displayCommentCount = displayCommentCount;
        this.updatedAt = updatedAt;
    }
    public void updateCount(int delta) {
        this.totalCommentCount = Math.max(0, (this.totalCommentCount == null ? 0 : this.totalCommentCount) + delta);
        this.displayCommentCount = formatCount(this.totalCommentCount);
        this.updatedAt = Instant.now();
    }

    public void setCount(int exactCount) {
        this.totalCommentCount = Math.max(0, exactCount);
        this.displayCommentCount = formatCount(this.totalCommentCount);
        this.updatedAt = Instant.now();
    }



    private String formatCount(int count) {
        if (count >= 1_000_000) {
            return String.format("%.1fM", count / 1_000_000.0);
        } else if (count >= 1_000) {
            return String.format("%.1fk", count / 1_000.0);
        } else {
            return Integer.toString(count);
        }
    }
}

