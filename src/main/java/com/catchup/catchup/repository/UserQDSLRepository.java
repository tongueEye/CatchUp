package com.catchup.catchup.repository;

import com.catchup.catchup.domain.User;

public interface UserQDSLRepository {


    User loginCheck(String id);

    Long idCheck(String id);

    Long getuid(String id);
}
