package jungle.krafton.AIInterviewMate.service;

import io.openvidu.java.client.OpenVidu;
import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.interview.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import jungle.krafton.AIInterviewMate.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class InterviewService {
    private final InterviewRoomRepository interviewRoomRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final Validator validator;

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;
    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;
    private OpenVidu openVidu;

    @Autowired
    public InterviewService(InterviewRoomRepository interviewRoomRepository, QuestionRepository questionRepository, MemberRepository memberRepository, Validator validator) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    @PostConstruct
    public void init() {
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
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
            roomList.add(convertCreateAndProceedRoom(cnt, room));
        }

        return roomList;
    }

    @Transactional
    public InterviewRoomCreateResponseDto createInterviewRoom(InterviewRoomCreateRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        InterviewRoom interviewRoom = interviewRoomRepository.save(createInterviewRoom(requestDto, member));

        InterviewRoomCreateResponseDto dto = convertInterviewRoomToDto(interviewRoom, member);

        RoomType roomType = interviewRoom.getRoomType();
        if (roomType.equals(RoomType.USER)) {
            OpenViduInfo openViduInfo = OpenViduInfo.of(openVidu, interviewRoom, member);
            interviewRoom.setSessionId(openViduInfo.getSessionId());

            dto.setConnectionToken(openViduInfo.getConnectionToken());
        } else {
            List<InterviewQuestionDto> questionList = createQuestionList(interviewRoom);

            dto.setQuestionList(questionList);
        }

        return dto;
    }

    private List<InterviewQuestionDto> createQuestionList(InterviewRoom interviewRoom) {
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

        return interviewQuestions;
    }

    public void updateRoomStatus(Long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx);
        if (interviewRoom == null) throw new PrivateException(StatusCode.NOT_FOUND_ROOM);

        RoomStatus roomStatus = interviewRoom.getRoomStatus();
        if (roomStatus.equals(RoomStatus.CREATE)) {
            roomStatus = RoomStatus.PROCEED;
        } else if (roomStatus.equals(RoomStatus.PROCEED)) {
            roomStatus = RoomStatus.EXIT;
        } else {
            throw new PrivateException(StatusCode.NOT_UPDATE_EXIT_ROOM);
        }

        interviewRoom.setRoomStatus(roomStatus);

        interviewRoomRepository.save(interviewRoom);
    }

    private InterviewRoomListDto convertCreateAndProceedRoom(Integer cnt, InterviewRoom interviewRoom) {
        return InterviewRoomListDto.builder()
                .idx(interviewRoom.getIdx())
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


    private InterviewRoom createInterviewRoom(InterviewRoomCreateRequestDto requestDto, Member member) {
        validator.validate(requestDto);

        return InterviewRoom.builder()
                .member(member)
                .roomType(requestDto.getRoomType())
                .roomName(requestDto.getRoomName())
                .roomPassword(requestDto.getRoomPassword())
                .isPrivate(requestDto.getIsPrivate())
                .roomTime(requestDto.getRoomTime())
                .roomQuestionNum(requestDto.getRoomQuestionNum())
                .roomQuestionBoxIdx(requestDto.getRoomQuestionBoxIdx())
                .roomPeopleNum(requestDto.getRoomPeopleNum())
                .build();
    }

    private InterviewRoomCreateResponseDto convertInterviewRoomToDto(InterviewRoom interviewRoom, Member member) {
        return InterviewRoomCreateResponseDto.builder()
                .roomIdx(interviewRoom.getIdx())
                .roomName(interviewRoom.getRoomName())
                .roomPeopleNum(interviewRoom.getRoomPeopleNum())
                .roomPassword(interviewRoom.getRoomPassword())
                .roomType(interviewRoom.getRoomType())
                .nickname(member.getNickname())
                .roomTime(interviewRoom.getRoomTime())
                .roomQuestionBoxIdx(interviewRoom.getRoomQuestionBoxIdx())
                .roomQuestionNum(interviewRoom.getRoomQuestionNum())
                .createdAt(interviewRoom.getCreatedAt())
                .roomStatus(interviewRoom.getRoomStatus())
                .build();
    }
}



