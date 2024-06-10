package imhong.dowith.challenge.controller;

import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.challenge.service.ChallengeService;
import imhong.dowith.member.domain.Member;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<Void> createChallenge(
        // TODO 인가 추가 후 변경
        @RequestPart("leader") Member leader,
        @ModelAttribute ChallengeCreateRequest request
    ) {
        Long challengeId = challengeService.createChallenge(leader, request);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }

    @PostMapping("/{challengeId}/participants")
    public ResponseEntity<Void> participate(
        // TODO 인가 추가 후 변경
        @RequestBody Member participant,
        @PathVariable Long challengeId
    ) {
        challengeService.participate(participant, challengeId);
        return ResponseEntity.noContent().build();
    }
}
