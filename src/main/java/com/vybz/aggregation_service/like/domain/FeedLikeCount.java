package com.vybz.aggregation_service.like.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("feed_like_count")
public class FeedLikeCount {

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
    public FeedLikeCount(String id,
                         String feedId,
                         FeedType feedType,
                         Integer totalLikeCount,
                         String displayLikeCount,
                         Instant updatedAt) {
        this.id = id;
        this.feedId = feedId;
        this.feedType = feedType;
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
    public void overwrite(FeedLikeCount feedLikeCount) {
        this.feedType = feedLikeCount.getFeedType();
        this.totalLikeCount = feedLikeCount.getTotalLikeCount();
        this.displayLikeCount = feedLikeCount.getDisplayLikeCount();
        this.updatedAt = feedLikeCount.getUpdatedAt();
    }

}
