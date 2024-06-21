package com.catchup.catchup.repository;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.service.FreeBoardService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.catchup.catchup.domain.QFreeBoard.freeBoard;
import static com.catchup.catchup.domain.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FreeBoardlTest {

    @Autowired
    private FreeBoardService service;
    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    public void countTest(){
        Integer cnt = queryFactory.select(freeBoard.cnt)
                .from(freeBoard)
                .where(freeBoard.fid.eq(10L))
                .fetchOne();
        System.out.println(cnt);
    }

    @Test
    public void detailTest(){
        List<FreeBoardDTO> detail = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.fid
                        , freeBoard.title
                        , freeBoard.writer
                        , freeBoard.content
                        , freeBoard.cate
                        , user.profile
                ))
                .from(freeBoard)
                .join(freeBoard.user, user)
                .where(freeBoard.fid.eq(8L))
                .fetch();
        for(FreeBoardDTO dto:detail){
            System.out.println(dto.getProfile());
        }
    }

}