package com.vybz.aggregation_service.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LiveLikeCountResultEvent {
    private String streamKey;
    private Integer totalLikeCount;

    @Builder
    public LiveLikeCountResultEvent(String streamKey,
                                    Integer totalLikeCount) {
        this.streamKey = streamKey;
        this.totalLikeCount = totalLikeCount;
    }
}
