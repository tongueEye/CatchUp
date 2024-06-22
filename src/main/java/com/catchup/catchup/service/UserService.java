package com.catchup.catchup.service;

import com.catchup.catchup.dto.UserDTO;

public interface UserService {

    Long save(UserDTO dto);

    UserDTO findUserById(long uid);

    //boolean loginCkeck(String id, String password);
}
