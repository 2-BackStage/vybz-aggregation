package com.vybz.aggregation_service.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /**
     * 200: 요청 성공
     **/
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),
    EMAIL_CODE_SUCCESS(HttpStatus.OK, true, 201, "이메일 인증코드 발송에 성공하였습니다."),
    EMAIL_CODE_VERIFICATION_SUCCESS(HttpStatus.OK, true, 202, "이메일 인증에 성공하였습니다."),
    SIGN_UP_SUCCESS(HttpStatus.OK, true, 203, "회원가입에 성공하였습니다."),
    SIGN_IN_SUCCESS(HttpStatus.OK, true, 204, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, true, 205, "로그아웃 되었습니다."),
    SUCCESS_MATCH_PASSWORD(HttpStatus.OK, true, 206, "비밀번호가 확인되었습니다."),
    SUCCESS_UPDATE_PASSWORD(HttpStatus.OK, true, 207, "비밀번호가 변경되었습니다."),
    SUCCESS_UPDATE_NICKNAME(HttpStatus.OK, true, 208, "닉네임이 변경되었습니다."),
    SUCCESS_RECENT_VIEW(HttpStatus.OK, true, 209, "최근 본 상품이 등록되었습니다."),
    SUCCESS_WITHDRAWAL_USER(HttpStatus.OK, true, 210, "회원 탈퇴가 완료되었습니다. 2주안에 재 로그인 시 계정 복구가 가능합니다."),
    SUCCESS_ACCOUNT_RECOVERY(HttpStatus.OK, true, 211, "계정 복구가 완료되었습니다. 로그인 해주세요."),
    NO_OAUTH_USER(HttpStatus.OK, true, 2200, "소셜 계정이 존재하지 않습니다. 추가 정보를 입력해 회원가입 해주세요."),
    SUCCESS_DOWNLOAD_COUPON(HttpStatus.OK, true, 2201, "쿠폰 다운로드에 성공하였습니다."),
    SUCCESS_USE_COUPON(HttpStatus.OK, true, 2202, "쿠폰 사용에 성공하였습니다."),

    /**
     * 400 : security 에러
     */
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "토큰이 유효하지 않습니다"),
    NO_SIGN_IN(HttpStatus.UNAUTHORIZED, false, 402, "로그인을 먼저 진행해주세요"),
    NO_ACCESS_AUTHORITY(HttpStatus.FORBIDDEN, false, 403, "접근 권한이 없습니다"),
    DISABLED_USER(HttpStatus.FORBIDDEN, false, 404, "비활성화된 계정입니다. 계정을 복구하시겠습니까?"),
    FAILED_TO_RESTORE(HttpStatus.INTERNAL_SERVER_ERROR, false, 405, "계정 복구에 실패했습니다. 관리자에게 문의해주세요."),
    NO_EXIST_OAUTH(HttpStatus.NOT_FOUND, false, 406, "소셜 로그인 정보가 존재하지 않습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, false, 407, "이메일 또는 패스워드를 다시 확인해주세요."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, false, 408, "Refresh Token이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, false, 409, "Refresh Token이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, false, 410, "유효하지 않은 Access Token입니다."),

    /**
     * 900: 기타 에러
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 900, "Internal server error"),
    SSE_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 901, "알림 전송에 실패하였습니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 902, "로그인에 실패하였습니다."),
    COMMENT_BATCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 903, "댓글 수 배치 실행에 실패하였습니다."),



    /**
     * Request 유효성 에러
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, false, 1000, "잘못된 요청입니다."),

    /**
     * 카테고리 에러
     */
    DUPLICATE_CATEGORY(HttpStatus.BAD_REQUEST, false, 1100, "중복된 카테고리입니다."),
    NO_EXIST_CATEGORY(HttpStatus.BAD_REQUEST, false, 1101, "존재하지 않는 카테고리입니다."),

    /**
     * 유저 정보 에러
     */
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, false, 1200, "이미 존재하는 회원입니다."),
    NO_EXIST_USER(HttpStatus.BAD_REQUEST, false, 1201, "존재하지 않는 회원입니다."),

    /**
     * 버스커 정보 에러
     */
    DUPLICATE_BUSKER(HttpStatus.BAD_REQUEST, false, 1300, "이미 존재하는 버스커입니다."),
    NO_EXIST_BUSKER(HttpStatus.BAD_REQUEST, false, 1301, "존재하지 않는 버스커입니다."),

    /**
     * 버스커 그룹 에러
     */
    DUPLICATE_BUSKER_GROUP(HttpStatus.BAD_REQUEST, false, 1400, "이미 존재하는 버스커 그룹입니다."),
    NO_EXIST_BUSKER_GROUP(HttpStatus.BAD_REQUEST, false, 1401, "존재하지 않는 버스커 그룹입니다."),
    DUPLICATE_BUSKER_MEMBER(HttpStatus.BAD_REQUEST, false, 1402, "이미 존재하는 버스커 멤버입니다."),
    NO_EXIST_BUSKER_MEMBER(HttpStatus.BAD_REQUEST, false, 1403, "존재하지 않는 버스커 멤버입니다."),

    /**
     * 버스커 SNS 에러
     */
    DUPLICATE_BUSKER_SNS(HttpStatus.BAD_REQUEST, false, 1500, "이미 존재하는 버스커 SNS 입니다."),
    NO_EXIST_BUSKER_SNS(HttpStatus.BAD_REQUEST, false, 1501, "존재하지 않는 버스커 SNS 입니다."),

    /**
     * 약관 에러
     */
    DUPLICATE_AGREEMENT(HttpStatus.BAD_REQUEST, false, 1600, "이미 존재하는 약관입니다."),
    NO_EXIST_AGREEMENT(HttpStatus.BAD_REQUEST, false, 1601, "존재하지 않는 약관입니다."),
    NO_EXIST_AGREEMENT_TYPE(HttpStatus.BAD_REQUEST, false, 1602, "존재하지 않는 약관 종류입니다."),
    ALREADY_AGREED(HttpStatus.BAD_REQUEST, false, 1603, "이미 동의한 약관입니다."),
    NO_EXIST_USER_AGREEMENT(HttpStatus.BAD_REQUEST, false, 1604, "존재하지 않는 유저 약관입니다."),

    /**
     * 팔로우 에러
     */
    ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST, false, 1700, "이미 팔로우한 버스커입니다."),
    NO_EXIST_FOLLOW(HttpStatus.BAD_REQUEST, false, 1701, "존재하지 않는 팔로우입니다.");

    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}
