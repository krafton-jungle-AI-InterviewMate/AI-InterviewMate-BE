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
import jungle.krafton.AIInterviewMate.dto.rating.RatingHistoryDto;
import jungle.krafton.AIInterviewMate.dto.rating.RatingInterviewDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "rating", description = "채점 관련 API")
@RestController
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "면접 결과 전체 리스트 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RatingHistoryDto.class))))
    })
    @GetMapping("/history")
    public ResponseEntity<PrivateResponseBody> getRatingHistory() {
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, ratingService.getRatingHistory()), HttpStatus.OK);
    }

    @Operation(summary = "면접 결과 저장 ")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "roomIdx", description = "방 번호", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
    })
    @PostMapping(value = "/{roomIdx}/viewee")
    public ResponseEntity<PrivateResponseBody> saveRating(@PathVariable int roomIdx, @RequestBody RatingInterviewDto ratingInterviewDto) {
        ratingService.saveRating(roomIdx, ratingInterviewDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }
}
