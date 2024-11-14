package dev.cass.knorda.domain.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	Optional<Member> findFirstByMemberId(int memberId);

	Optional<Member> findFirstByMemberName(String memberName);

	Optional<Member> findFirstByMemberNameAndIsDeleted(String memberName, boolean isDeleted);
}
