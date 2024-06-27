package com.catchup.catchup.service;

import com.catchup.catchup.domain.Love;
import com.catchup.catchup.domain.User;
import com.catchup.catchup.dto.*;
import com.catchup.catchup.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageServiceImpl implements MyPageService{

    private final MyPageRepository repository;
    private final FreeBoardRepository freeRepository;
    private final InfoBoardRepository infoRepository;
    private final ModelMapper modelMapper;
    private final FreeRepBoardRepository repBoardRepository;
    private final LoveRepository loveRepository;

    @Override
    public MyPageDTO getProfile(Long uid) {

        Optional<User> result = repository.findById(uid);

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
    public UserDTO getInfo(Long uid) {

        // 세션 정보 받아와야 함...
        Optional<User> result = repository.findById(uid);// update 해 줘야 함 => 받아줘야
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
    public long modifyInfo(UserDTO dto, Long uid) {
        User user = repository.findById(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setAddr(dto.getAddr());

        repository.save(user);
        return uid;
    }


    /** 댓글 리스트 불러오기 */
    @Override
    public Page<RepBoardDTO> getRepList(Pageable pageable, Long uid) {
        Page<RepBoardDTO> list = repBoardRepository.mypageList(pageable, uid);

        return list;
    }

    /** 내가 쓴 글 불러오기 */
    @Override
    public Page<FreeBoardDTO> boardlist(Pageable pageable, Long uid) {

        Page<FreeBoardDTO> list = freeRepository.list(uid, pageable);

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
    public List<InfoBoardDTO> getQnaList(Long uid) {

        /* 세션값 받으면 수정해야 함 */
        List<InfoBoardDTO> list = infoRepository.mypageList(uid);

        return list;
    }

    /** 진짜 모달창 */
    @Override
    public String updateNickname(Long id, String nickname, String profile) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int startIndex = profile.indexOf("%2F") + 3;

        if (startIndex < 3) { // 파일이 없다면 예외 던짐
            throw new IllegalArgumentException("Invalid S3 URL");
        }

        String delete_file = profile.substring(startIndex);

        user.setNickname(nickname);
        user.setProfile(delete_file);

        repository.save(user);
        return user.getNickname() + user.getProfile();
    }

    @Override
    public void schoolInsert(UserDTO dto, Long uid) {
        repository.schoolInsert(dto, uid);

    }

    /** 밥 정보 불러오기!! */
    @Override
    public UserDTO getBap(Long uid) {

        Optional<User> result = repository.findById(uid);

        // 출력하려면 entity -> dto 변환하기
        modelMapper.map(User.class, UserDTO.class);

        UserDTO bap = result.stream().map(item -> modelMapper.map(item, UserDTO.class))
                .findAny()
                .orElseThrow(() -> {
                    throw new RuntimeException();
                });
        log.info("bap data ............... {} ", bap.getSidoCode());
        log.info("bap data ............... {} ", bap.getSdschulCode());
        return bap;
    }

    /** 좋아요 불러오기*/
    @Override
    public List<LoveDTO> getLove(Long uid) {
        List<LoveDTO> lovelist = loveRepository.findFreeboardTitleAndCateByUserId(uid)
                .stream()
                .map(tuple -> {
                    LoveDTO loveDTO = new LoveDTO();
                    loveDTO.setTitle((String) tuple[0]);
                    loveDTO.setCate((String) tuple[1]);
                    return loveDTO;
                })
                .collect(Collectors.toList());
        return lovelist;
    }
    /** 학교 정보 ㄹㅇ 디비에 값 변경하기 */
    @Transactional
    @Override
    public void school_result(UserDTO dto, Long uid) {
        Optional<User> result = repository.findById(uid);

        User findBoard = result.orElseThrow(() -> {
            throw new RuntimeException();
        });

        findBoard.setSchoolName(dto.getSchoolName());
        findBoard.setSdschulCode(dto.getSdschulCode());
        findBoard.setSidoCode(dto.getSidoCode());

        repository.save(findBoard);

    }

}
