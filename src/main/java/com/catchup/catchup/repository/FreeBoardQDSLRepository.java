package com.catchup.catchup.repository;

import com.catchup.catchup.domain.Love;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.RepBoardDTO;
import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardQDSLRepository {
    Page<FreeBoardDTO> search(SearchCondition condition, Pageable pageable);
    List<FreeBoardDTO> detail(Long fid);


    void addLove(Long fid);

    void delLove(Long fid);

//    List<FreeBoardDTO> repList(Long fid);
    Page<FreeBoardDTO> list(Long id, Pageable pageable); // 채원

}
