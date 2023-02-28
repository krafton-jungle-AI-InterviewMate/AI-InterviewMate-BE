package jungle.krafton.AIInterviewMate.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class RatingUserCommentDto {
    private String questionTitle;
    private String comment;

    public RatingUserCommentDto(Comment comment) {
        this.questionTitle = comment.getQuestionTitle();
        this.comment = comment.getComment();
    }
}
