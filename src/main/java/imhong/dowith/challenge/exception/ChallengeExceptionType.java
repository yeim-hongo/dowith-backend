package imhong.dowith.challenge.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import imhong.dowith.challenge.service.ChallengeImageUploader;
import imhong.dowith.common.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ChallengeExceptionType implements BaseExceptionType {
    CHALLENGE_NOT_FOUND(NOT_FOUND, "C01", "존재하지 않는 챌린지입니다."),
    PARTICIPANTS_COUNT_FULL(BAD_REQUEST, "C02", "챌린지 인원이 가득 찼습니다."),
    END_DATE_BEFORE_START_DATE(BAD_REQUEST, "C03", "챌린지 종료 날짜는 시작 날짜 보다 빠를 수 없습니다."),
    INVALID_DURATION(BAD_REQUEST, "C04", "챌린지 기간은 4주 이하이어야 합니다."),
    START_DATE_BEFORE_TODAY(BAD_REQUEST, "C05", "챌린지 시작 날짜는 현재 날짜보다 빠를 수 없습니다."),
    CHALLENGE_START_DATE_TOO_FAR(BAD_REQUEST, "C06", "챌린지 시작 날짜는 현재 날짜로부터 4주 이후로 설정할 수 없습니다."),
    INVALID_MIN_PARTICIPANTS_COUNT(BAD_REQUEST, "C07", "최소 참가자 수는 1명 이상이어야 합니다."),
    INVALID_MAX_PARTICIPANTS_COUNT(BAD_REQUEST, "C08", "최대 참가자 수는 100명 이하이어야 합니다."),
    MIN_PARTICIPANTS_EXCEED_MAX_PARTICIPANTS(BAD_REQUEST, "C09", "최소 참가자 수는 최대 참가자 수보다 작아야 합니다."),
    IMAGES_SIZE_EXCEEDED(BAD_REQUEST, "C10", String.format("이미지는 최대 %d개까지 업로드할 수 있습니다.",
        ChallengeImageUploader.MAX_IMAGE_COUNT));
    private HttpStatus httpStatus;
    private String code;
    private String message;

    ChallengeExceptionType(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
