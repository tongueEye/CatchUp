package com.catchup.catchup.service;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

/*    @Override
    public boolean loginCkeck(String id, String password) {
        boolean login = userRepository.findById(id);

        return login;
    }*/
}
