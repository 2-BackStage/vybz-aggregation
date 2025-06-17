package com.vybz.aggregation_service.like.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("live_like_count")
public class LiveLikeCount {

    @Id
    private String id;

    /**
     * 스트림 키
     */
    private String streamKey;

    /**
     * 총 좋아요 수
     */
    private Integer totalLikeCount;

    /**
     * 마지막 집계 시각
     */
    private Instant updatedAt;

    @Builder
    public LiveLikeCount(String id,
                         String streamKey,
                         Integer totalLikeCount,
                         Instant updatedAt) {
        this.id = id;
        this.streamKey = streamKey;
        this.totalLikeCount = totalLikeCount != null ? totalLikeCount : 0;
        this.updatedAt = updatedAt;
    }
}
