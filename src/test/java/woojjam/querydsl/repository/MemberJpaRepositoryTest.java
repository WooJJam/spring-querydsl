package woojjam.querydsl.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import woojjam.querydsl.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired MemberJpaRepository memberJpaRepository;

	@Test
	public void basicTest() throws Exception{
		Member member = new Member("member1", 10);
		memberJpaRepository.save(member);
		Member findMember = memberJpaRepository.findById(member.getId()).get();
		assertThat(findMember).isEqualTo(member);

		List<Member> result = memberJpaRepository.findAll();
		assertThat(result).containsExactly(member);

		List<Member> result2 = memberJpaRepository.findByUsername("member1");
		assertThat(result2).containsExactly(member);

	}

	@Test
	public void basicTest_Querydsl() throws Exception{
		Member member = new Member("member1", 10);
		memberJpaRepository.save(member);

		List<Member> result = memberJpaRepository.findAll_Querydsl();
		assertThat(result).containsExactly(member);

		List<Member> result2 = memberJpaRepository.findByUsername_Querydsl("member1");
		assertThat(result2).containsExactly(member);
	}

}
