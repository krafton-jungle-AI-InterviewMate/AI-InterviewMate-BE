package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.AuthService;
import jungle.krafton.AIInterviewMate.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "login", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;
    private final AuthService authService;

    @Autowired
    public LoginController(LoginService loginService, AuthService authService) {
        this.loginService = loginService;
        this.authService = authService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<PrivateResponseBody> refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestBody String accessToken) {
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, authService.refreshToken(request, response, accessToken)), HttpStatus.OK);
    }

    @GetMapping("/oauth2/authorization/{social}")
    public ResponseEntity<PrivateResponseBody> doSocialLogin(@PathVariable("social") String social) {
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }
}
