package jungle.krafton.AIInterviewMate.dto.interview;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionInfoDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewRoomInfoUserDto extends InterviewRoomCreateResponseDto {
    private List<String> interviewerIdxes;

    public InterviewRoomInfoUserDto(InterviewRoom interviewRoom, List<Question> questions) {
        super(interviewRoom, interviewRoom.getMember());

        List<QuestionInfoDto> questionList = new ArrayList<>();
        for (Question question : questions) {
            questionList.add(QuestionInfoDto.of(question));
        }
        super.setQuestionList(questionList);

        String viewerIdxes = interviewRoom.getInterviewerIdxes();
        this.interviewerIdxes = List.of(viewerIdxes.split(","));
    }
}
