package com.example.travelko.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruitForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    @NotEmpty(message="지역은 필수항목입니다.")
    @Size(max=200)
    private String region;
    
    @NotEmpty(message="시작날짜는 필수항목입니다.")
    @Size(max=200)
    private String startDate;
    
    @NotEmpty(message="종료날짜는 필수항목입니다.")
    @Size(max=200)
    private String endDate;
}
