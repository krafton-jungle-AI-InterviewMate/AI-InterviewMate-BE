package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.interview.*;
import jungle.krafton.AIInterviewMate.dto.openvidu.OpenViduInfoDto;
import jungle.krafton.AIInterviewMate.dto.questionbox.QuestionInfoDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import jungle.krafton.AIInterviewMate.repository.CommentRepository;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import jungle.krafton.AIInterviewMate.repository.MemberRepository;
import jungle.krafton.AIInterviewMate.repository.QuestionRepository;
import jungle.krafton.AIInterviewMate.util.OpenViduCustomWrapper;
import jungle.krafton.AIInterviewMate.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewService {
    private final InterviewRoomRepository interviewRoomRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final Validator validator;
    private final JwtTokenProvider jwtTokenProvider;
    private final OpenViduCustomWrapper openViduCustomWrapper;

    @Autowired
    public InterviewService(InterviewRoomRepository interviewRoomRepository,
                            QuestionRepository questionRepository,
                            MemberRepository memberRepository,
                            CommentRepository commentRepository,
                            Validator validator,
                            JwtTokenProvider jwtTokenProvider,
                            OpenViduCustomWrapper openViduCustomWrapper) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.validator = validator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.openViduCustomWrapper = openViduCustomWrapper;
    }

    @Transactional
    public InterviewRoomInfoUserDto enterInterviewRoom(Long roomIdx, InterviewRoomEnterRequestDto requestDto) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        RoomType roomType = interviewRoom.getRoomType();
        validator.validateRoomType(roomType, RoomType.USER);

        validator.validateEnterRoomStatus(interviewRoom.getRoomStatus());

        validator.validatePassword(requestDto.getPassword(), interviewRoom.getRoomPassword());

        Long memberIdx = jwtTokenProvider.getUserInfo();
        Member memberToEnter = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_USER));

        //TODO: 실 서비스 시에 확인할 부분
