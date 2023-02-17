package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "login", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

//    @GetMapping("/auth/{socialLoginType}/callback")
//    public ResponseEntity<PrivateResponseBody> callback(
//            @PathVariable() String socialLoginType,
//            @RequestParam(name = "code") String code) throws IOException {
//        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :" + code);
//        LoginConstant loginConstant = LoginConstant.valueOf(socialLoginType.toUpperCase());
//        LoginGetSocialAuthRes getSocialOAuthRes = loginService.oAuthLogin(loginConstant, code);
//        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, getSocialOAuthRes), HttpStatus.OK);
//    }
}
