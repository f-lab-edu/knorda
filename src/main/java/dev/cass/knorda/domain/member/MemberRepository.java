package dev.cass.knorda.domain.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	Optional<Member> findFirstByUsername(String username);
	Optional<Member> findFirstByUsernameAndIsDeleted(String username, boolean isDeleted);
}
