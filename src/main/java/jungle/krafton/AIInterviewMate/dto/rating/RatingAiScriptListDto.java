package jungle.krafton.AIInterviewMate.dto.rating;

import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.Script;
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
    private Integer rating;

    public RatingAiScriptListDto(Question question, Script script) {
        this.questionTitle = question.getQuestionTitle();
        this.script = script.getScript();
        this.rating = script.getRating();
    }
}
