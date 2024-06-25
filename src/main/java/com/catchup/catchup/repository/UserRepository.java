package com.catchup.catchup.repository;

import com.catchup.catchup.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQDSLRepository {

    @Override
    <S extends User> S save(S entity);

    @Override
    Optional<User> findById(Long aLong);


}
