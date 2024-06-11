package imhong.dowith.auth.service;

import imhong.dowith.common.CustomException;
import imhong.dowith.member.enums.MemberExceptionType;
import java.util.regex.Pattern;

public class PasswordValidator {

    // 비밀번호 패턴 (최소 1개의 대문자, 소문자, 숫자, 특수문자 포함)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*]).{8,20}$"
    );

    public static void validate(String password) {
        if (password == null || password.isEmpty()) {
            throw new CustomException(MemberExceptionType.PASSWORD_EMPTY);
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new CustomException(MemberExceptionType.PASSWORD_LENGTH);
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new CustomException(MemberExceptionType.PASSWORD_PATTERN);
        }
    }
}
