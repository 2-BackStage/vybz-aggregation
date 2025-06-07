package com.vybz.aggregation_service.comment.batch.policy;

public class CommentCountDisplayPolicy {

    public static String convert(long count) {
        if (count < 1000) return String.valueOf(count);
        double k = count / 1000.0;
        return String.format("%.1fk", k);
    }
}
