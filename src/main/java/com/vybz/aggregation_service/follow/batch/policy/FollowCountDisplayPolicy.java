package com.vybz.aggregation_service.follow.batch.policy;

public class FollowCountDisplayPolicy {

    public static String convert(int count) {
        if (count >= 1000000) {
            return String.format("%.1fM", count / 1000000.0);
        } else if (count >= 1000) {
            return String.format("%.1fK", count / 1000.0);
        } else {
            return String.valueOf(count);
        }
    }

}
