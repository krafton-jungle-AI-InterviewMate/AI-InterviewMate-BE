package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.dto.mypage.MyInfoDto;
import jungle.krafton.AIInterviewMate.dto.mypage.MyPageDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "mypage", description = "마이페이지 관련 API")
@RestController
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService mypageService;

    @Autowired
    public MyPageController(MyPageService mypageService) {
        this.mypageService = mypageService;
    }

    @Operation(summary = "닉네임 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @PutMapping("/nickname")
    public ResponseEntity<PrivateResponseBody> updateNickname(@RequestBody MyPageDto myPageDto) {
        mypageService.updateNickname(myPageDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

    @Operation(summary = "회원 정보 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Member.class)))
    })
    @GetMapping("/myinfo")
    public ResponseEntity<PrivateResponseBody> getMyInfo() {
        MyInfoDto myInfo = mypageService.getMyInfo();
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, myInfo), HttpStatus.OK);
    }
}
