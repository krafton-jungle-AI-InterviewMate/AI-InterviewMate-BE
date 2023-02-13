package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByIdx(Long memberIdx);

    Optional<Member> findByEmail(String email);
}
