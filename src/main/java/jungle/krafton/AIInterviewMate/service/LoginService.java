package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.dto.login.LoginConstant;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.util.GoogleOauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;

    @Autowired
    public LoginService(GoogleOauth googleOauth, HttpServletResponse response) {
        this.googleOauth = googleOauth;
        this.response = response;
    }

    public void socialLoginRequest(LoginConstant socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = googleOauth.getOauthRedirectURL();
            }
            break;
            default: {
                throw new PrivateException(StatusCode.NOT_FOUND_SOCIAL_LOGIN_TYPE);
            }

        }

        response.sendRedirect(redirectURL);
    }

//    public LoginGetSocialAuthRes oAuthLogin(LoginConstant socialLoginType, String code) throws IOException {
//
//        switch (socialLoginType) {
//            case GOOGLE: {
//                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
//                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
//                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
//                LoginGoogleAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
//
//                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
//                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
//                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
//                LoginUser googleUser = googleOauth.getUserInfo(userInfoResponse);
//
//            }
//            default: {
//                throw new PrivateException(StatusCode.NOT_FOUND_SOCIAL_LOGIN_TYPE);
//            }
//        }
//    }
}
