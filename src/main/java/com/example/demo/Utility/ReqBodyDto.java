package com.example.demo.Utility;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
}
