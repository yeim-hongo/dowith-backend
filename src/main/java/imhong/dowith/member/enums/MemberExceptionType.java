package imhong.dowith.member.enums;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import imhong.dowith.common.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionType implements BaseExceptionType {
    USER_ID_EMPTY(BAD_REQUEST, "M01", "사용자 ID는 비워둘 수 없습니다."),
    USER_ID_LENGTH(BAD_REQUEST, "M02", "사용자 ID는 4자에서 20자 사이여야 합니다."),
    NICKNAME_EMPTY(BAD_REQUEST, "M03", "닉네임은 비워둘 수 없습니다."),
    NICKNAME_LENGTH(BAD_REQUEST, "M04", "닉네임은 4자에서 20자 사이여야 합니다."),
    PASSWORD_EMPTY(BAD_REQUEST, "M05", "비밀번호는 비워둘 수 없습니다."),
    PASSWORD_LENGTH(BAD_REQUEST, "M06", "비밀번호는 8자에서 20자 사이여야 합니다."),
    PASSWORD_PATTERN(BAD_REQUEST, "M07", "비밀번호는 최소 하나의 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
