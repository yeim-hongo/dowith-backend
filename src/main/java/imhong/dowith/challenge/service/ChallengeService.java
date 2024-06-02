package imhong.dowith.challenge.service;


import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.ChallengeRepository;
import imhong.dowith.challenge.domain.ImageRepository;
import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ImageRepository imageRepository;
    private final ChallengeImageUploader imageUploader;

    public Long createChallenge(Member leader, ChallengeCreateRequest request) {
        Challenge challenge = Challenge.create(
            request.getTitle(),
            request.getDescription(),
            request.getVerificationRule(),
            imageUploader.uploadThumbnail(request.getThumbnail()),
            request.getStartDate(),
            request.getEndDate(),
            request.getMinParticipantsCount(),
            request.getMaxParticipantsCount(),
            leader.getId()
        );
        Challenge savedChallenge = challengeRepository.save(challenge);
        imageRepository.saveAll(imageUploader.uploadImages(request.getImages(), savedChallenge));
        return savedChallenge.getId();
    }
}
