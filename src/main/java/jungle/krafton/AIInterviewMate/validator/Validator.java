package jungle.krafton.AIInterviewMate.validator;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.dto.interview.InterviewRoomCreateRequestDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Validator {
    public void validate(InterviewRoomCreateRequestDto requestDto) {
        if (!isNameValid(requestDto.getRoomName())
                || requestDto.getRoomQuestionBoxIdx() == null) {
            throw new PrivateException(StatusCode.NULL_INPUT_CHAT_REQUEST);
        }
    }

    public boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isMemberAccessValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public boolean isMemberAccessValid(Member member, JwtTokenProvider jwtTokenProvider) {
        return Objects.equals(member.getIdx(), jwtTokenProvider.getUserInfo());
    }

    public void validateName(String name) {
        if (!isNameValid(name)) {
            throw new PrivateException(StatusCode.INVALID_TITLE);
        }
    }
}
