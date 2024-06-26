package com.catchup.catchup.repository;

import com.catchup.catchup.domain.FreeBoard;
import com.catchup.catchup.domain.Love;
import com.catchup.catchup.dto.*;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardQDSLRepository {
    Page<FreeBoardDTO> search(SearchCondition condition, Pageable pageable);

    FreeBoardDTO freeDetail(Long fid);

    Long repCount(Long fid);

    Page<FreeBoardDTO> list(Long id, Pageable pageable); // 채원

    void updateCount(Long fid);

    List<FreeBoardDTO> mostView();

    List<FreeBoardDTO> mostLike();

    List<FreeBoardDTO> getWriterInfo(Long fid);

    List<FreeBoardDTO> mostViewC();

    List<FreeBoardDTO> mostLikeC();

}
