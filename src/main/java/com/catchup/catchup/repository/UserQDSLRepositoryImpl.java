package com.catchup.catchup.repository;

import com.catchup.catchup.domain.QUser;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.catchup.catchup.domain.QUser.user;
import static com.catchup.catchup.domain.QFreeBoard.freeBoard;

@RequiredArgsConstructor
public class UserQDSLRepositoryImpl implements UserQDSLRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public User loginCheck(String id) {
        User user = queryFactory.select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.id.eq(id))
                .fetchOne();

        return user;
    }

    @Override
    public Long idCheck(String id) {
        Long idck = queryFactory.select(user.id.count())
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();

        return idck;
    }


    /**
     * Uid 가지고 오기
     */
    @Override
    public Long getuid(String id) {
        Long uid = queryFactory.select(user.uid)
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();

        return uid;
    }

}
