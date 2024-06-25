package com.catchup.catchup.repository;

import com.catchup.catchup.dto.InfoBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InfoBoardQDSLRepository {

    Page<InfoBoardDTO> search(SearchCondition condition, Pageable pageable);

    List<InfoBoardDTO> mypageList(Long id);

}
