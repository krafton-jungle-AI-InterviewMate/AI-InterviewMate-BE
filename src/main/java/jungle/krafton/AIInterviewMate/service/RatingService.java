package jungle.krafton.AIInterviewMate.service;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import jungle.krafton.AIInterviewMate.dto.rating.RatingHistoryDto;
import jungle.krafton.AIInterviewMate.repository.InterviewRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {
    private final long MEMBER_IDX = 1;

    private final InterviewRoomRepository interviewRoomRepository;

    @Autowired
    public RatingService(InterviewRoomRepository interviewRoomRepository) {
        this.interviewRoomRepository = interviewRoomRepository;
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
}
