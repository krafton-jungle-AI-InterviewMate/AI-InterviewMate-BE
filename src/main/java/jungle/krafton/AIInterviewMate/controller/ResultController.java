package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import jungle.krafton.AIInterviewMate.dto.result.RatingAiResponseDto;
import jungle.krafton.AIInterviewMate.dto.result.RatingHistoryDto;
import jungle.krafton.AIInterviewMate.dto.result.ResultInterviewDto;
import jungle.krafton.AIInterviewMate.dto.result.ResultRequestCommentDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "result", description = "채점 관련 API")
@RestController
@RequestMapping("/result")
public class ResultController {
    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @Operation(summary = "면접 결과 전체 리스트 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RatingHistoryDto.class))))
    })
    @GetMapping("/history")
    public ResponseEntity<PrivateResponseBody> getRatingHistory() {
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, resultService.getRatingHistory()), HttpStatus.OK);
    }

    @Operation(summary = "면접 결과 저장 ")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "roomIdx", description = "방 번호", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
    })
    @PostMapping(value = "/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> saveRating(@PathVariable Long roomIdx, @RequestBody ResultInterviewDto resultInterviewDto) {
        resultService.saveResult(roomIdx, resultInterviewDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

    @Operation(summary = "면접 결과 확인", description = "타입에 따라 다른 Dto가 출력 됩니다.")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "roomIdx", description = "방 번호", example = "1"),
            @Parameter(in = ParameterIn.QUERY, name = "type", description = "방 타입", example = "AI")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RatingAiResponseDto.class)))
    })
    @GetMapping("/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> getRatingList(@PathVariable Long roomIdx, @RequestParam(name = "type") RoomType roomType) {

        if (roomType.equals(RoomType.AI)) {
            return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, resultService.getAiResult(roomIdx)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, resultService.getUserResult(roomIdx)), HttpStatus.OK);
        }
    }

    @Operation(summary = "면접 코멘트 저장", description = "body에 'comment' 라는 이름으로 String 데이터 넣어주시면 됩니다!!")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "roomIdx", description = "방 번호", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
    })
    @PostMapping(value = "/{roomIdx}/comment")
    public ResponseEntity<PrivateResponseBody> saveComment(@PathVariable Long roomIdx, @RequestBody ResultRequestCommentDto resultRequestComment) {
        resultService.saveComment(roomIdx, resultRequestComment);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }
}
