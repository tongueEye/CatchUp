package com.catchup.catchup.repository;

import com.catchup.catchup.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyPageRepository extends JpaRepository<User, Long>, MyPageQDSLRepository {

    //되는 코드
//    @Query("select u.profile, u.nickname from User u where u.uid = 101L")
//    List<Object[]> getProfile();


}
