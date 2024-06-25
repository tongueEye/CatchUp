package com.catchup.catchup.repository;

import com.catchup.catchup.domain.InfoBoard;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import static com.catchup.catchup.domain.QInfoBoard.infoBoard;
import static com.catchup.catchup.domain.QUser.user;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InfoBoardQDSLRepositoryImpl implements InfoBoardQDSLRepository{
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<InfoBoardDTO> search(SearchCondition condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(condition.getCate()) && !"null".equals(condition.getCate())){
            builder.and(infoBoard.cate.contains(condition.getCate()));
        } else if (StringUtils.hasText(condition.getTitle()) && !"null".equals(condition.getTitle())){
            builder.and(infoBoard.title.contains(condition.getTitle()));
        } else if (StringUtils.hasText(condition.getContent()) && !"null".equals(condition.getContent())){
            builder.and(infoBoard.content.contains(condition.getContent()));
        }

        List<InfoBoardDTO> qnaList = queryFactory.select(Projections.fields(
                InfoBoardDTO.class
                , infoBoard.iid
                , infoBoard.title
                , infoBoard.content
                , infoBoard.cate
                , infoBoard.writer
                , infoBoard.createDate
                , infoBoard.repContent
                , infoBoard.kind
        )).from(infoBoard)
                .where(builder.and(infoBoard.kind.eq(condition.getKind())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalcount = queryFactory.select(infoBoard.count())
                .from(infoBoard)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(qnaList, pageable, totalcount);

    }

    @Override
    public List<InfoBoardDTO> mypageList(Long id) {
        List<InfoBoardDTO> mypagelist = queryFactory.select(Projections.fields(
                InfoBoardDTO.class
                ,infoBoard.iid
                , infoBoard.title
                , infoBoard.repContent
        )).from(infoBoard)
                .join(infoBoard.user, user)
                .where(user.uid.eq(id))
                .fetch();

        return mypagelist;
    }
}