//        validator.validateHostToRejoin(interviewRoom.getMember(), memberToEnter);

        return getUserRoomInfo(interviewRoom, memberToEnter);
    }

    @Transactional
    public void exitInterviewRoom(Long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        RoomStatus roomStatus = interviewRoom.getRoomStatus();

        validator.validateExitRoomStatus(roomStatus);

        RoomType roomType = interviewRoom.getRoomType();
        if (roomType == RoomType.USER) {
            if (interviewRoom.getRoomStatus() == RoomStatus.CREATE) {
                exitNormalUserInterviewRoom(interviewRoom);
            } else {
                exitAbnormalUserInterviewRoom(interviewRoom);
            }
        } else {
            deleteAbnormalAiInterviewRoom(interviewRoom);
        }
    }

    private void exitNormalUserInterviewRoom(InterviewRoom interviewRoom) {
        /*
            면접자가 나감 => 더 이상의 방을 유지할 필요가 없으므로 Session 을 종료
            면접관이 나가면 면접관 List 업데이트
         */
        if (hasHostLeftRoom(interviewRoom.getMember())) {
            openViduCustomWrapper.closeSession(interviewRoom.getSessionId());

            interviewRoomRepository.delete(interviewRoom);
        } else {
            updateExitInterviewerIdxes(interviewRoom);
        }
    }

    private void exitAbnormalUserInterviewRoom(InterviewRoom interviewRoom) {
        /*
            면접자가 나감 || 마지막 면접관이 방을 나감 => 더 이상의 방을 유지할 필요가 없으므로 Session 을 종료
            그리고 면접관의 comment가 하나도 없으면 면접 결과도 확인할 필요가 없으므로 방을 삭제.
         */
        if (hasHostLeftRoom(interviewRoom.getMember()) || isAllViewersOut(interviewRoom)) {
            openViduCustomWrapper.closeSession(interviewRoom.getSessionId());

            if (viewerCommentsEmpty(interviewRoom)) {
                interviewRoomRepository.delete(interviewRoom);
            }
        } else {
            updateExitInterviewerIdxes(interviewRoom);
        }
    }

    private void updateExitInterviewerIdxes(InterviewRoom interviewRoom) {
        String interviewerIdxes = interviewRoom.getInterviewerIdxes();
        if (interviewerIdxes == null) {
            throw new PrivateException(StatusCode.NOT_FOUND_INTERVIEWERS);
        }

        String[] memberIdxes = interviewerIdxes.split(",");
        String memberIdxToExit = String.valueOf(jwtTokenProvider.getUserInfo());

        String saveIdxes = Arrays.stream(memberIdxes)
                .filter(memberIdx -> !memberIdx.equals(memberIdxToExit))
                .collect(Collectors.joining(","));

        if (saveIdxes.isEmpty()) {
            saveIdxes = null;
        }

        interviewRoom.setInterviewerIdxes(saveIdxes);
    }

    private boolean viewerCommentsEmpty(InterviewRoom interviewRoom) {
        List<Comment> comments = commentRepository.findAllByInterviewRoomIdx(interviewRoom.getIdx());
        return comments.isEmpty();
    }

    private boolean isAllViewersOut(InterviewRoom interviewRoom) {
        String interviewerIdxes = interviewRoom.getInterviewerIdxes();
        if (interviewerIdxes == null) {
            throw new PrivateException(StatusCode.NOT_FOUND_INTERVIEWERS);
        }

        String[] memberIdxes = interviewerIdxes.split(",");
        Long memberIdx = jwtTokenProvider.getUserInfo();

        return memberIdxes.length == 1 && Long.parseLong(memberIdxes[0]) == memberIdx;
    }

    private boolean hasHostLeftRoom(Member host) {
        Long memberIdxToExit = jwtTokenProvider.getUserInfo();

        return Objects.equals(host.getIdx(), memberIdxToExit);
    }

    private void deleteAbnormalAiInterviewRoom(InterviewRoom interviewRoom) {
        interviewRoomRepository.delete(interviewRoom);
    }

    private InterviewRoomInfoUserDto getUserRoomInfo(InterviewRoom interviewRoom, Member memberToEnter) {
        addMemberToInterviewerIdxes(interviewRoom, memberToEnter);

        OpenViduInfoDto openViduInfoDto = openViduCustomWrapper.createOpenViduInfoDto(interviewRoom, memberToEnter);

        List<Question> questions = questionRepository.findAllByQuestionBoxIdx(interviewRoom.getRoomQuestionBoxIdx());
        InterviewRoomInfoUserDto dto = new InterviewRoomInfoUserDto(interviewRoom, questions);
        dto.setConnectionToken(openViduInfoDto.getConnectionToken());
        return dto;
    }

    private void addMemberToInterviewerIdxes(InterviewRoom interviewRoom, Member memberToEnter) {
        String interviewerIdxes = interviewRoom.getInterviewerIdxes();
        String memberToEnterIdx = String.valueOf(memberToEnter.getIdx());
        List<String> idxes = new ArrayList<>();

        if (interviewerIdxes != null) {
            String[] viewerStrIdxArr = interviewerIdxes.split(",");
            idxes = new ArrayList<>(List.of(viewerStrIdxArr));

            validator.validateMemberToEnterInterviewerRole(idxes, interviewRoom.getRoomPeopleNum(), memberToEnterIdx);
        }

        idxes.add(memberToEnterIdx);
        String saveIdxes = idxes.stream().sorted().collect(Collectors.joining(","));

        interviewRoom.setInterviewerIdxes(saveIdxes);
    }

    public List<InterviewRoomListDto> getRoomList() {
        List<InterviewRoomListDto> roomList = new ArrayList<>();

        List<InterviewRoom> allRoom = interviewRoomRepository
                .findAllByRoomStatusNotOrderByRoomTypeDescRoomStatusAscCreatedAtDesc(RoomStatus.EXIT);
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
            OpenViduInfoDto openViduInfoDto = openViduCustomWrapper.createOpenViduInfoDto(interviewRoom, member);

            interviewRoom.setSessionId(openViduInfoDto.getSessionId());

            dto.setConnectionToken(openViduInfoDto.getConnectionToken());
        } else {
            List<QuestionInfoDto> questionList = createQuestionList(interviewRoom);

            dto.setQuestionList(questionList);
        }

        return dto;
    }

    private List<QuestionInfoDto> createQuestionList(InterviewRoom interviewRoom) {
        Long questionBoxIdx = interviewRoom.getRoomQuestionBoxIdx();

        List<Question> questions = questionRepository.findAllByQuestionBoxIdx(questionBoxIdx);
        if (questions.isEmpty()) {
            throw new PrivateException(StatusCode.NOT_FOUND_QUESTION);
        }

        List<QuestionInfoDto> interviewQuestions = new ArrayList<>();
        int questionNum = interviewRoom.getRoomQuestionNum();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        if (questionNum >= questions.size()) {
            for (Question question : questions) {
                interviewQuestions.add(QuestionInfoDto.of(question));
            }
        } else {
            Set<Integer> randomSet = new HashSet<>();

            while (randomSet.size() != questionNum) {
                randomSet.add(random.nextInt(questions.size()));
            }

            for (int idx : randomSet) {
                interviewQuestions.add(QuestionInfoDto.of(questions.get(idx)));
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
        String interviewerIdxes = interviewRoom.getInterviewerIdxes();
        List<String> idxes = null;
        if (interviewerIdxes != null) {
            idxes = List.of(interviewerIdxes.split(","));
        }

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
                .interviewerIdxes(idxes)
                .build();
    }


    private InterviewRoom createInterviewRoom(InterviewRoomCreateRequestDto requestDto, Member member) {
        validator.validateDto(requestDto);

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

    public Page<InterviewRoomListDto> getRoomListByPage(long memberIdx, int page) {
        page = page - 1; // page 기준은 0부터 시작함

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 20, sort);

        Page<InterviewRoom> pages = interviewRoomRepository.findAllByMemberIdx(memberIdx, pageable);

        Page<InterviewRoomListDto> rtDto = pages.map(data -> InterviewRoomListDto.builder()
                .idx(data.getIdx())
                .roomName(data.getRoomName())
                .nickname("Tester")
                .roomPeopleNum(data.getRoomPeopleNum())
                .roomTime(data.getRoomTime())
                .roomIsPrivate(data.getIsPrivate())
                .roomType(data.getRoomType())
                .roomStatus(data.getRoomStatus())
                .createdAt(data.getCreatedAt())
                .interviewerIdxes(null)
                .build());

        return rtDto;
    }

    public List<InterviewRoomListDto> getRoomListByNoOffset(long memberIdx, int stanIdx) {
        List<InterviewRoom> pages = interviewRoomRepository.findAllByMemberIdxGraterThanStanIdx(memberIdx, stanIdx);

        List<InterviewRoomListDto> rtDto = new ArrayList<>();
        for (InterviewRoom page : pages) {
            rtDto.add(InterviewRoomListDto.builder()
                    .idx(page.getIdx())
                    .roomName(page.getRoomName())
                    .nickname("Tester")
                    .roomPeopleNum(page.getRoomPeopleNum())
                    .roomTime(page.getRoomTime())
                    .roomIsPrivate(page.getIsPrivate())
                    .roomType(page.getRoomType())
                    .roomStatus(page.getRoomStatus())
                    .createdAt(page.getCreatedAt())
                    .interviewerIdxes(null)
                    .build());
        }

        return rtDto;
    }
}



