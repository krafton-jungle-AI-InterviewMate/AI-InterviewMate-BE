package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.dto.mypage.UserInfoDto;
import jungle.krafton.AIInterviewMate.dto.rating.MypageDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MypageService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void updateNickname(MypageDto mypageDto) {

        Member member = memberRepository.findByEmail(mypageDto.getEmail())
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        member.setNickname(mypageDto.getNickname());

        memberRepository.save(member);
    }

    public UserInfoDto getUserInfo() {
        Long idx = Long.valueOf(jwtTokenProvider.getUserInfo());
        Member member = memberRepository.findByIdx(idx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));

        return new UserInfoDto(member);
    }
}