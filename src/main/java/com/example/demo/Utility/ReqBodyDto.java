package com.example.demo.Utility;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ReqBodyDto {

    private String email;

    private String scheduleDescription;

    private  String scheduleTitle;

    private String date;

    @Override
    public String toString() {
        return "ReqBodyDto{" +
                "email='" + email + '\'' +
                ", scheduleDescription='" + scheduleDescription + '\'' +
                ", scheduleTitle='" + scheduleTitle + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
