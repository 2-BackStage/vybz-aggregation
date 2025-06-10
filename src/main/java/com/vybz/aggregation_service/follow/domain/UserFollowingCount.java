package com.vybz.aggregation_service.follow.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Document("user_following_count")
public class UserFollowingCount {

    @Id
    private String id;

    /**
     * 유저 uuid
     */
    @Field(name = "user_uuid")
    private String userUuid;

    /**
     * 총 팔로잉 수
     */
    @Field(name = "following_count")
    private Integer followingCount;

    /**
     * 생성일
     */
    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    /**
     * 수정일
     */
    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;

    public void increaseCount() {
        this.followingCount++;
    }

    public void decreaseCount() {
        if (this.followingCount > 0) {
            this.followingCount--;
        }
    }

    @Builder
    public UserFollowingCount(String id, String userUuid, Integer followingCount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.followingCount = followingCount != null ? followingCount : 0;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
