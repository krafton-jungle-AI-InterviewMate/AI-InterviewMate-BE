package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.dto.mypage.MyInfoDto;
import jungle.krafton.AIInterviewMate.dto.mypage.MyPageDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MyPageService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


        Member member = memberRepository.findByEmail(mypageDto.getEmail())
    public void updateNickname(MyPageDto myPageDto) {
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        member.setNickname(myPageDto.getNickname());

        memberRepository.save(member);
    }

    public MyInfoDto getMyInfo() {
        Long memberIdx = jwtTokenProvider.getUserInfo();
        Member member = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));

        return new MyInfoDto(member);
    }
}
