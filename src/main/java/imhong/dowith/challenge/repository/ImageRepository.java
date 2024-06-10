package imhong.dowith.challenge.repository;

import imhong.dowith.challenge.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByChallengeId(Long challengeId);
}
