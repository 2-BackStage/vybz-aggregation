package com.vybz.aggregation_service.kafka.event;

import com.vybz.aggregation_service.comment.domain.FeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeltaEvent  {
    private String feedId;
    private String commentId;
    private FeedType feedType;
    private int delta;
}
