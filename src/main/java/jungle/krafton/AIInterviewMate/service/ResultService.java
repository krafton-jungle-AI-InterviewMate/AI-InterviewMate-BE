package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.result.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.jwt.JwtTokenProvider;
import jungle.krafton.AIInterviewMate.repository.*;
import jungle.krafton.AIInterviewMate.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    public ResultService(InterviewRoomRepository interviewRoomRepository,
                         ResultRepository resultRepository,
                         CommentRepository commentRepository,
                         ScriptRepository scriptRepository,
                         QuestionRepository questionRepository,
                         MemberRepository memberRepository,
                         Validator validator,
                         JwtTokenProvider jwtTokenProvider) {

        this.interviewRoomRepository = interviewRoomRepository;
        this.commentRepository = commentRepository;
        this.scriptRepository = scriptRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<ResultHistoryDto> getRatingHistory() {
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

        String eyeTimelines = convertTimelinesToString(resultInterviewDto.getEyeTimelines());
        String attitudeTimelines = convertTimelinesToString(resultInterviewDto.getAttitudeTimelines());
        String questionTimelines = convertTimelinesToString(resultInterviewDto.getQuestionTimelines());

        resultRepository.save(convertDtoToResult(interviewRoom, resultInterviewDto, eyeTimelines, attitudeTimelines, questionTimelines));

        if (interviewRoom.getRoomType().equals(RoomType.USER)) {
            for (ResultInterviewCommentDto resultInterviewCommentDto : resultInterviewDto.getComments()) {
                commentRepository.save(convertDtoToComment(interviewRoom, resultInterviewCommentDto));
            }
        } else {
            for (ResultInterviewScriptDto resultInterviewScriptDto : resultInterviewDto.getScripts()) {
                scriptRepository.save(convertDtoToScript(interviewRoom, resultInterviewScriptDto));
            }
        }
    private String convertTimelinesToString(List<String> timelines) {
        if (timelines == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String timeline : timelines) {
            stringBuilder.append(",").append(timeline);
        }

        stringBuilder.deleteCharAt(0);

        return stringBuilder.toString();
    }

    private Result convertDtoToResult(InterviewRoom interviewRoom, ResultInterviewDto resultInterviewDto, String eyeTimelines, String attitudeTimelines, String questionTimelines) {
        return Result.builder()
                .interviewRoom(interviewRoom)
                .videoUrl(resultInterviewDto.getVideoUrl())
                .eyeTimeline(eyeTimelines)
                .attitudeTimeline(attitudeTimelines)
                .questionTimeline(questionTimelines)
                .build();
    }

    private Script convertDtoToScript(InterviewRoom interviewRoom, ResultInterviewScriptDto resultInterviewScriptDto) {
        return Script.builder()
                .interviewRoom(interviewRoom)
                .questionIdx(resultInterviewScriptDto.getQuestionIdx())
                .script(resultInterviewScriptDto.getScript())
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

        Result result = resultRepository.findByInterviewRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_RESULT));
        List<String> eyeTimeline = Arrays.asList(result.getEyeTimeline().split(","));
        List<String> attitudeTimeline = Arrays.asList(result.getAttitudeTimeline().split(","));
        List<String> questionTimeline = Arrays.asList(result.getQuestionTimeline().split(","));
        List<ResultResponseScriptDto> scripts = new ArrayList<>();

        for (Script script : scriptRepository.findAllByInterviewRoomIdx(roomIdx)) {
            String questionTitle = questionRepository.findByIdx(script.getQuestionIdx())
                    .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION))
                    .getQuestionTitle();
            scripts.add(convertScriptToDto(script, questionTitle));
        }

        return new ResultAiResponseDto(result, eyeTimeline, attitudeTimeline, questionTimeline, scripts, interviewRoom);
    }

    public ResultUserResponseDto getUserResult(Long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        if (!interviewRoom.getRoomType().equals(RoomType.USER)) {
            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
        }
        Result result = resultRepository.findByInterviewRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_RESULT));
        List<String> eyeTimeline = Arrays.asList(result.getEyeTimeline().split(","));
        List<String> attitudeTimeline = Arrays.asList(result.getAttitudeTimeline().split(","));
        List<String> questionTimeline = Arrays.asList(result.getQuestionTimeline().split(","));
        List<ResultResponseCommentDto> comments = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByInterviewRoomIdx(roomIdx)) {
            comments.add(convertCommentToDto(comment));
        }
        return new ResultUserResponseDto(result, eyeTimeline, attitudeTimeline, questionTimeline, comments, interviewRoom);
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
        Long memberIdx = jwtTokenProvider.getUserInfo();

        Member viewer = memberRepository.findByIdx(memberIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));

        Result result = resultRepository.findByInterviewRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_RESULT));

        validator.validateContents(resultMemoDto.getMemo());

        result.setMemo(resultMemoDto.getMemo());

        resultRepository.save(result);
    }
}
