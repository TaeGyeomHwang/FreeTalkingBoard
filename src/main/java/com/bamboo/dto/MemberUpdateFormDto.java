package com.bamboo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberUpdateFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Length(min = 3, max = 8, message = "이름은 3글자 이상, 8글자 이하로 입력해주세요.")
    private String name; // 유저 아이디

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password;

    public MemberUpdateFormDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
