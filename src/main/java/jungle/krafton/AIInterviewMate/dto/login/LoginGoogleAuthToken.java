package jungle.krafton.AIInterviewMate.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginGoogleAuthToken {
    private String accessToken;
    private int expiresIn;
    private String scope;
    private String tokenType;
    private String idToken;
}
