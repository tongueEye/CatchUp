package com.catchup.catchup.repository;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyPageQDSLRepository {

    void schoolInsert(UserDTO dto, Long uid);
}
