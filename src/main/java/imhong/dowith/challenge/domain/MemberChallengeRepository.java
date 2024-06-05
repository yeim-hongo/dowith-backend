package imhong.dowith.challenge.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {

    Optional<MemberChallenge> findByMemberId(Long memberId);
}
