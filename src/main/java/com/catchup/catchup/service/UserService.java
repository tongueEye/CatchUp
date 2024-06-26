package com.catchup.catchup.service;

import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.UserDTO;

import java.util.List;

public interface UserService {

    Long save(UserDTO dto);

    UserDTO findUserById(long uid);

    UserDTO loginCheck(String id);

    Long idCheck(String id);

}
