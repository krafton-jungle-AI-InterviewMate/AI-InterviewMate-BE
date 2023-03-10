package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionBoxInfoDto;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionInfoDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.QuestionBoxesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "questionBoxes", description = "질문 관련 ")
@RestController
@RequestMapping("/questionBoxes")
public class QuestionBoxesController {

    private final QuestionBoxesService questionBoxesService;

    @Autowired
    public QuestionBoxesController(QuestionBoxesService questionBoxesService) {
        this.questionBoxesService = questionBoxesService;
    }

    @Operation(summary = "질문 꾸러미 List [꾸러미에 속한 질문들 포함 X] 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionBoxInfoDto.class))))
    })
    @GetMapping("")
    public ResponseEntity<PrivateResponseBody> getQuestionBoxes() {
        List<QuestionBoxInfoDto> questionBoxList = questionBoxesService.getQuestionBoxes();
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, questionBoxList), HttpStatus.OK);
    }

    @Operation(summary = "질문 꾸러미 1개의 정보 [꾸러미에 속한 질문들 포함] 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestionBoxInfoDto.class)))
    })
    @GetMapping("/{questionBoxIdx}")
    public ResponseEntity<PrivateResponseBody> getQuestionBoxInfo(@PathVariable("questionBoxIdx") Long questionBoxIdx) {
        QuestionBoxInfoDto dto = questionBoxesService.getQuestionBoxInfo(questionBoxIdx);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, dto), HttpStatus.OK);
    }

    @Operation(summary = "질문 꾸러미 이름 수정하기", description = "질문 꾸러미 이름 수정시에 Body에 json 으로 questionBoxName 요소만 String Type으로 보내주시면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @PutMapping("/{questionBoxIdx}")
    public ResponseEntity<PrivateResponseBody> updateQuestionBoxName(@PathVariable("questionBoxIdx") Long questionBoxIdx, @RequestBody QuestionBoxInfoDto questionBoxInfoDto) {
        questionBoxesService.updateQuestionBoxName(questionBoxIdx, questionBoxInfoDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

    @Operation(summary = "질문 꾸러미에 질문 추가하기", description = "Body에 questionIdx 는 전달할 필요 X, 사용하지 않는 Keyword도 전달할 필요 X 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @PostMapping("/{questionBoxIdx}")
    public ResponseEntity<PrivateResponseBody> createQuestion(@PathVariable("questionBoxIdx") Long questionBoxIdx, @RequestBody QuestionInfoDto questionInfoDto) {
        questionBoxesService.createQuestion(questionBoxIdx, questionInfoDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }


    @Operation(summary = "질문 꾸러미의 질문 비우기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @DeleteMapping("/{questionBoxIdx}")
    public ResponseEntity<PrivateResponseBody> clearQuestionBox(@PathVariable("questionBoxIdx") Long questionBoxIdx) {
        questionBoxesService.clearQuestionBox(questionBoxIdx);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

    @Operation(summary = "질문 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @DeleteMapping("/question/{questionIdx}")
    public ResponseEntity<PrivateResponseBody> deleteQuestion(@PathVariable("questionIdx") Long questionIdx) {
        questionBoxesService.deleteQuestion(questionIdx);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

    @Operation(summary = "질문 정보 [제목, 키워드] 수정하기", description = "Body에 questionIdx 는 전달할 필요 X, 사용하지 않는 Keyword도 전달할 필요 X 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @PutMapping("/question/{questionIdx}")
    public ResponseEntity<PrivateResponseBody> updateQuestion(@PathVariable("questionIdx") Long questionIdx, @RequestBody QuestionInfoDto questionInfoDto) {
        questionBoxesService.updateQuestion(questionIdx, questionInfoDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

}
