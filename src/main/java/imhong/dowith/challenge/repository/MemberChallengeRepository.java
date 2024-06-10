package imhong.dowith.challenge.repository;

import imhong.dowith.challenge.domain.MemberChallenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {

    Optional<MemberChallenge> findByMemberId(Long memberId);
}
