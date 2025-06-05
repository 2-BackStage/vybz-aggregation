package com.vybz.aggregation_service.follow.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field(name = "total_follower_count")
    private Integer totalFollowerCount;

    public void increaseCount() {
        this.totalFollowerCount++;
    }

    public void decreaseCount() {
        if (this.totalFollowerCount > 0) {
            this.totalFollowerCount--;
        }
    }

    @Builder
    public BuskerFollowerCount(String id, String buskerUuid, Integer totalFollowerCount) {
        this.id = id;
        this.buskerUuid = buskerUuid;
        this.totalFollowerCount = totalFollowerCount != null ? totalFollowerCount : 0;
    }
}
