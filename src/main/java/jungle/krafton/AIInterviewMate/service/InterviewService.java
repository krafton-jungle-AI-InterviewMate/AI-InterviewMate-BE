package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.Question;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.RoomType;
import jungle.krafton.AIInterviewMate.dto.interview.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InterviewService {
    private final InterviewRoomRepository interviewRoomRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public InterviewService(InterviewRoomRepository interviewRoomRepository, QuestionRepository questionRepository) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.questionRepository = questionRepository;
    }

    public InterviewRoomInfoDto getRoomInfo(Long roomIdx) { // AI 대인에 따른 예외처리, QuestionBox의 길이가 0인 경우 예외처리
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx);
        String memberNickname = interviewRoom.getMember().getNickname();

        RoomType roomType = interviewRoom.getRoomType();
        if (roomType.equals(RoomType.USER)) {
            return new InterviewRoomInfoUserDto(interviewRoom, memberNickname);
        }

        Long questionBoxIdx = interviewRoom.getRoomQuestionBoxIdx();

        List<Question> questions = questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);
        if (questions.isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_QUESTION);
        }

        List<InterviewQuestionDto> interviewQuestions = new ArrayList<>();
        int questionNum = interviewRoom.getRoomQuestionNum();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        if (questionNum >= questions.size()) {
            for (Question question : questions) {
                interviewQuestions.add(new InterviewQuestionDto(question));
            }
        } else {
            Set<Integer> randomSet = new HashSet<>();

            while (randomSet.size() != questionNum) {
                randomSet.add(random.nextInt(questions.size()));
            }

            for (int idx : randomSet) {
                interviewQuestions.add(new InterviewQuestionDto(questions.get(idx)));
            }
        }

        return new InterviewRoomInfoAiDto(interviewRoom, memberNickname, interviewQuestions);
    }

    public List<InterviewRoomListDto> getRoomList() {
        List<InterviewRoomListDto> roomList = new ArrayList<>();

        List<InterviewRoom> allRoom = interviewRoomRepository
                .findAllByRoomStatusOrRoomStatusOrderByCreatedAtDescRoomStatus(RoomStatus.CREATE, RoomStatus.PROCEED);
        for (InterviewRoom room : allRoom) {

            int cnt = 1;
            if (room.getRoomViewer1Idx() != null) {
                cnt++;
            }
            if (room.getRoomViewer2Idx() != null) {
                cnt++;
            }
            if (room.getRoomViewer3Idx() != null) {
                cnt++;
            }
            roomList.add(convertCreateRoom(cnt, room));
        }

        return roomList;
    }

    private InterviewRoomListDto convertCreateRoom(Integer cnt, InterviewRoom interviewRoom) {
        return InterviewRoomListDto.builder()
                .roomStatus(interviewRoom.getRoomStatus())
                .roomType(interviewRoom.getRoomType())
                .roomName(interviewRoom.getRoomName())
                .nickname(interviewRoom.getMember().getNickname())
                .roomPeopleNum(interviewRoom.getRoomPeopleNum())
                .roomPeopleNow(cnt)
                .roomTime(interviewRoom.getRoomTime())
                .roomIsPrivate(interviewRoom.getIsPrivate())
                .createdAt(interviewRoom.getCreatedAt())
                .build();
    }
}



