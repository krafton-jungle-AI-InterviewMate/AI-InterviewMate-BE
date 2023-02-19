package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Member;
import jungle.krafton.AIInterviewMate.domain.QuestionBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionBoxRepository extends JpaRepository<QuestionBox, Long> {
    List<QuestionBox> findAllByMember(Member member);

    Optional<QuestionBox> findByIdx(Long Idx);
}
