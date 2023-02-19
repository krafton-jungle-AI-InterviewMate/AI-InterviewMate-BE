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
}
