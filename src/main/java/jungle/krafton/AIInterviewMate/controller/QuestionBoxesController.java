package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionBoxListDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.QuestionBoxesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @Operation(summary = "질문 리스트와 키워드 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Question.class))))
    })
    @GetMapping("/{questionBoxIdx}")
    public ResponseEntity<PrivateResponseBody> createQuestionList(@PathVariable("questionBoxIdx") Long questionBoxIdx) {
        List<Question> returnQuestionList = questionBoxesService.createQuestionList(questionBoxIdx);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, returnQuestionList), HttpStatus.OK);
    }

    @Operation(summary = "질문 꾸러미 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionBoxListDto.class))))
    })
    @GetMapping
    public ResponseEntity<PrivateResponseBody> createQuestionBox(@RequestBody HashMap<String, Object> email) {
        List<QuestionBoxListDto> returnQuestionBoxList = questionBoxesService.createQuestionBox((String) email.get("email"));
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, returnQuestionBoxList), HttpStatus.OK);
    }

    @Operation(summary = "질문 꾸러미의 질문 비우기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content())
    })
    @DeleteMapping("/{questionBoxIdx}")
    public ResponseEntity<PrivateResponseBody> deleteQuestionList(@PathVariable("questionBoxIdx") Long questionBoxIdx) {
        questionBoxesService.deleteQuestionList(questionBoxIdx);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }
}
