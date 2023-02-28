package jungle.krafton.AIInterviewMate.validator;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import jungle.krafton.AIInterviewMate.dto.interview.InterviewRoomCreateRequestDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Validator {
    public void validate(InterviewRoomCreateRequestDto requestDto) {
        if (isNameInvalid(requestDto.getRoomName())
                || requestDto.getRoomQuestionBoxIdx() == null) {
            throw new PrivateException(StatusCode.NULL_INPUT_CHAT_REQUEST);
        }
    }

    private boolean isNameInvalid(String name) {
        return name == null || name.trim().isEmpty();
    }

    public boolean hasInvalidAccess(Member member, JwtTokenProvider jwtTokenProvider) {
        return !Objects.equals(member.getIdx(), jwtTokenProvider.getUserInfo());
    }

    public void validateName(String name) {
        if (isNameInvalid(name)) {
            throw new PrivateException(StatusCode.INVALID_TITLE);
        }
    }

    public void validateMember(Member member, JwtTokenProvider jwtTokenProvider) {
        if (hasInvalidAccess(member, jwtTokenProvider)) {
            throw new PrivateException(StatusCode.WRONG_REQUEST);
        }
    }

    public void validatePassword(String actual, String needed) {
        if (needed != null && !needed.equals(actual)) {
            throw new PrivateException(StatusCode.WRONG_PASSWORD);
        }
    }

    public void validateRoomType(RoomType actual, RoomType needed) {
        if (!needed.equals(actual)) {
            throw new PrivateException(StatusCode.ROOM_TYPE_ERROR);
        }
    }
}
