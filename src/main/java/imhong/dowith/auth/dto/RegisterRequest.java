package imhong.dowith.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotNull(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 20, message = "사용자 ID는 4자에서 20자 사이여야 합니다.")
    private String userId;

    @NotNull(message = "닉네임을 입력해주세요.")
    @Size(min = 4, max = 20, message = "닉네임은 4자에서 20자 사이여야 합니다.")
    private String nickname;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 사이여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!*]).{8,20}$",
        message = "비밀번호는 8자 이상 20자 이하이며, 최소 하나의 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;
}
