package jungle.krafton.AIInterviewMate.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class RatingUserListDto {
    private String viewerName;
    private Integer eyesRating;
    private Integer attitudeRating;
    private Integer answerRating;
    private List<RatingUserCommentDto> commentList;

    public RatingUserListDto(String nickname, VieweeRating vieweeRating, List<RatingUserCommentDto> commentList) {
        this.viewerName = nickname;
        this.eyesRating = vieweeRating.getEyesRating();
        this.attitudeRating = vieweeRating.getAttitudeRating();
        this.answerRating = vieweeRating.getAnswerRating();
        this.commentList = commentList;
    }
}
