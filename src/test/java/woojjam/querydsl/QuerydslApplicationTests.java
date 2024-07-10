package woojjam.querydsl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

	@Autowired
	EntityManager entityManager;
	
	@Test
	void contextLoads() {
		Hello hello = new Hello();
		entityManager.persist(hello);

		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
		QHello qHello = QHello.hello;

		Hello result = jpaQueryFactory.selectFrom(qHello)
			.fetchOne();

		Assertions.assertThat(result).isEqualTo(hello);
		Assertions.assertThat(result.getId()).isEqualTo(hello.getId());
	}

}
