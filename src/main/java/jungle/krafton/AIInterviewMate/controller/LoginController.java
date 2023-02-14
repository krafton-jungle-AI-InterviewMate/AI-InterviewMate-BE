package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.dto.login.LoginConstant;
import jungle.krafton.AIInterviewMate.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "login", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable() String socialLoginType) throws IOException {
        LoginConstant loginConstant = LoginConstant.valueOf(socialLoginType.toUpperCase());
        loginService.socialLoginRequest(loginConstant);
    }


//    @GetMapping("/auth/{socialLoginType}/callback")
//    public BaseResponse<LoginGetSocialAuthRes> callback(
//            @PathVariable() String socialLoginType,
//            @RequestParam(name = "code") String code) throws IOException, BaseException {
//        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :" + code);
//        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginType.toUpperCase());
//        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLogin(socialLoginType, code);
//        return new BaseResponse<>(getSocialOAuthRes);
//    }
}
