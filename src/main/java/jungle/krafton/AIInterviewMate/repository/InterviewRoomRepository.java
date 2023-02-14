package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
    List<InterviewRoom> findAllByMemberIdxOrderByCreatedAtDesc(Long memberIdx);

    InterviewRoom findByIdx(Long roomIdx);

    List<InterviewRoom> findAllByRoomStatusOrRoomStatusOrderByCreatedAtDescRoomStatus(RoomStatus create, RoomStatus proceed);
}
