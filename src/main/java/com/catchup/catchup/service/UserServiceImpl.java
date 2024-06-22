package com.catchup.catchup.service;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Long save(UserDTO dto) {
        User map = modelMapper.map(dto, User.class);
        User save = userRepository.save(map);

        return save.getUid();
    }

    @Override
    public UserDTO findUserById(long uid) { //uid로 유저 정보 가져오기 (세션 uid로 작성자 불러올 때 사용)
        Optional<User> user = userRepository.findById(uid);

        UserDTO userDTO = user.stream().map(
                item->modelMapper.map(item, UserDTO.class)
        ).findAny().orElseThrow(()->new RuntimeException());

        return userDTO;
    }

/*    @Override
    public boolean loginCkeck(String id, String password) {
        boolean login = userRepository.findById(id);

        return login;
    }*/
}