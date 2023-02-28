package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.result.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public ResultService(InterviewRoomRepository interviewRoomRepository, ResultRepository resultRepository, CommentRepository commentRepository, ScriptRepository scriptRepository, QuestionRepository questionRepository, MemberRepository memberRepository) {

        this.interviewRoomRepository = interviewRoomRepository;
        this.commentRepository = commentRepository;
        this.scriptRepository = scriptRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
        this.memberRepository = memberRepository;
    }

    public List<RatingHistoryDto> getRatingHistory() {
        List<InterviewRoom> interviewRooms = interviewRoomRepository.findAllByMemberIdxOrderByCreatedAtDesc(MEMBER_IDX);

        List<RatingHistoryDto> ratingHistoryDtos = new ArrayList<>();

        for (InterviewRoom interviewRoom : interviewRooms) {
            if (interviewRoom.getRoomStatus() == RoomStatus.EXIT) {
                ratingHistoryDtos.add(new RatingHistoryDto(interviewRoom));
            }
        }

        return ratingHistoryDtos;
    }

    public void saveResult(Long roomIdx, ResultInterviewDto resultInterviewDto) { // TODO: 코드 수정 필요 ( 중복 데이터 삭제 로직 변경 )
        InterviewRoom interviewRoom = interviewRoomRepository.findById(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));
        StringBuffer eyeTimelines = new StringBuffer();
        StringBuffer attitudeTimelines = new StringBuffer();
        StringBuffer questionTimelines = new StringBuffer();
        if (resultInterviewDto.getEyeTimelines() != null) {
            for (String eyeTimeline : resultInterviewDto.getEyeTimelines()) {
                eyeTimelines.append(",").append(eyeTimeline);
            }
            eyeTimelines.deleteCharAt(0);
        }
        if (resultInterviewDto.getAttitudeTimelines() != null) {
            for (String attitudeTimeline : resultInterviewDto.getAttitudeTimelines()) {
                attitudeTimelines.append(",").append(attitudeTimeline);
            }
            attitudeTimelines.deleteCharAt(0);
        }
        if (resultInterviewDto.getQuestionTimelines() != null) {
            for (String questionTimeline : resultInterviewDto.getQuestionTimelines()) {
                questionTimelines.append(",").append(questionTimeline);
            }
            questionTimelines.deleteCharAt(0);
        }

        if (interviewRoom.getRoomType().equals(RoomType.USER)) {
            resultRepository.save(convertResult(interviewRoom, resultInterviewDto, eyeTimelines, attitudeTimelines, questionTimelines));
            for (ResultInterviewCommentDto resultInterviewCommentDto : resultInterviewDto.getComments()) {
                commentRepository.save(convertDtotoComment(interviewRoom, resultInterviewCommentDto));
            }
        } else {
            resultRepository.save(convertResult(interviewRoom, resultInterviewDto, eyeTimelines, attitudeTimelines, questionTimelines));
            for (ResultInterviewScriptDto resultInterviewScriptDto : resultInterviewDto.getScripts()) {
                scriptRepository.save(convertDtotoScript(interviewRoom, resultInterviewScriptDto));
            }
        }
    }

    private Result convertResult(InterviewRoom interviewRoom, ResultInterviewDto resultInterviewDto, StringBuffer eyeTimelines, StringBuffer attitudeTimelines, StringBuffer questionTimelines) {
        return Result.builder()
                .interviewRoom(interviewRoom)
                .videoUrl(resultInterviewDto.getVideoUrl())
                .eyeTimeline(eyeTimelines.toString())
                .attitudeTimeline(attitudeTimelines.toString())
                .questionTimeline(questionTimelines.toString())
                .build();
    }

    private Script convertDtotoScript(InterviewRoom interviewRoom, ResultInterviewScriptDto resultInterviewScriptDto) {
        return Script.builder()
                .interviewRoom(interviewRoom)
                .questionIdx(resultInterviewScriptDto.getQuestionIdx())
                .script(resultInterviewScriptDto.getScript())
                .build();
    }

    private Comment convertDtotoComment(InterviewRoom interviewRoom, ResultInterviewCommentDto resultInterviewCommentDto) {
        return Comment.builder()
                .interviewRoom(interviewRoom)
                .viewerIdx(resultInterviewCommentDto.getViewerIdx())
                .comment(resultInterviewCommentDto.getComment())
                .build();
    }

    public ResultAiResponseDto getAiResult(Long roomIdx) { // TODO: 코드 수정 필요 ( 채점 기능 수정 or 삭제 )
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
        List<ResultInterviewScriptDto> scripts = new ArrayList<>();
        for (Script script : scriptRepository.findAllByInterviewRoomIdx(roomIdx)) {
            scripts.add(convertScriptToDto(script));
        }


        return new ResultAiResponseDto(result, eyeTimeline, attitudeTimeline, questionTimeline, scripts);
    }

    public ResultUserResponseDto getUserResult(Long roomIdx) { // TODO: 코드 수정 필요 ( 채점 기능 수정 or 삭제 )
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
        List<ResultInterviewCommentDto> comments = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByInterviewRoomIdx(roomIdx)) {
            comments.add(convertCommentToDto(comment));
        }


        return new ResultUserResponseDto(result, eyeTimeline, attitudeTimeline, questionTimeline, comments);
    }

    public ResultInterviewScriptDto convertScriptToDto(Script script) {
        return ResultInterviewScriptDto.builder()
                .script(script.getScript())
                .questionIdx(script.getQuestionIdx())
                .build();
    }

    public ResultInterviewCommentDto convertCommentToDto(Comment comment) {
        return ResultInterviewCommentDto.builder()
                .comment(comment.getComment())
                .viewerIdx(comment.getViewerIdx())
                .build();
    }

}
