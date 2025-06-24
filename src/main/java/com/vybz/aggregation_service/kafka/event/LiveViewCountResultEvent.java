package com.vybz.aggregation_service.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LiveViewCountResultEvent {

    private String streamKey;
    private Integer totalViewerCount;


    @Builder
    public LiveViewCountResultEvent(String streamKey,
                                    Integer totalViewerCount) {
        this.streamKey = streamKey;
        this.totalViewerCount = totalViewerCount;
    }
}
