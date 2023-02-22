package jungle.krafton.AIInterviewMate.dto.questionbox;

import jungle.krafton.AIInterviewMate.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QuestionInfoDto {
    private String keyword1;

    private String keyword2;

    private String keyword3;

    private String keyword4;

    private String keyword5;

    private String questionTitle;

    public static QuestionInfoDto of(Question question) {
        return QuestionInfoDto.builder()
                .keyword1(question.getKeyword1())
                .keyword2(question.getKeyword2())
                .keyword3(question.getKeyword3())
                .keyword4(question.getKeyword4())
                .keyword5(question.getKeyword5())
                .questionTitle(question.getQuestionTitle())
                .build();
    }
}
