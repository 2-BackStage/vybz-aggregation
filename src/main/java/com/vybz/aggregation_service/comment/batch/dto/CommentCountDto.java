package com.vybz.aggregation_service.comment.batch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class CommentCountDto {

    private String feedId;
    private Integer totalCommentCount;
    private String displayCommentCount;
    private Instant updatedAt;

    @Builder
    public CommentCountDto(String feedId,
                           Integer totalCommentCount,
                           String displayCommentCount,
                           Instant updatedAt) {
        this.feedId = feedId;
        this.totalCommentCount = totalCommentCount != null ? totalCommentCount : 0;
        this.displayCommentCount = displayCommentCount;
        this.updatedAt = updatedAt;
    }

}
