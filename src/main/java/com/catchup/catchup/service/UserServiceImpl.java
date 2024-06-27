package com.catchup.catchup.service;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.SearchCondition;
import com.catchup.catchup.dto.FreeBoardDTO;
import com.catchup.catchup.dto.UserDTO;
import com.catchup.catchup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public UserDTO loginCheck(String id) {
        User user = userRepository.loginCheck(id);
        UserDTO dto = new UserDTO();

        if(user == null || user.getId().equals("")){
            return dto;
        }

        dto = modelMapper.map(user, UserDTO.class);

        return dto;
    }

    @Override
    public Long idCheck(String id) {
        Long cnt = userRepository.idCheck(id);

        return cnt;
    }


    @Override
    public Page<UserDTO> userlist(String search, String searchtxt, Pageable pageable) {
        SearchCondition condition = new SearchCondition();
        condition.setId(null);
        condition.setName(null);

        if("Id".equals(search) && search!=null && !"".equals(search)){
            condition.setId(searchtxt);
        }else if("Name".equals(search) && search!=null && !"".equals(search)){
            condition.setName(searchtxt);
        }

         Page<UserDTO> userlist = userRepository.search(condition, pageable);

        return userlist;
    }

    @Override
    public Long delUser(Long uid) {
        userRepository.deleteById(uid);
        return uid;
    }

}
