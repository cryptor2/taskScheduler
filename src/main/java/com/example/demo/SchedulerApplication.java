package com.example.demo;

import com.example.demo.Service.Impl.ScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class SchedulerApplication {
	@Autowired
	ScheduleServiceImpl scheduleService;
	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}
}

