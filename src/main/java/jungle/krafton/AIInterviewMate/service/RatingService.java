//package jungle.krafton.AIInterviewMate.service;
//
//import jungle.krafton.AIInterviewMate.domain.*;
//import jungle.krafton.AIInterviewMate.dto.rating.*;
//import jungle.krafton.AIInterviewMate.exception.PrivateException;
//import jungle.krafton.AIInterviewMate.exception.StatusCode;
//import jungle.krafton.AIInterviewMate.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RatingService {
//    private final long MEMBER_IDX = 1;
//
//    private final InterviewRoomRepository interviewRoomRepository;
//    private final VieweeRatingRepository vieweeRatingRepository;
//    private final CommentRepository commentRepository;
//    private final ScriptRepository scriptRepository;
//
//    private final QuestionRepository questionRepository;
//    private final MemberRepository memberRepository;
//
//    @Autowired
//    public RatingService(InterviewRoomRepository interviewRoomRepository, VieweeRatingRepository vieweeRatingRepository, CommentRepository commentRepository, ScriptRepository scriptRepository, QuestionRepository questionRepository, MemberRepository memberRepository) {
//
//        this.interviewRoomRepository = interviewRoomRepository;
//        this.vieweeRatingRepository = vieweeRatingRepository;
//        this.commentRepository = commentRepository;
//        this.scriptRepository = scriptRepository;
//        this.questionRepository = questionRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    public List<RatingHistoryDto> getRatingHistory() {
//        List<InterviewRoom> interviewRooms = interviewRoomRepository.findAllByMemberIdxOrderByCreatedAtDesc(MEMBER_IDX);
//
//        List<RatingHistoryDto> ratingHistoryDtos = new ArrayList<>();
//
//        for (InterviewRoom interviewRoom : interviewRooms) {
//            if (interviewRoom.getRoomStatus() == RoomStatus.EXIT) {
//                ratingHistoryDtos.add(new RatingHistoryDto(interviewRoom));
//            }
//        }
//
//        return ratingHistoryDtos;
//    }
//
//    public void saveRating(Long roomIdx, RatingInterviewDto ratingInterviewDto) { // TODO: 코드 수정 필요 ( 중복 데이터 삭제 로직 변경 )
//        InterviewRoom interviewRoom = interviewRoomRepository.findById(roomIdx)
//                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));
//
//        VieweeRating vieweeRating = vieweeRatingRepository.findByRoomIdxAndViewerIdx(roomIdx, ratingInterviewDto.getViewerIdx());
//        if (vieweeRating == null) {
//            vieweeRatingRepository.save(convertVieweeRating(roomIdx, ratingInterviewDto));
//        } else {
//            vieweeRatingRepository.delete(vieweeRating);
//            vieweeRatingRepository.save(convertVieweeRating(roomIdx, ratingInterviewDto));
//        }
//
//        if (interviewRoom.getRoomType().equals(RoomType.USER)) {
//            for (CommentsRequestDto commentsRequestDto : ratingInterviewDto.getCommentsRequestDtos()) {
//                if (commentsRequestDto.getQuestionTitle().trim().length() == 0
//                        || commentsRequestDto.getComment().trim().length() == 0) {
//                    continue;
//                }
//                commentRepository.save(convertComment(interviewRoom, commentsRequestDto));
//            }
//        } else {
//            scriptRepository.deleteAllByInterviewRoom_Idx(roomIdx);
//            for (ScriptSaveDto scriptSaveDto : ratingInterviewDto.getScriptRequestsDtos()) {
//                scriptRepository.save(convertScript(scriptSaveDto, interviewRoom));
//            }
//        }
//    }
//
//    private Script convertScript(ScriptSaveDto scriptSaveDto, InterviewRoom interviewRoom) {
//        return Script.builder()
//                .interviewRoom(interviewRoom)
//                .memberIdx(interviewRoom.getMember().getIdx())
//                .questionIdx(scriptSaveDto.getQuestionIdx())
//                .script(scriptSaveDto.getScript())
//                .build();
//    }
//
//
//    private VieweeRating convertVieweeRating(Long roomIdx, RatingInterviewDto ratingInterviewDto) {
//        return VieweeRating.builder()
//                .viewerIdx(ratingInterviewDto.getViewerIdx())
//                .vieweeIdx(1L) // TODO: Login Data 가 없어서 현재는 임시방편으로 처리함
//                .roomType(ratingInterviewDto.getViewerIdx() == 79797979 ? RoomType.AI : RoomType.USER)
//                .answerRating(ratingInterviewDto.getAnswerRating())
//                .eyesRating(ratingInterviewDto.getEyesRating())
//                .attitudeRating(ratingInterviewDto.getAttitudeRating())
//                .roomIdx(roomIdx)
//                .build();
//    }
//
//    private Comment convertComment(InterviewRoom interviewRoom, CommentsRequestDto commentsRequestDto) {
//        return Comment.builder()
//                .interviewRoom(interviewRoom)
//                .vieweeIdx(interviewRoom.getMember().getIdx())
//                .viewerIdx(commentsRequestDto.getViewerIdx())
//                .questionTitle(commentsRequestDto.getQuestionTitle())
//                .comment(commentsRequestDto.getComment())
//                .build();
//    }
//
//    public RatingUserResponseDto getUserRatingList(Long roomIdx) {
//        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
//                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));
//
//        if (!interviewRoom.getRoomType().equals(RoomType.USER)) {
//            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
//        }
//
//        List<RatingUserListDto> ratingList = new ArrayList<>();
//        List<VieweeRating> vieweeRatingList = vieweeRatingRepository.findAllByRoomIdx(roomIdx);
//        for (VieweeRating vieweeRating : vieweeRatingList) {
//            Long viewerIdx = vieweeRating.getViewerIdx();
//            String nickname = "BOT";
//            if (viewerIdx != 79797979) {
//                Member member = memberRepository.findByIdx(viewerIdx).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_MEMBER));
//                nickname = member.getNickname();
//            }
//            List<RatingUserCommentDto> commentList = new ArrayList<>();
//            List<Comment> comments = commentRepository.findAllByInterviewRoomIdxAndViewerIdx(roomIdx, viewerIdx);
//            for (Comment comment : comments) {
//                commentList.add(new RatingUserCommentDto(comment));
//            }
//            ratingList.add(new RatingUserListDto(nickname, vieweeRating, commentList));
//        }
//
//        return new RatingUserResponseDto(interviewRoom, ratingList);
//    }
//
//    public RatingAiResponseDto getAiRatingList(Long roomIdx) { // TODO: 코드 수정 필요 ( 채점 기능 수정 or 삭제 )
//        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx)
//                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));
//
//        if (!interviewRoom.getRoomType().equals(RoomType.AI)) {
//            throw new PrivateException(StatusCode.NOT_MATCH_QUERY_STRING);
//        }
//
//        List<RatingAiScriptListDto> scriptList = new ArrayList<>();
//        List<Script> scripts = scriptRepository.findAllByInterviewRoomIdx(roomIdx);
//
//        for (Script script : scripts) {
//            String pureScript = script.getScript();
//            Long questionIdx = script.getQuestionIdx();
//            Question question = questionRepository.findByIdx(questionIdx).orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_QUESTION));
//            List<String> keywords = new ArrayList<>();
//
//            keywords.add(question.getKeyword1());
//            keywords.add(question.getKeyword2());
//            keywords.add(question.getKeyword3());
//            keywords.add(question.getKeyword4());
//            keywords.add(question.getKeyword5());
//
//            ScriptRating converter = convertScript(pureScript, keywords);
//
//            String newScript = converter.getScript();
//            int score = converter.getScore();
//
//            Script updateQuery = scriptRepository.findByInterviewRoomIdxAndQuestionIdx(roomIdx, questionIdx);
//
//            updateQuery.setRating(score);
//            updateQuery.setScript(pureScript);
//            scriptRepository.save(updateQuery);
//
//            scriptList.add(new RatingAiScriptListDto(question, newScript, score));
//        }
//
//        VieweeRating vieweeRating = vieweeRatingRepository.findByRoomIdx(roomIdx)
//                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_VIEWEE_RATING));
//
//        return new RatingAiResponseDto(vieweeRating, interviewRoom, scriptList);
//    }
//
//    private String convertScript(String script, List<String> keywords) {
//        int matchCnt = 1;
//        for (String keyword : keywords) {
//            if (keyword == null) {
//                continue;
//            }
//            script = script.replace(keyword, "<" + matchCnt + ">" + keyword + "</" + matchCnt + ">");
//            matchCnt++;
//        }
//        return script;
//    }
//}
