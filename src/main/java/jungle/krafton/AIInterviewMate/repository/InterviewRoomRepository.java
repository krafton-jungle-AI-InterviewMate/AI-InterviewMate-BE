package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.InterviewRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRoomRepository extends JpaRepository<InterviewRoom, Long> {
    InterviewRoom findByIdx(Long roomIdx);
}
