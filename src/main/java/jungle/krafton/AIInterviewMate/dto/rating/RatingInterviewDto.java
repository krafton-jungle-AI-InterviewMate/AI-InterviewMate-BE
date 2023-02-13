package jungle.krafton.AIInterviewMate.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class RatingInterviewDto {
    private Long viewerIdx;
    private Integer eyesRating;
    private Integer attitudeRating;
    private Integer answerRating;
    private List<CommentsRequestDto> commentsRequestDtos;
    private List<ScriptSaveDto> scriptRequestsDtos;
}

