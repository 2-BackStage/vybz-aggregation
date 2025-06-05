package com.vybz.aggregation_service.follow.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field(name = "total_following_count")
    private Integer totalFollowingCount;

    public void increaseCount() {
        this.totalFollowingCount++;
    }

    public void decreaseCount() {
        this.totalFollowingCount = Math.max(0, this.totalFollowingCount - 1);
    }

    @Builder
    public UserFollowingCount(String id, String userUuid, Integer totalFollowingCount) {
        this.id = id;
        this.userUuid = userUuid;
        this.totalFollowingCount = totalFollowingCount != null ? totalFollowingCount : 0;
    }
}
