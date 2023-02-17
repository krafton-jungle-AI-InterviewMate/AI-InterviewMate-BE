package jungle.krafton.AIInterviewMate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jungle.krafton.AIInterviewMate.dto.interview.*;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "interview", description = "면접방 관련 API")
@RestController
@RequestMapping("/interview")
public class InterviewController {
    private final InterviewService interviewService;

    @Autowired
    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @Operation(summary = "면접 방 정보 상세")
    @Parameters({
            @Parameter(in = ParameterIn.PATH, name = "roomIdx", description = "방 번호", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI_OK", content = @Content(schema = @Schema(implementation = InterviewRoomInfoAiDto.class)))
    })
    @GetMapping("/rooms/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> getRoomInfo(@PathVariable("roomIdx") Long roomIdx) {
        InterviewRoomInfoDto interviewRoomInfo = interviewService.getRoomInfo(roomIdx);

        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, interviewRoomInfo), HttpStatus.OK);
    }

    @Operation(summary = "방 리스트 전체 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InterviewRoomListDto.class)))
    })
    @GetMapping("/rooms")
    public ResponseEntity<PrivateResponseBody> getRoomList() {
        List<InterviewRoomListDto> roomList = interviewService.getRoomList();

        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, roomList), HttpStatus.OK);
    }

    @Operation(summary = "방 생성하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = InterviewRoomCreateResponseDto.class)))
    })
    @PostMapping("/rooms/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> createRoom(InterviewRoomCreateRequestDto requestDto) {
        InterviewRoomCreateResponseDto createRoom = interviewService.createRoom(requestDto);

        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, createRoom), HttpStatus.OK);
    }
}
