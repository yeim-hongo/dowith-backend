package imhong.dowith.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
