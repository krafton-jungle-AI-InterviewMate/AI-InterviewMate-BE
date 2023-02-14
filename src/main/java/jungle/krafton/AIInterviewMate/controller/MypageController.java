package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.dto.rating.MypageDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "mypage", description = "마이페이지 관련 API")
@RestController
@RequestMapping("/mypage")
public class MypageController {
    private final MypageService mypageService;

    @Autowired
    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    @Operation(summary = "닉네임 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @PutMapping("/nickname")
    public ResponseEntity<PrivateResponseBody> updateMypage(@RequestBody MypageDto mypageDto) {
        mypageService.updateNickname(mypageDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }
}
