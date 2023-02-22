package jungle.krafton.AIInterviewMate.dto.questionbox;

import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class QuestionBoxInfoDto {
    private Long questionBoxIdx;
    private String questionBoxName;
    private Integer questionNum;
    private List<QuestionInfoDto> questions;

    public static QuestionBoxInfoDto of(QuestionBox questionBox, List<QuestionInfoDto> questionInfoDtos) {
        return QuestionBoxInfoDto.builder()
                .questionBoxIdx(questionBox.getIdx())
                .questionBoxName(questionBox.getBoxName())
                .questionNum(questionBox.getQuestionNum())
                .questions(questionInfoDtos)
                .build();
    }
}
