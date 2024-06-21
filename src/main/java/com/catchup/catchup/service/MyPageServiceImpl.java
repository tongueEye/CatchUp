package com.catchup.catchup.service;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.MyPageDTO;
import com.catchup.catchup.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

    private final MyPageRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public MyPageDTO getProfile() {
        Optional<User> result = repository.findById(101L);
        System.out.println("qwerty >>> " + result);

        // 출력하려면 entity -> dto 변환하기
        modelMapper.map(User.class, MyPageDTO.class);

        MyPageDTO list = result.stream().map(item -> modelMapper.map(item, MyPageDTO.class))
                .findAny()
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });
        return list;
    }
}
