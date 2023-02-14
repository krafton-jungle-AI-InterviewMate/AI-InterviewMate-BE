package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScriptRepository extends JpaRepository<Script, Long> {
    List<Script> findAllByInterviewRoomIdx(Long roomIdx);

    Script findByInterviewRoomIdx(Long roomIdx);

    @Transactional
    void deleteAllByInterviewRoom_Idx(Long roomIdx);
}
