package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByInterviewRoomIdx(Long roomIdx);

    List<Comment> findAllByInterviewRoomIdxAndViewerIdx(Long roomIdx, Long viewerIdx);
}
