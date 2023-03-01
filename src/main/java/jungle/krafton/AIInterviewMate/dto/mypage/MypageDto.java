package jungle.krafton.AIInterviewMate.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MyPageDto {
    private String nickname;
    private String email;
}
