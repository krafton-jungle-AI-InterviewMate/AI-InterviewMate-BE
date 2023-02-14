package jungle.krafton.AIInterviewMate.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginGetSocialAuthRes {
    private String jwtToken;
    private int user_num;
    private String accessToken;
    private String tokenType;
}
