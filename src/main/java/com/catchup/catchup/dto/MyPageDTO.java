package com.catchup.catchup.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageDTO {

    /* 프로필 사진 / 닉네임 변경 관련 */
    private Long uid;

    private String id;
    private String password;
    private String nickname;
    private String addr;
    private String profile;
    private String phone;

}
