package com.catchup.catchup.repository;

import com.catchup.catchup.dto.RepBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreeRepBoardQDSLRepository {
    Page<RepBoardDTO> mypageList(Pageable pageable, Long uid);
}
