package com.catchup.catchup.repository;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.service.FreeBoardService;
import com.catchup.catchup.service.UserService;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class InfoBoardTest {
    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    public void t1(){
        Assertions.assertThat(service).isNotNull();
        Assertions.assertThat(repository).isNotNull();

        Optional<User> user = repository.findById(101L);
        System.out.println("user_test>>>>"+user);

        UserDTO newuser = user.stream().map(item->modelMapper.map(item, UserDTO.class)).findAny().orElseThrow(()->new RuntimeException());

        System.out.println(newuser.getNickname());

    }
}
