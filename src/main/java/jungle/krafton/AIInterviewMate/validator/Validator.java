package jungle.krafton.AIInterviewMate.validator;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import jungle.krafton.AIInterviewMate.dto.interview.InterviewRoomCreateRequestDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Validator {
    public void validateDto(InterviewRoomCreateRequestDto requestDto) {
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

    public void validateAccessMember(Member member, JwtTokenProvider jwtTokenProvider) {
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

    public void validateContents(String contents) {
        if (contents == null || contents.trim().isEmpty()) {
            throw new PrivateException(StatusCode.CONTENTS_IS_EMPTY_ERROR);
        }
    }

    public void validateEnterRoomStatus(RoomStatus actual) {
        if (actual != RoomStatus.CREATE) {
            throw new PrivateException(StatusCode.ROOM_STATUS_ERROR);
        }
    }

    public void validateExitRoomStatus(RoomStatus actual) {
        if (actual == RoomStatus.EXIT) {
            throw new PrivateException(StatusCode.ALREADY_EXIT_ROOM);
        }
    }

    public void validateHostToRejoin(Member host, Member memberToEnter) {
        if (host.equals(memberToEnter)) {
            throw new PrivateException(StatusCode.ROOM_VIEWER_ERROR);
        }
    }

    public boolean isMaxInterviewerNum(int curSize, int maxSize) {
        return curSize >= maxSize;
    }

    public boolean hasSameInterviewer(List<String> idxes, String memberToEnterIdx) {
        return idxes.contains(memberToEnterIdx);
    }

    public void validateMemberToEnterInterviewerRole(List<String> idxes, Integer enterPeopleLimit, String memberToEnterIdx) {
        //TODO: 실 서비스 사용시에 해당 부분 확인을 해야함.

        if (
                isMaxInterviewerNum(idxes.size(), enterPeopleLimit)
//                        || hasSameInterviewer(idxes, memberToEnterIdx)
        ) {
            throw new PrivateException(StatusCode.ROOM_VIEWER_ERROR);
        }
    }
}
