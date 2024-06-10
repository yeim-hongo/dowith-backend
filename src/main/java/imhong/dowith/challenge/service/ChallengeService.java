package imhong.dowith.challenge.service;


import static imhong.dowith.challenge.enums.ChallengeExceptionType.CHALLENGE_NOT_FOUND;
import static imhong.dowith.challenge.enums.ChallengeExceptionType.PARTICIPANTS_COUNT_FULL;

import imhong.dowith.challenge.domain.Challenge;
import imhong.dowith.challenge.domain.MemberChallenge;
import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.challenge.repository.ChallengeRepository;
import imhong.dowith.challenge.repository.ImageRepository;
import imhong.dowith.challenge.repository.MemberChallengeRepository;
import imhong.dowith.common.CustomException;
import imhong.dowith.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final ImageRepository imageRepository;
    private final ChallengeImageUploader imageUploader;

    public Long createChallenge(Member leader, ChallengeCreateRequest request) {
        // TODO ImageUploader를 트랜잭션에서 분리
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
        memberChallengeRepository.save(
            MemberChallenge.createLeader(leader.getId(), savedChallenge.getId()));
        return savedChallenge.getId();
    }

    public void participate(Member participant, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
            .orElseThrow(() -> new CustomException(CHALLENGE_NOT_FOUND));

        if (challenge.isFull()) {
            throw new CustomException(PARTICIPANTS_COUNT_FULL);
        }
        challenge.increaseParticipantsCount();
        challengeRepository.save(challenge);
        memberChallengeRepository.save(
            MemberChallenge.createParticipant(participant.getId(), challengeId));
    }
}
