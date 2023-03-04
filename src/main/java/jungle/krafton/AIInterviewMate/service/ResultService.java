package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.result.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import jungle.krafton.AIInterviewMate.repository.*;
import jungle.krafton.AIInterviewMate.util.OpenViduCustomWrapper;
import jungle.krafton.AIInterviewMate.util.TimelineComparator;
import jungle.krafton.AIInterviewMate.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {
    private final InterviewRoomRepository interviewRoomRepository;
    private final ResultRepository resultRepository;
    private final CommentRepository commentRepository;
    private final ScriptRepository scriptRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final Validator validator;
    private final JwtTokenProvider jwtTokenProvider;
    private final OpenViduCustomWrapper openViduCustomWrapper;


    @Autowired
    public ResultService(InterviewRoomRepository interviewRoomRepository,
                         ResultRepository resultRepository,
                         CommentRepository commentRepository,
                         ScriptRepository scriptRepository,
                         QuestionRepository questionRepository,
                         MemberRepository memberRepository,
                         Validator validator,
                         JwtTokenProvider jwtTokenProvider,
                         OpenViduCustomWrapper openViduCustomWrapper) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.commentRepository = commentRepository;
        this.scriptRepository = scriptRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.openViduCustomWrapper = openViduCustomWrapper;
    }

    public List<ResultHistoryDto> getResultHistory() {
        Long memberIdx = jwtTokenProvider.getUserInfo();
        List<InterviewRoom> interviewRooms = interviewRoomRepository.findAllByMemberIdxOrderByCreatedAtDesc(memberIdx);

        List<ResultHistoryDto> resultHistoryDtos = new ArrayList<>();

        for (InterviewRoom interviewRoom : interviewRooms) {
            if (interviewRoom.getRoomStatus() == RoomStatus.EXIT) {
                resultHistoryDtos.add(new ResultHistoryDto(interviewRoom));
            }
        }

        return resultHistoryDtos;
    }

    @Transactional
    public void saveResult(Long roomIdx, ResultInterviewDto resultInterviewDto) {
        InterviewRoom interviewRoom = interviewRoomRepository.findById(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        validator.validateAccessMember(interviewRoom.getMember(), jwtTokenProvider);

        String eyeTimelines = convertTimelinesToString(resultInterviewDto.getEyeTimelines());
        String attitudeTimelines = convertTimelinesToString(resultInterviewDto.getAttitudeTimelines());
        String questionTimelines = convertTimelinesToString(resultInterviewDto.getQuestionTimelines());

        if (resultRepository.findByInterviewRoomIdx(roomIdx).isPresent()) {
            throw new PrivateException(StatusCode.NOT_ACCESS_DATA_DUPLICATE);
        }

        resultRepository.save(convertDtoToResult(interviewRoom, eyeTimelines, attitudeTimelines, questionTimelines));

        if (interviewRoom.getRoomType().equals(RoomType.USER)) {
            if (resultInterviewDto.getComments() != null) {
                for (ResultInterviewCommentDto resultInterviewCommentDto : resultInterviewDto.getComments()) {
                    commentRepository.save(convertDtoToComment(interviewRoom, resultInterviewCommentDto));
                }
            }
        } else {
            if (resultInterviewDto.getScripts() != null) {
                for (ResultInterviewScriptDto resultInterviewScriptDto : resultInterviewDto.getScripts()) {
                    List<String> keywords = new ArrayList<>();
                    Question question = questionRepository.findByIdx(resultInterviewScriptDto.getQuestionIdx())
                            .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));
                    keywords.add(question.getKeyword1());
                    keywords.add(question.getKeyword2());
                    keywords.add(question.getKeyword3());
                    keywords.add(question.getKeyword4());
                    keywords.add(question.getKeyword5());

                    String script = resultInterviewScriptDto.getScript();
                    int matchCnt = 1;
                    for (String keyword : keywords) {
                        if (keyword == null) {
                            continue;
                        }
                        script = script.replace(keyword, "<" + matchCnt + ">" + keyword + "</" + matchCnt + ">");
                        matchCnt++;
                    }

                    scriptRepository.save(convertDtoToScript(interviewRoom, resultInterviewScriptDto, script));
                }
            }
        }

        if (interviewRoom.getRoomType().equals(RoomType.USER)) {
            openViduCustomWrapper.closeSession(interviewRoom.getSessionId());
        }
    }

    private String convertTimelinesToString(List<String> timelines) {
        if (timelines == null || timelines.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String timeline : timelines) {
            stringBuilder.append(",").append(timeline);
        }

        stringBuilder.deleteCharAt(0);

        return stringBuilder.toString();
    }

    private Result convertDtoToResult(InterviewRoom interviewRoom, String eyeTimelines, String attitudeTimelines, String questionTimelines) {
        return Result.builder()
                .interviewRoom(interviewRoom)
                .eyeTimeline(eyeTimelines)
                .attitudeTimeline(attitudeTimelines)
                .questionTimeline(questionTimelines)
                .build();
    }

    private Script convertDtoToScript(InterviewRoom interviewRoom, ResultInterviewScriptDto resultInterviewScriptDto, String script) {
        return Script.builder()
                .interviewRoom(interviewRoom)
                .questionIdx(resultInterviewScriptDto.getQuestionIdx())
                .script(script)
                .build();
    }

    private Comment convertDtoToComment(InterviewRoom interviewRoom, ResultInterviewCommentDto resultInterviewCommentDto) {
        return Comment.builder()
                .interviewRoom(interviewRoom)
                .viewerIdx(resultInterviewCommentDto.getViewerIdx())
                .comment(resultInterviewCommentDto.getComment())
                .build();
    }

    public ResultAiResponseDto getAiResult(Long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        if (!interviewRoom.getRoomType().equals(RoomType.AI)) {
            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
        }

        validator.validateAccessMember(interviewRoom.getMember(), jwtTokenProvider);

        Result result = resultRepository.findByInterviewRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_RESULT));

        List<ResultTimelineDto> totalTimelines = new ArrayList<>();

        addResultDtoToTimeline(result.getEyeTimeline(), totalTimelines, "eye");
        addResultDtoToTimeline(result.getAttitudeTimeline(), totalTimelines, "attitude");
        addResultDtoToTimeline(result.getQuestionTimeline(), totalTimelines, "question");

        totalTimelines.sort(new TimelineComparator());

        List<ResultResponseScriptDto> scripts = new ArrayList<>();

        for (Script script : scriptRepository.findAllByInterviewRoomIdx(roomIdx)) {
            String questionTitle = questionRepository.findByIdx(script.getQuestionIdx())
                    .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION))
                    .getQuestionTitle();
            scripts.add(convertScriptToDto(script, questionTitle));
        }

        return new ResultAiResponseDto(result, totalTimelines, scripts, interviewRoom);
    }

    public ResultUserResponseDto getUserResult(Long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        if (!interviewRoom.getRoomType().equals(RoomType.USER)) {
            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
        }

        validator.validateAccessMember(interviewRoom.getMember(), jwtTokenProvider);

        Result result = resultRepository.findByInterviewRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_RESULT));
        List<ResultTimelineDto> totalTimelines = new ArrayList<>();

        addResultDtoToTimeline(result.getEyeTimeline(), totalTimelines, "eye");
        addResultDtoToTimeline(result.getAttitudeTimeline(), totalTimelines, "attitude");
        addResultDtoToTimeline(result.getQuestionTimeline(), totalTimelines, "question");

        totalTimelines.sort(new TimelineComparator());

        List<ResultResponseCommentDto> comments = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByInterviewRoomIdx(roomIdx)) {
            comments.add(convertCommentToDto(comment));
        }
        return new ResultUserResponseDto(result, totalTimelines, comments, interviewRoom);
    }

    public ResultResponseScriptDto convertScriptToDto(Script script, String questionTitle) {
        return ResultResponseScriptDto.builder()
                .script(script.getScript())
                .questionTitle(questionTitle)
                .build();
    }

    public ResultResponseCommentDto convertCommentToDto(Comment comment) {
        return ResultResponseCommentDto.builder()
                .comment(comment.getComment())
                .build();
    }

    public void saveComment(Long roomIdx, ResultRequestCommentDto resultRequestComment) {
        Long memberIdx = jwtTokenProvider.getUserInfo();

        Member viewer = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));

        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        validator.validateContents(resultRequestComment.getComment());

        Comment comment = Comment.builder()
                .interviewRoom(interviewRoom)
                .viewerIdx(viewer.getIdx())
                .comment(resultRequestComment.getComment())
                .build();

        commentRepository.save(comment);
    }

    public void saveMemo(Long roomIdx, ResultMemoDto resultMemoDto) {
        Result result = resultRepository.findByInterviewRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_RESULT));

        InterviewRoom interviewRoom = result.getInterviewRoom();
        validator.validateAccessMember(interviewRoom.getMember(), jwtTokenProvider);

        validator.validateContents(resultMemoDto.getMemo());

        result.setMemo(resultMemoDto.getMemo());

        resultRepository.save(result);
    }

    private void addResultDtoToTimeline(String timeline, List<ResultTimelineDto> totalTimelines, String type) {
        if (timeline == null) {
            return;
        }
        for (String temp : timeline.split(",")) {
            totalTimelines.add(ResultTimelineDto.builder()
                    .type(type)
                    .timestamp(temp)
                    .build());
        }
    }
}
