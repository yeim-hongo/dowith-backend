package imhong.dowith.auth.service;

import static imhong.dowith.auth.enums.AuthExceptionType.NICKNAME_DUPLICATED;
import static imhong.dowith.auth.enums.AuthExceptionType.NOT_AUTHORIZED;
import static imhong.dowith.auth.enums.AuthExceptionType.USERID_DUPLICATED;

import imhong.dowith.auth.dto.LoginRequest;
import imhong.dowith.auth.dto.RegisterRequest;
import imhong.dowith.common.CustomException;
import imhong.dowith.common.security.JwtTokenProvider;
import imhong.dowith.common.security.PasswordEncoder;
import imhong.dowith.member.domain.Member;
import imhong.dowith.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        if (memberRepository.existsByUserId(request.getUserId())) {
            throw new CustomException(USERID_DUPLICATED);
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(NICKNAME_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = Member.create(request.getUserId(), request.getNickname(), encodedPassword);
        memberRepository.save(member);

        return jwtTokenProvider.generateToken(member.getUserId());
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest request) {
        Member member = memberRepository.findByUserId(request.getUserId())
            .orElseThrow(() -> new CustomException(NOT_AUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(NOT_AUTHORIZED);
        }

        return jwtTokenProvider.generateToken(member.getUserId());
    }
}
