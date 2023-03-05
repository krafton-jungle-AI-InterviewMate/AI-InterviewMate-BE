package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import jungle.krafton.AIInterviewMate.domain.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
    List<InterviewRoom> findAllByMemberIdxOrderByCreatedAtDesc(Long memberIdx);

    Optional<InterviewRoom> findByIdx(Long roomIdx);

    //Sort by [타입 - 상태 - 시간]
    List<InterviewRoom> findAllByRoomStatusNotOrderByRoomTypeDescRoomStatusAscCreatedAtDesc(RoomStatus status1);

    //Page
    Page<InterviewRoom> findAllByMemberIdx(Long memberIdx, Pageable pageable);
}
