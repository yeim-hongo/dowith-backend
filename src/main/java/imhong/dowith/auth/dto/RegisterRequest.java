package imhong.dowith.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotNull(message = "아이디를 입력해주세요.")
    @Size(min = 6, max = 12, message = "사용자 ID는 6자에서 12자 사이여야 합니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9_-]{6,12}$",
        message = "아이디는 영문자, 숫자, 밑줄, 하이픈만 사용 가능합니다."
    )
    private String userId;

    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(min = 1, max = 12, message = "닉네임은 1자에서 12자 사이여야 합니다.")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣_-]{1,12}$",
        message = "닉네임은 영문자, 숫자, 밑줄, 하이픈, 한글만 사용 가능합니다."
    )
    private String nickname;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 사이여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&~+=?!*_-])(?!.*\\\\s).{8,20}$",
        message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자, 특수문자(@#$%^&~+=?!*_-)를 포함해야 하며, 공백을 포함할 수 없습니다."
    )
    private String password;
}
