package com.catchup.catchup.repository;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
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

//    @Override
//    public List<FreeBoardDTO> repList(Long fid) {
//        List<FreeBoardDTO> repList = queryFactory.select(Projections.fields(
//                        FreeBoardDTO.class
//                        , freeBoard.user.profile
//                        , freeBoard.user.nickname
//                        , freeRepBoard.frid
//                        , freeRepBoard.user.uid
//                        , freeRepBoard.frcontent
//                        , freeRepBoard.frCreateDate
//                        , freeRepBoard.frUpdateDate
//                ))
//                .from(freeBoard)
//                .join(freeBoard.repList, freeRepBoard)
//                .join(freeBoard.user, user)
//                .where(freeBoard.fid.eq(fid))
//                .fetch();
//        return repList;
//    }
}
