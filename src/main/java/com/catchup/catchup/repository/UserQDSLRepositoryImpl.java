package com.catchup.catchup.repository;

import com.catchup.catchup.domain.QUser;
import com.catchup.catchup.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.catchup.catchup.domain.QUser.user;

@RequiredArgsConstructor
public class UserQDSLRepositoryImpl implements UserQDSLRepository{

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


}
