package com.catchup.catchup.service;

import com.catchup.catchup.dto.UserDTO;

public interface UserService {

    Long save(UserDTO dto);

    //boolean loginCkeck(String id, String password);
}
