package com.catchup.catchup.repository;

import com.amazonaws.services.kms.model.NotFoundException;
import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.domain.Love;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.LoveDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.service.FreeBoardService;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.catchup.catchup.domain.QFreeBoard.freeBoard;
import static com.catchup.catchup.domain.QFreeRepBoard.freeRepBoard;
import static com.catchup.catchup.domain.QLove.love;
import static com.catchup.catchup.domain.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FreeBoardlTest {

    @Autowired
    private FreeBoardService service;
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private FreeBoardRepository freeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoveRepository loveRepository;

    @Test
    public void countTest(){
        Integer cnt = queryFactory.select(freeBoard.count)
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

    @Test
    public void replyListTest(){
        List<FreeBoardDTO> repList = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                , freeBoard.user.profile
                , freeBoard.user.nickname
                        , freeRepBoard.frcontent
                        , freeRepBoard.frCreateDate
                        , freeRepBoard.frUpdateDate
                        ))
                .from(freeBoard)
                .join(freeBoard.repList, freeRepBoard)
                .join(freeBoard.user, user)
                .where(freeBoard.fid.eq(8L))
                .fetch();

        for(FreeBoardDTO dto : repList){
            System.out.println(dto.getProfile());
            System.out.println(dto.getNickname());
            System.out.println(dto.getFrcontent());
            System.out.println(dto.getFrCreateDate());
            System.out.println(dto.getFrUpdateDate());
        }

//        for(Tuple t : repList){
//            System.out.println(t.get(0, Tuple.class));
//            System.out.println(t.get(1, Tuple.class));
//            System.out.println(t.get(2, Tuple.class));
//            System.out.println(t.get(3, Tuple.class));
//            System.out.println(t.get(4, Tuple.class));
//        }

    }

    @Test
    @Transactional
    public void replyDeleteTest(){
        service.repDelete(21L);

    }

    @Test
    public void mostLikeTest(){
        List<FreeBoardDTO> mostLike = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , love.freeBoard.fid
                        , love.freeBoard.title
                )).from(freeBoard)
                .innerJoin(freeBoard.loveList, love)
                .groupBy(freeBoard.fid)
                .orderBy(love.lid.count().desc())
                .offset(0)
                .limit(5)
                .fetch();
        for(FreeBoardDTO dto : mostLike){
            System.out.println(dto.getFid());
        }
    }


    @Test
    public void CheckTest(){
        LoveDTO dto = LoveDTO.builder()
                .fid(1L)
                .uid(100L)
                .build();

        boolean b = service.loveCheck(dto);
        assertThat(b).isEqualTo(false);
    }
}