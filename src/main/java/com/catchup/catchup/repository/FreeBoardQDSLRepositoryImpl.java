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
import static com.catchup.catchup.domain.QFreeRepBoard.freeRepBoard;
import static com.catchup.catchup.domain.QUser.user;
import static com.querydsl.core.types.dsl.Wildcard.count;


public class FreeBoardQDSLRepositoryImpl implements FreeBoardQDSLRepository {

    private final JPAQueryFactory queryFactory;

    public FreeBoardQDSLRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /** 게시판 검색/전체 페이지 조회 **/
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

    /** 조회수 카운트 +1 **/
    @Override
    @Transactional
    public void updateCount(Long fid) {
        queryFactory.update(freeBoard)
                .set(freeBoard.count, freeBoard.count.add(1))
                .where(freeBoard.fid.eq(fid))
                .execute();
    }

    @Override
    public List<FreeBoardDTO> mostView() {
        List<FreeBoardDTO> hotList = queryFactory.select(Projections.fields(
                        FreeBoardDTO.class
                        , freeBoard.title
                        , freeBoard.fid
                ))
                .from(freeBoard)
                .orderBy(freeBoard.count.desc())
                .offset(0)
                .limit(5)
                .fetch();
        return hotList;
    }

//    /** 게시글 좋아요 카운트 +1 **/
//    @Override
//    public void addLove(Long fid) {
//        queryFactory.update(freeBoard)
//                .set(freeBoard.count, freeBoard.count.add(1))
//                .where(freeBoard.fid.eq(fid))
//                .execute();
//    }
//    /** 게시글 좋아요 카운트 -1 **/
//    @Override
//    public void delLove(Long fid) {
//        queryFactory.update(freeBoard)
//                .set(freeBoard.count, freeBoard.count.subtract(1))
//                .where(freeBoard.fid.eq(fid))
//                .execute();
//    }

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
