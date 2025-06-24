package com.vybz.aggregation_service.live.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("live_view_count")
public class LiveViewCount {

    @Id
    private String id;

    private String streamKey;

    private Integer totalViewerCount;

    private Instant updatedAt;

    @Builder
    public LiveViewCount(String id,
                         String streamKey,
                         Integer totalViewerCount,
                         Instant updatedAt) {
        this.id = id;
        this.streamKey = streamKey;
        this.totalViewerCount = totalViewerCount;
        this.updatedAt = updatedAt;
    }
}
