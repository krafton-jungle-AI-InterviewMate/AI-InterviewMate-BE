package jungle.krafton.AIInterviewMate.dto.rating;

import jungle.krafton.AIInterviewMate.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class RatingAiScriptListDto {
    private String questionTitle;
    private String script;

    public RatingAiScriptListDto(Question question, String script) {
        this.questionTitle = question.getQuestionTitle();
        this.script = script;
    }
}
