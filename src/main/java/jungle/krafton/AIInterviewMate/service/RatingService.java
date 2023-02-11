package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.Comment;
import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.domain.VieweeRating;
import jungle.krafton.AIInterviewMate.dto.rating.CommentsRequestDto;
import jungle.krafton.AIInterviewMate.dto.rating.RatingHistoryDto;
import jungle.krafton.AIInterviewMate.dto.rating.RatingInterviewDto;
import jungle.krafton.AIInterviewMate.exception.PrivateException;
import jungle.krafton.AIInterviewMate.exception.StatusCode;
import jungle.krafton.AIInterviewMate.repository.CommentRepository;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import jungle.krafton.AIInterviewMate.repository.VieweeRatingRepository;
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

    @Autowired
    public RatingService(InterviewRoomRepository interviewRoomRepository, VieweeRatingRepository vieweeRatingRepository, CommentRepository commentRepository) {
        this.interviewRoomRepository = interviewRoomRepository;
        this.vieweeRatingRepository = vieweeRatingRepository;
        this.commentRepository = commentRepository;
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
        VieweeRating vieweeRating = new VieweeRating(ratingInterviewDto);
        vieweeRatingRepository.save(vieweeRating);

        InterviewRoom interviewRoom = interviewRoomRepository.findById((long) roomIdx)
                .orElseThrow(() -> new PrivateException(StatusCode.NOT_FOUND_ROOM));

        for (CommentsRequestDto commentsRequestDto : ratingInterviewDto.getCommentsRequestDtos()) {
            if (commentsRequestDto.getQuestionTitle().trim().length() == 0
                    || commentsRequestDto.getComment().trim().length() == 0) {
                continue;
            }
            Comment comment = new Comment(interviewRoom, commentsRequestDto);

            commentRepository.save(comment);
        }
    }
}
