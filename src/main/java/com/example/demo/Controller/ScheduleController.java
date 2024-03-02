
package com.example.demo.Controller;

import com.example.demo.Entity.Schedule;
import com.example.demo.Service.ScheduleService;
import com.example.demo.Utility.ReqBodyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    // must be in this "2024-02-10 20:35:00" format
    @CrossOrigin
    @PostMapping("/addSchedule")
    public ResponseEntity<Schedule> addSchedule(@RequestBody ReqBodyDto reqBody) {
        System.out.println(reqBody.toString());
        Schedule schedule = scheduleService.addSchedule(reqBody);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        String res = scheduleService.test();
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
