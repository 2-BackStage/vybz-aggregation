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
@Document("busker_follower_count")
public class BuskerFollowerCount {

    @Id
    private String id;

    /**
     * 버스커 uuid
     */
    @Field(name = "busker_uuid")
    private String buskerUuid;

    /**
     * 총 팔로워 수
     */
    @Field(name = "follower_count")
    private Integer followerCount;

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
        this.followerCount++;
    }

    public void decreaseCount() {
        if (this.followerCount > 0) {
            this.followerCount--;
        }
    }

    @Builder
    public BuskerFollowerCount(String id, String buskerUuid, Integer followerCount, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.buskerUuid = buskerUuid;
        this.followerCount = followerCount != null ? followerCount : 0;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
