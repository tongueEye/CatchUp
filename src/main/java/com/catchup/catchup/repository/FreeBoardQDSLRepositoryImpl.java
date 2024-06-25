package com.catchup.catchup.repository;

import com.catchup.catchup.domain.Love;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
                        , freeBoard.cnt
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

    /**
     * 게시판 상세 페이지
     **/
    @Override
    public List<FreeBoardDTO> detail(Long fid) {
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
                .where(freeBoard.fid.eq(fid))
                .fetch();
        return detail;
    }

    @Override
    public void addLove(Long fid) {
        queryFactory.update(freeBoard)
                .set(freeBoard.cnt, freeBoard.cnt.add(1))
                .where(freeBoard.fid.eq(fid))
                .execute();
    }

    @Override
    public void delLove(Long fid) {
        queryFactory.update(freeBoard)
                .set(freeBoard.cnt, freeBoard.cnt.subtract(1))
                .where(freeBoard.fid.eq(fid))
                .execute();
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
