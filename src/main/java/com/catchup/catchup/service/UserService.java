package com.catchup.catchup.service;

import com.catchup.catchup.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Long save(UserDTO dto);

    UserDTO findUserById(long uid);

    UserDTO loginCheck(String id);

    Long idCheck(String id);

    Page<UserDTO> userlist(String search, String searchtxt, Pageable pageable);

    Long delUser(Long uid);
}
