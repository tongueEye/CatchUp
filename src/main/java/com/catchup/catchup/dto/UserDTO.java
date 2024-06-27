package com.catchup.catchup.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.w3c.dom.Text;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long uid;

    @NotEmpty(message = "아이디를 입력하세요")
    private String id;

    @NotEmpty(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{6,12}", message = "비밀번호는 6~12자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotEmpty(message = "닉네임을 입력하세요")
    private String nickname;

    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    @NotEmpty(message = "주소를 입력하세요")
    private String addr;

    @NotEmpty(message = "휴대폰 번호를 입력하세요")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 형식으로 입력하세요.")
    private String phone;

    private String profile;

    private String sidoCode;

    private String sdschulCode;

    private String schoolName;



}
