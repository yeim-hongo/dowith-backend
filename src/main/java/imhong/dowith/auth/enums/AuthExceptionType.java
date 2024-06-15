package imhong.dowith.auth.enums;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import imhong.dowith.common.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionType implements BaseExceptionType {
    USERID_DUPLICATED(CONFLICT, "A01", "이미 존재하는 User ID 입니다."),
    NICKNAME_DUPLICATED(CONFLICT, "A02", "이미 존재하는 Nickname 입니다."),
    NOT_AUTHORIZED(UNAUTHORIZED, "A03", "아이디 또는 비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
