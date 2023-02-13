package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.ScriptsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Tag(name = "scripts", description = "스크립트 관련 API")
@RestController
@RequestMapping("/scripts")
public class ScriptsController {
    private final ScriptsService scriptsService;

    public ScriptsController(ScriptsService scriptsService) {
        this.scriptsService = scriptsService;
    }

    @Operation(summary = "스크립트 저장")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "roomIdx", description = "방 번호", example = "1"),
            @Parameter(in = ParameterIn.QUERY, name = "idx", description = "질문 Idx", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @PostMapping("/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> saveScript(@RequestParam("idx") Long questionIdx, @PathVariable Long roomIdx, @RequestBody HashMap<String, Object> script) {
        scriptsService.saveScript(questionIdx, roomIdx, script);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

}
