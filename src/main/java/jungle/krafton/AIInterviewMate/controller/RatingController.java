package jungle.krafton.AIInterviewMate.controller;

import jungle.krafton.AIInterviewMate.domain.RoomType;
import jungle.krafton.AIInterviewMate.dto.rating.RatingInterviewDto;
import jungle.krafton.AIInterviewMate.exception.PrivateResponseBody;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/history")
    public ResponseEntity<PrivateResponseBody> getRatingHistory() {
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, ratingService.getRatingHistory()), HttpStatus.OK);
    }

    @PostMapping(value = "/{roomIdx}/viewee")
    public ResponseEntity<PrivateResponseBody> saveRating(@PathVariable int roomIdx, @RequestBody RatingInterviewDto ratingInterviewDto) {
        ratingService.saveRating(roomIdx, ratingInterviewDto);
        return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, null), HttpStatus.OK);
    }

    @GetMapping("/{roomIdx}")
    public ResponseEntity<PrivateResponseBody> getRatingList(@PathVariable Long roomIdx, @RequestParam(name = "type") RoomType roomType) {
        if (roomType.equals(RoomType.AI)) {
            return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, ratingService.getAiRatingList(roomIdx)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new PrivateResponseBody(StatusCode.OK, ratingService.getUserRatingList(roomIdx)), HttpStatus.OK);
        }
    }
}
