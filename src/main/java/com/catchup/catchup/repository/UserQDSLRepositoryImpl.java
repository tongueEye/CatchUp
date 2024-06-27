package com.catchup.catchup.repository;

import com.catchup.catchup.domain.QUser;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.dto.UserDTO;
import com.querydsl.core.BooleanBuilder;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

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


    @Override
    public Page<UserDTO> search(SearchCondition condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(condition.getId()) && !"null".equals(condition.getId())){
            builder.and(user.id.contains(condition.getId()));
        } else if (StringUtils.hasText(condition.getName()) && !"null".equals(condition.getName())){
            builder.and(user.name.contains(condition.getName()));
        }

        List<UserDTO> list = queryFactory.select(Projections.fields(
                        UserDTO.class
                        , user.uid
                        , user.id
                        , user.password
                        , user.name
                        , user.nickname
                        , user.addr
                        , user.phone
                        , user.profile
                        , user.sdschulCode
                        , user.sidoCode
                        , user.schoolName))
                .from(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalcount = queryFactory.select(user.count())
                .from(user)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(list, pageable, totalcount);
    }

}
