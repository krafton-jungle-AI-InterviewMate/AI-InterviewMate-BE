package jungle.krafton.AIInterviewMate.service;

import io.openvidu.java.client.OpenVidu;
import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.interview.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;
    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;
    private OpenVidu openVidu;

    @Autowired
    public InterviewService(InterviewRoomRepository interviewRoomRepository,
                            QuestionRepository questionRepository,
                            MemberRepository memberRepository,
                            Validator validator,
                            JwtTokenProvider jwtTokenProvider) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostConstruct
    public void init() {
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    @Transactional
    public InterviewRoomInfoUserDto enterInterviewRoom(Long roomIdx, InterviewRoomEnterRequestDto requestDto) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        RoomType roomType = interviewRoom.getRoomType();
        validator.validateRoomType(roomType, RoomType.USER);

        validator.validatePassword(requestDto.getPassword(), interviewRoom.getRoomPassword());

        Long memberIdx = jwtTokenProvider.getUserInfo();
        Member memberToEnter = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        return getUserRoomInfo(interviewRoom, memberToEnter);
    }

    private InterviewRoomInfoUserDto getUserRoomInfo(InterviewRoom interviewRoom, Member memberToEnter) {
        //TODO: 실 서비스 사용시에 해당 부분 확인을 해야함.
//        checkMemberToEnterIdx(interviewRoom, memberToEnter);

        OpenViduInfo openViduInfo = OpenViduInfo.of(openVidu, interviewRoom, memberToEnter);

        InterviewRoomInfoUserDto dto = new InterviewRoomInfoUserDto(interviewRoom, memberToEnter);
        dto.setConnectionToken(openViduInfo.getConnectionToken());
        return dto;
    }

    //TODO: Entity 수정에 맞춰서 로직 바꿔주세요...ㅎㅎ..
    private void checkMemberToEnterIdx(InterviewRoom interviewRoom, Member memberToEnter) {
        Long viewer1Idx = interviewRoom.getRoomViewer1Idx();
        Long viewer2Idx = interviewRoom.getRoomViewer2Idx();
        Long viewer3Idx = interviewRoom.getRoomViewer3Idx();
        Long hostMemberIdx = interviewRoom.getMember().getIdx();
        Long memberToEnterIdx = memberToEnter.getIdx();

        if (
                Objects.equals(hostMemberIdx, memberToEnterIdx) //방 Host 와 동일한 Id로 로그인 시도
                        || (viewer1Idx != null && Objects.equals(viewer1Idx, memberToEnterIdx)) //동일한 면접관이 또 접속을 하려고 하는지 확인
                        || (viewer2Idx != null && Objects.equals(viewer2Idx, memberToEnterIdx)) //동일한 면접관이 또 접속을 하려고 하는지 확인
                        || (viewer3Idx != null && Objects.equals(viewer3Idx, memberToEnterIdx)) //동일한 면접관이 또 접속을 하려고 하는지 확인
        ) {
            throw new PrivateException(StatusCode.ROOM_VIEWER_ERROR);
        }

        if (viewer1Idx == null) {
            interviewRoom.setRoomViewer1Idx(memberToEnterIdx);
            return;
        } else if (viewer2Idx == null) {
            interviewRoom.setRoomViewer2Idx(memberToEnterIdx);
            return;
        } else if (viewer3Idx == null) {
            interviewRoom.setRoomViewer3Idx(memberToEnterIdx);
            return;
        }

        throw new PrivateException(StatusCode.ROOM_VIEWER_ERROR);
    }

    public List<InterviewRoomListDto> getRoomList() {
        List<InterviewRoomListDto> roomList = new ArrayList<>();

        List<InterviewRoom> allRoom = interviewRoomRepository
                .findAllByRoomStatusOrRoomStatusOrderByCreatedAtDescRoomStatus(RoomStatus.CREATE, RoomStatus.PROCEED);
        for (InterviewRoom room : allRoom) {
            roomList.add(convertCreateAndProceedRoom(room));
        }

        return roomList;
    }

    @Transactional
    public InterviewRoomCreateResponseDto createInterviewRoom(InterviewRoomCreateRequestDto requestDto) {
        Long memberIdx = jwtTokenProvider.getUserInfo();
        Member member = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        InterviewRoom interviewRoom = interviewRoomRepository.save(createInterviewRoom(requestDto, member));

        InterviewRoomCreateResponseDto dto = new InterviewRoomCreateResponseDto(interviewRoom, member);

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
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

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

    private InterviewRoomListDto convertCreateAndProceedRoom(InterviewRoom interviewRoom) {
        return InterviewRoomListDto.builder()
                .idx(interviewRoom.getIdx())
                .roomStatus(interviewRoom.getRoomStatus())
                .roomType(interviewRoom.getRoomType())
                .roomName(interviewRoom.getRoomName())
                .nickname(interviewRoom.getMember().getNickname())
                .roomPeopleNum(interviewRoom.getRoomPeopleNum())
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
}



