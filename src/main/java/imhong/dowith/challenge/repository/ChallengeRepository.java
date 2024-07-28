package imhong.dowith.challenge.repository;

import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.ChallengeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("SELECT c FROM Challenge c")
    Slice<Challenge> findAllSlice(Pageable pageable);

    Slice<Challenge> findAllByStatus(ChallengeStatus status, Pageable pageable);
}
