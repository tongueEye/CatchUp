package com.catchup.catchup.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.w3c.dom.Text;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long uid;

    @NotEmpty(message = "입력해")
    private String id;

    @NotEmpty(message = "입력해")
    private String password;

    private String nickname;

    private String name;

    private String addr;

    private String phone;

    private String profile;

    private String schoolCode;

    private String schoolName;

    private String local_code;



}
