package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.result.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {
    private final long MEMBER_IDX = 1;

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
                commentRepository.save(convertComment(interviewRoom, resultInterviewCommentDto));
            }
        } else {
            resultRepository.save(convertResult(interviewRoom, resultInterviewDto, eyeTimelines, attitudeTimelines, questionTimelines));
            for (ResultInterviewScriptDto resultInterviewScriptDto : resultInterviewDto.getScripts()) {
                scriptRepository.save(convertScript(interviewRoom, resultInterviewScriptDto));
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

    private Script convertScript(InterviewRoom interviewRoom, ResultInterviewScriptDto resultInterviewScriptDto) {
        return Script.builder()
                .interviewRoom(interviewRoom)
                .questionIdx(resultInterviewScriptDto.getQuestionIdx())
                .script(resultInterviewScriptDto.getScript())
                .build();
    }

    private Comment convertComment(InterviewRoom interviewRoom, ResultInterviewCommentDto resultInterviewCommentDto) {
        return Comment.builder()
                .interviewRoom(interviewRoom)
                .viewerIdx(resultInterviewCommentDto.getViewerIdx())
                .comment(resultInterviewCommentDto.getComment())
                .build();
    }

    public RatingUserResponseDto getUserRatingList(Long roomIdx) {
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        if (!interviewRoom.getRoomType().equals(RoomType.USER)) {
            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
        }

        List<RatingUserListDto> ratingList = new ArrayList<>();
        List<VieweeRating> vieweeRatingList = vieweeRatingRepository.findAllByRoomIdx(roomIdx);
        for (VieweeRating vieweeRating : vieweeRatingList) {
            Long viewerIdx = vieweeRating.getViewerIdx();
            String nickname = "BOT";
            if (viewerIdx != 79797979) {
                Member member = memberRepository.findByIdx(viewerIdx).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));
                nickname = member.getNickname();
            }
            List<RatingUserCommentDto> commentList = new ArrayList<>();
            List<Comment> comments = commentRepository.findAllByInterviewRoomIdxAndViewerIdx(roomIdx, viewerIdx);
            for (Comment comment : comments) {
                commentList.add(new RatingUserCommentDto(comment));
            }
            ratingList.add(new RatingUserListDto(nickname, vieweeRating, commentList));
        }

        return new RatingUserResponseDto(interviewRoom, ratingList);
    }

    public RatingAiResponseDto getAiRatingList(Long roomIdx) { // TODO: 코드 수정 필요 ( 채점 기능 수정 or 삭제 )
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        if (!interviewRoom.getRoomType().equals(RoomType.AI)) {
            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
        }

        List<RatingAiScriptListDto> scriptList = new ArrayList<>();
        List<Script> scripts = scriptRepository.findAllByInterviewRoomIdx(roomIdx);

        for (Script script : scripts) {
            String pureScript = script.getScript();
            Long questionIdx = script.getQuestionIdx();
            Question question = questionRepository.findByIdx(questionIdx).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));
            List<String> keywords = new ArrayList<>();

            keywords.add(question.getKeyword1());
            keywords.add(question.getKeyword2());
            keywords.add(question.getKeyword3());
            keywords.add(question.getKeyword4());
            keywords.add(question.getKeyword5());

            ScriptRating converter = convertScript(pureScript, keywords);

            String newScript = converter.getScript();
            int score = converter.getScore();

            Script updateQuery = scriptRepository.findByInterviewRoomIdxAndQuestionIdx(roomIdx, questionIdx);

            updateQuery.setRating(score);
            updateQuery.setScript(pureScript);
            scriptRepository.save(updateQuery);

            scriptList.add(new RatingAiScriptListDto(question, newScript, score));
        }

        VieweeRating vieweeRating = vieweeRatingRepository.findByRoomIdx(roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_VIEWEE_RATING));

        return new RatingAiResponseDto(vieweeRating, interviewRoom, scriptList);
    }

    private String convertScript(String script, List<String> keywords) {
        int matchCnt = 1;
        for (String keyword : keywords) {
            if (keyword == null) {
                continue;
            }
            script = script.replace(keyword, "<" + matchCnt + ">" + keyword + "</" + matchCnt + ">");
            matchCnt++;
        }
        return script;
    }
}
