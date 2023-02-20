package jungle.krafton.AIInterviewMate.validator;

import jungle.krafton.AIInterviewMate.dto.interview.InterviewRoomCreateRequestDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    public void validate(InterviewRoomCreateRequestDto requestDto) {
        if (!isNameValid(requestDto.getRoomName())
                || requestDto.getRoomQuestionBoxIdx() == null) {
            throw new PrivateException(StatusCode.WRONG_INPUT_REQUEST);
        }
    }

    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

}
