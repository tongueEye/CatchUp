package com.catchup.catchup.repository;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.catchup.catchup.domain.QFreeRepBoard.freeRepBoard;
import static com.catchup.catchup.domain.QFreeBoard.freeBoard;
import static com.catchup.catchup.domain.QUser.user;


@Repository
@RequiredArgsConstructor
@Slf4j
public class FreeRepBoardQDSLRepositoryImpl implements FreeRepBoardQDSLRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RepBoardDTO> mypageList(Pageable pageable, Long id) {
        /** 마이페이지 내가 쓴 게시글 조회하기 */
        List<RepBoardDTO> list = queryFactory.select(Projections.fields(
                        RepBoardDTO.class
                        , freeRepBoard.frcontent
                        , freeRepBoard.frUpdateDate
                        , freeBoard.title
                        , freeBoard.cate
                        , freeBoard.fid
                )).from(freeRepBoard)
                .join(freeRepBoard.user, user)
                .join(freeRepBoard.freeBoard, freeBoard)
                .where(user.uid.eq(id))
                .fetch();

        Long totalCount = queryFactory.select(freeRepBoard.count())
                .from(freeRepBoard)
                .where(user.uid.eq(id))
                .fetchOne();

        return new PageImpl<>(list, pageable, totalCount);
    }
}
