package jungle.krafton.AIInterviewMate.dto.mypage;

import jungle.krafton.AIInterviewMate.domain.AuthProvider;
import jungle.krafton.AIInterviewMate.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyInfoDto {
    private Long idx;
    private String email;
    private String nickname;
    private AuthProvider authProvider;


    public MyInfoDto(Member member) {
        this.idx = member.getIdx();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.authProvider = member.getAuthProvider();
    }
}
