package imhong.dowith.challenge.controller;

import imhong.dowith.challenge.dto.ChallengeCreateRequest;
import imhong.dowith.challenge.service.ChallengeService;
import imhong.dowith.member.domain.Member;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<Void> createChallenge(
        Member leader,
        @RequestBody ChallengeCreateRequest request
    ) {
        Long challengeId = challengeService.createChallenge(leader, request);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }
}
