package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.domain.Love;
import com.catchup.catchup.dto.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.catchup.catchup.domain.QFreeBoard.freeBoard;
import static com.catchup.catchup.domain.QLove.love;
import static com.catchup.catchup.domain.QUser.user;
import static com.querydsl.core.types.dsl.Wildcard.count;


public class FreeBoardQDSLRepositoryImpl implements FreeBoardQDSLRepository {

    private final JPAQueryFactory queryFactory;

    public FreeBoardQDSLRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 게시판 검색/전체 페이지 조회
     **/
    @Override
    public Page<FreeBoardDTO> search(SearchCondition condition, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (condition.getTitle() != null) {
            builder.and(freeBoard.title.contains(condition.getTitle()));
        } else if (condition.getContent() != null) {
            builder.and(freeBoard.content.contains(condition.getContent()));
        } else if (condition.getWriter() != null) {
            builder.and(freeBoard.writer.contains(condition.getWriter()));
        } else if (condition.getCate() != null) {
            builder.and(freeBoard.cate.contains(condition.getCate()));
        }

        List<FreeBoardDTO> list = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.fid
                        , freeBoard.title
                        , freeBoard.cate
                        , freeBoard.writer
                        , freeBoard.content
                        , freeBoard.createDate
                        , freeBoard.updateDate
                        , freeBoard.count
                        , freeBoard.link
                        , freeBoard.kind
                ))
                .from(freeBoard)
                .where(builder.and(freeBoard.kind.eq(condition.getKind())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(freeBoard.count())
                .from(freeBoard)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount);
    }

    /** 게시글 세부 **/
    @Override
    public FreeBoardDTO freeDetail(Long fid) {
        FreeBoardDTO freeBoardDTO = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.user.uid
                        , freeBoard.title
                        , freeBoard.writer
                        , freeBoard.content
                        , freeBoard.fid
                        , freeBoard.kind

                )).from(freeBoard)
                .where(freeBoard.fid.eq(fid))
                .fetchOne();
        return freeBoardDTO;
    }


    /**
     * 조회수 카운트 +1
     **/
    @Override
    @Transactional
    public void updateCount(Long fid) {
        queryFactory.update(freeBoard)
                .set(freeBoard.count, freeBoard.count.add(1))
                .where(freeBoard.fid.eq(fid))
                .execute();
    }

    /**
     * 조회수 1-5
     **/
    @Override
    public List<FreeBoardDTO> mostView() {
        List<FreeBoardDTO> hotList = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.title
                        , freeBoard.fid
                ))
                .from(freeBoard)
                .where(freeBoard.kind.eq("e"))
                .orderBy(freeBoard.count.desc())
                .offset(0)
                .limit(5)
                .fetch();
        return hotList;
    }

    /**
     * 좋아요 1-5
     **/
    @Override
    public List<FreeBoardDTO> mostLike() {
        List<FreeBoardDTO> mostLike = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , love.freeBoard.fid
                        , love.freeBoard.title
                )).from(freeBoard)
                .innerJoin(freeBoard.loveList, love)
                .where(freeBoard.kind.eq("e"))
                .groupBy(freeBoard.fid)
                .orderBy(love.lid.count().desc())
                .offset(0)
                .limit(5)
                .fetch();
        return mostLike;
    }

    /**
     * 작성자 정보 가져오기
     **/
    @Override
    public List<FreeBoardDTO> getWriterInfo(Long fid) {
        List<FreeBoardDTO> list = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , user.nickname
                        , user.profile
                        , user.uid
                        , freeBoard.title
                        , freeBoard.fid
                ))
                .from(user)
                .join(user.boardList, freeBoard)
                .where(freeBoard.fid.eq(fid))
                .fetch();
        return list;
    }

    /**
     * 조회수 1-5
     **/
    @Override
    public List<FreeBoardDTO> mostViewC() {
        List<FreeBoardDTO> hotList = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.title
                        , freeBoard.fid
                ))
                .from(freeBoard)
                .where(freeBoard.kind.eq("c"))
                .orderBy(freeBoard.count.desc())
                .offset(0)
                .limit(5)
                .fetch();
        return hotList;
    }

    /**
     * 좋아요 1-5
     **/
    @Override
    public List<FreeBoardDTO> mostLikeC() {
        List<FreeBoardDTO> mostLike = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , love.freeBoard.fid
                        , love.freeBoard.title
                )).from(freeBoard)
                .innerJoin(freeBoard.loveList, love)
                .where(freeBoard.kind.eq("c"))
                .groupBy(freeBoard.fid)
                .orderBy(love.lid.count().desc())
                .offset(0)
                .limit(5)
                .fetch();
        return mostLike;
    }

    @Override // 채원
    public Page<FreeBoardDTO> list(Long id, Pageable pageable) {
        /** 마이페이지 내가 쓴 게시글 조회하기 */
        List<FreeBoardDTO> list = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.title
                        , freeBoard.content
                        , freeBoard.cate
                        , freeBoard.kind
                        , freeBoard.updateDate
                )).from(freeBoard)
                .join(freeBoard.user, user)
                .where(user.uid.eq(id))
                .fetch();

        Long totalCount = queryFactory.select(freeBoard.count())
                .from(freeBoard)
                .where(user.uid.eq(id))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount);
    }


}
