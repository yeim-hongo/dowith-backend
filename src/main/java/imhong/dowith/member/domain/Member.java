package imhong.dowith.member.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private Long id;
    private String userId;
    private String nickname;
    private String password;
}
