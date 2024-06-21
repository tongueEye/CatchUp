package com.catchup.catchup.repository;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.SearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FreeBoardQDSLRepository {
    Page<FreeBoardDTO> search(SearchCondition condition, Pageable pageable);

    List<FreeBoardDTO> detail(Long fid);
}
