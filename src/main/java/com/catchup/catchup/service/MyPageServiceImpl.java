package com.catchup.catchup.service;

import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.*;
import com.catchup.catchup.repository.FreeBoardRepository;
import com.catchup.catchup.repository.InfoBoardRepository;
import com.catchup.catchup.repository.MyPageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

    private final MyPageRepository repository;
    private final FreeBoardRepository freeRepository;
    private final InfoBoardRepository infoRepository;
    private final ModelMapper modelMapper;

    @Override
    public MyPageDTO getProfile() {
        Optional<User> result = repository.findById(101L);

        // 출력하려면 entity -> dto 변환하기
        modelMapper.map(User.class, MyPageDTO.class);

        MyPageDTO list = result.stream().map(item -> modelMapper.map(item, MyPageDTO.class))
                .findAny()
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });
        return list;
    }

    /** 마이페이지 정보 수정 => 기존값 불러오기 */
    @Override
    public UserDTO getInfo() {

        // 세션 정보 받아와야 함...
        Long id = 101L;
        Optional<User> result = repository.findById(id);// update 해 줘야 함 => 받아줘야
        UserDTO list = result.stream().map(item -> modelMapper.map(item, UserDTO.class))
                .findAny()
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });
        return list;
    }

    /** 리얼 트루 정보 수정 */
    @Transactional
    @Override
    public long modifyInfo(UserDTO dto) {
        Long id = 101L; // 세션 받아ㅏㅏㅏㅏㅏ
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setAddr(dto.getAddr());

        repository.save(user);
        return id;
    }


    /** 댓글 리스트 불러오기 */
    @Override
    public MyPageDTO getRepList() {


        return null;
    }

    /** 내가 쓴 글 불러오기 */
    @Override
    public Page<FreeBoardDTO> boardlist(Pageable pageable) {

        Long id = 101L; // session에서 받아올 애 ...
        Page<FreeBoardDTO> list = freeRepository.list(id, pageable);

        return list;
    }

    /** 프로필 수정하기 */
    @Override
    public long modifyProfile(UserDTO dto) {

        /* id 세션값으로 변경해야 함 */
        Long id = 101L;
        Optional<User> findBoardId = repository.findById(id);// update 해 줘야 함 => 받아줘야

        User findBoard = findBoardId.orElseThrow(() -> {
            throw new RuntimeException();
        });

        findBoard.setNickname(dto.getNickname());

        return 0;
    }

    @Override // Qna 리스트
    public List<InfoBoardDTO> getQnaList() {

        /* 세션값 받으면 수정해야 함 */
        Long id = 101L;
        List<InfoBoardDTO> list = infoRepository.mypageList(id);

        return list;
    }

    /** 진짜 모달창 */
    @Override
    public String updateNickname(Long id, String nickname, String profile) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setNickname(nickname);
        user.setProfile(profile);

        repository.save(user);
        return user.getNickname() + user.getProfile();
    }




}
