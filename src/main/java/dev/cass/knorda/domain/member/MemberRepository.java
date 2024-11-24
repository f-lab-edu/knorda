package dev.cass.knorda.domain.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	Optional<Member> findFirstByMemberId(int memberId);

	Optional<Member> findFirstByMemberName(String memberName);

	Optional<Member> findFirstByMemberNameAndIsDeletedFalse(String memberName);

	List<Member> findAllByIsDeletedFalse(Pageable pageable);
}
