package com.catchup.catchup.service;

import com.catchup.catchup.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyPageService {
    MyPageDTO getProfile(Long uid);

    UserDTO getInfo(Long uid); // 정보 수정하기

    MyPageDTO getRepList(); // 댓글 리스트 불러오기

    Page<FreeBoardDTO> boardlist(Pageable pageable, Long uid); // 내가 쓴 글 불러오기

    long modifyProfile(UserDTO dto);

    List<InfoBoardDTO> getQnaList(Long uid); // Qna 리스트

    long modifyInfo(UserDTO dto, Long uid);

    String updateNickname(Long id, String nickname, String profile);

    void schoolInsert(UserDTO dto, Long uid);

    UserDTO getBap(Long uid);
}
