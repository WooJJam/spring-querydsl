package woojjam.querydsl;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static woojjam.querydsl.QMember.member;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
//        Member member2 = new Member("member2", 20, teamA);
//
//        Member member3 = new Member("member3", 30, teamB);
//        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
//        em.persist(member2);
//        em.persist(member3);
//        em.persist(member4);
    }

    @Test
    public void startJQPL() throws Exception {

        Member findByJQPL = em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        Assertions.assertThat(findByJQPL.getUsername()).isEqualTo("member1");

    }

    @Test
    public void startQueryDsl() throws Exception {
        QMember m = member;

        Member member = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        Assertions.assertThat(member.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() throws Exception {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void searchAndParam() throws Exception {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();

        Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void resultFetch() throws Exception {
        List<Member> fetchList = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        Long fetchCount = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        List<Long> fetchAll = queryFactory
                .select(Wildcard.count) // count(*)
                .from(member)
                .fetch();

    }

}

