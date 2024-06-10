package imhong.dowith.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotNull(message = "아이디를 입력해주세요.")
    private String userId;

    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
}
