package imhong.dowith.member.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import imhong.dowith.common.CustomException;
import imhong.dowith.member.enums.MemberExceptionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String userId;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 60)
    private String password;

    public static Member create(String userId, String nickname, String password) {
        validateUserId(userId);
        validateNickname(nickname);

        return new Member(
            null,
            userId,
            nickname,
            password
        );
    }

    private static void validateUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new CustomException(MemberExceptionType.USER_ID_EMPTY);
        }

        if (userId.length() < 4 || userId.length() > 20) {
            throw new CustomException(MemberExceptionType.USER_ID_LENGTH);
        }
    }

    private static void validateNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new CustomException(MemberExceptionType.NICKNAME_EMPTY);
        }

        if (nickname.length() < 4 || nickname.length() > 20) {
            throw new CustomException(MemberExceptionType.NICKNAME_LENGTH);
        }
    }
}
