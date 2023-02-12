package jungle.krafton.AIInterviewMate.controller;

import jungle.krafton.AIInterviewMate.dto.interview.InterviewRoomInfoDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interview")
public class InterviewController {
    private final InterviewService interviewService;

    @Autowired
    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }


    @GetMapping("/rooms/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> getRoomInfo(@PathVariable("roomIdx") Long roomIdx) {
        InterviewRoomInfoDto interviewRoomInfo = interviewService.getRoomInfo(roomIdx);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, interviewRoomInfo), HttpStatus.OK);
    }
}
