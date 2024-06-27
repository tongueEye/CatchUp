package com.catchup.catchup.repository;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.catchup.catchup.dto.FreeBoardDTO;

import java.util.List;


public interface UserQDSLRepository {


    User loginCheck(String id);

    Long idCheck(String id);

    Long getuid(String id);

    Page<UserDTO> search(SearchCondition condition, Pageable pageable);

}
