package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.*;
import jungle.krafton.AIInterviewMate.dto.rating.*;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {
    private final long MEMBER_IDX = 1;

    private final InterviewRoomRepository interviewRoomRepository;
    private final VieweeRatingRepository vieweeRatingRepository;
    private final CommentRepository commentRepository;
    private final ScriptRepository scriptRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public RatingService(InterviewRoomRepository interviewRoomRepository, VieweeRatingRepository vieweeRatingRepository, CommentRepository commentRepository, ScriptRepository scriptRepository, QuestionRepository questionRepository) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.vieweeRatingRepository = vieweeRatingRepository;
        this.commentRepository = commentRepository;
        this.scriptRepository = scriptRepository;
        this.questionRepository = questionRepository;
    }

    public List<RatingHistoryDto> getRatingHistory() {
        List<InterviewRoom> interviewRooms = interviewRoomRepository.findAllByMemberIdx(MEMBER_IDX);

        List<RatingHistoryDto> ratingHistoryDtos = new ArrayList<>();

        for (InterviewRoom interviewRoom : interviewRooms) {
            if (interviewRoom.getRoomStatus() == RoomStatus.EXIT) {
                ratingHistoryDtos.add(new RatingHistoryDto(interviewRoom));
            }
        }

        return ratingHistoryDtos;
    }

    public void saveRating(int roomIdx, RatingInterviewDto ratingInterviewDto) {
        vieweeRatingRepository.save(convertVieweeRating(ratingInterviewDto));

        InterviewRoom interviewRoom = interviewRoomRepository.findById((long) roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        for (CommentsRequestDto commentsRequestDto : ratingInterviewDto.getCommentsRequestDtos()) {
            if (commentsRequestDto.getQuestionTitle().trim().length() == 0
                    || commentsRequestDto.getComment().trim().length() == 0) {
                continue;
            }

            commentRepository.save(convertComment(interviewRoom, commentsRequestDto));
        }
    }

    private VieweeRating convertVieweeRating(RatingInterviewDto ratingInterviewDto) {
        return VieweeRating.builder()
                .viewerIdx(ratingInterviewDto.getViewerIdx())
                .vieweeIdx(1L) //Login Data 가 없어서 현재는 임시방편으로 처리함
                .roomType(ratingInterviewDto.getViewerIdx() == 79797979 ? RoomType.AI : RoomType.USER)
                .answerRating(ratingInterviewDto.getAnswerRating())
                .eyesRating(ratingInterviewDto.getEyesRating())
                .attitudeRating(ratingInterviewDto.getAttitudeRating())
                .build();
    }

    private Comment convertComment(InterviewRoom interviewRoom, CommentsRequestDto commentsRequestDto) {
        return Comment.builder()
                .interviewRoom(interviewRoom)
                .vieweeIdx(interviewRoom.getMember().getIdx())
                .viewerIdx(commentsRequestDto.getViewerIdx())
                .questionTitle(commentsRequestDto.getQuestionTitle())
                .comment(commentsRequestDto.getComment())
                .build();
    }

    public RatingUserResponseDto getUserRatingList(Long roomIdx) {
        return null;
    }

    public RatingAiResponseDto getAiRatingList(Long roomIdx) {
        List<RatingAiScriptListDto> scriptList = new ArrayList<>();
        List<Script> scripts = scriptRepository.findAllByInterviewRoomIdx(roomIdx);
        for (Script script : scripts) {
            String pureScript = script.getScript();
            System.out.println(pureScript);
            Long questionIdx = script.getQuestionIdx();
            Question question = questionRepository.findByIdx(questionIdx);
            List<String> keywords = new ArrayList<>();
            keywords.add(question.getKeyword1());
            keywords.add(question.getKeyword2());
            keywords.add(question.getKeyword3());
            keywords.add(question.getKeyword4());
            keywords.add(question.getKeyword5());
            String newScript = convertScript(pureScript, keywords);

            scriptList.add(new RatingAiScriptListDto(question, newScript));
        }

        VieweeRating vieweeRating = vieweeRatingRepository.findByRoomIdx(roomIdx);
        InterviewRoom interviewRoom = interviewRoomRepository.findByIdx(roomIdx);

        return new RatingAiResponseDto(vieweeRating, interviewRoom, scriptList);
    }

    private String convertScript(String script, List<String> keywords) {
        int cnt = 1;
        for (String keyword : keywords) {
            script = script.replace(keyword, "<" + cnt + ">" + keyword + "<" + cnt + ">");
            cnt++;
        }
        return script;
    }
}
