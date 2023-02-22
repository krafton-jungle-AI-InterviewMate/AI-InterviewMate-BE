package jungle.krafton.AIInterviewMate.repository;

import jungle.krafton.AIInterviewMate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdx(Long memberIdx);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m.refreshToken FROM Member m WHERE m.idx=:idx")
    String getRefreshTokenById(@Param("idx") Long idx);

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.refreshToken=:token WHERE m.idx=:idx")
    void updateRefreshToken(@Param("idx") Long idx, @Param("token") String token);
}
