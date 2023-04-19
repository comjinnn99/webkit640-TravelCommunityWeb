package com.example.travelko.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyForm {
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
    
    @NotEmpty(message = "나이는 필수항목입니다.")
    private String age;
   
    @NotEmpty(message = "휴대전화 번호는 필수항목입니다.")
    private String phone;
}
