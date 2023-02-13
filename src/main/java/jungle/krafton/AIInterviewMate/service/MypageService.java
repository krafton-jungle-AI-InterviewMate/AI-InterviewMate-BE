package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.dto.rating.MypageDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    private final MemberRepository memberRepository;

    @Autowired
    public MypageService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public void updateNickname(MypageDto mypageNicknameDto) {

        Member member = memberRepository.findByEmail(mypageNicknameDto.getEmail())
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        member.setNickname(mypageNicknameDto.getNickname());

        memberRepository.save(member);
    }
}