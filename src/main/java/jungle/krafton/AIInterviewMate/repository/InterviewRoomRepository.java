package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
    List<InterviewRoom> findAllByMemberIdxOrderByCreatedAtDesc(Long memberIdx);

    Optional<InterviewRoom> findByIdx(Long roomIdx);

    List<InterviewRoom> findAllByRoomStatusOrRoomStatusOrderByCreatedAtDescRoomStatus(RoomStatus status1, RoomStatus status2);
}
