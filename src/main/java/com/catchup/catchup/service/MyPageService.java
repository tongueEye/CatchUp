package com.catchup.catchup.service;

import com.catchup.catchup.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyPageService {
    MyPageDTO getProfile();

    UserDTO getInfo(); // 정보 수정하기

    MyPageDTO getRepList(); // 댓글 리스트 불러오기

    Page<FreeBoardDTO> boardlist(Pageable pageable); // 내가 쓴 글 불러오기

    long modifyProfile(UserDTO dto);

    List<InfoBoardDTO> getQnaList(); // Qna 리스트

    long modifyInfo(UserDTO dto);

    String updateNickname(Long id, String nickname);
}
