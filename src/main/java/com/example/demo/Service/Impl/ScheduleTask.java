<<<<<<< HEAD
package com.example.demo.Service.Impl;public class ScheduleTask {
=======
package com.example.demo.Service.Impl;

import com.example.demo.Entity.Schedule;
import com.example.demo.Repository.ScheduleRepo;
import com.example.demo.Service.MailSenderi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class ScheduleTask {

    //Size of prioroty queue
    int CAPACITY = 20000;

    //size of the page
    int PAGE_SIZE = 20000;


    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private MailSenderi mailSender;

    //Created scheduled thread pool to check the database on specific interval
    private ScheduledExecutorService executor =  Executors.newScheduledThreadPool(1);
    private ExecutorService fixedExecuter = Executors.newFixedThreadPool(20);

    ScheduleTask(ScheduleRepo scheduleRepo){
        this.scheduleRepo = scheduleRepo;
        ScheduledTask();
    }

    //sort priority queue on the base scheduled timestamp
    static class scheudleComparator implements Comparator<Schedule>{
        public int compare(Schedule s1, Schedule s2) {
            if (s1.getSchdeuledate().before(s2.getSchdeuledate()))
                return 1;
            else if (s1.getSchdeuledate().after(s2.getSchdeuledate()))
                return -1;
            return 0;
        }
    }
/*
* Check the database on the every 5 minutes and fetch all the record that are scheduled in next 5 minutes.
* count the total records and get the total pages (noOfPages = totalRecords / PAGE_SIZE); get the upper bound of the division
* call addToTasksToPQ that add all records of a page into the priority queue
*/
    public void ScheduledTask() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Your task that needs to run every hour
                LocalDateTime localDateTimeStart =  LocalDateTime.now();
                LocalDateTime localDateTimeEnd = localDateTimeStart.plusMinutes(5);
                try {

                    List<Schedule> schedules = scheduleRepo.findByDateTime(localDateTimeEnd);
//                    List<Schedule> schedule = scheduleRepo.findAll();

                    long noOfPages = schedules.size()/PAGE_SIZE;
                    if(schedules.size() % PAGE_SIZE > 0) noOfPages++;
                    int i = 0;
                    int j = 0;
                    while(i < noOfPages) {
                        addTasksToPQ(j, schedules);
                        j = j + PAGE_SIZE;
                        i++;
                    }

                } catch (Exception e) {
                    System.out.println("Error in scheduled task " + e);
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

/*
* Get schedule receives the pq of the max size of CAPACITY
* Creates the SingleThreadScheduledExecutor service;
* after every 20 seconds we check is timeScheduled of the task is passed of is equal to the current time if yes then
* we pop out the record from the pq and add call the required api(in this application we print it on console)
* after this we add it to the list of completedTask
* we run the loop and pop out the element from the priority queue if current time is greater than or equal to the top element of the pq
* at the end we delete all the tasks from the database that are present in the completed queue;
* and shutdown the thread if pq is empty because nothing is left for this thread to do
* */
    public void getScheduledTask(PriorityQueue<Schedule> pq) {
        ScheduledExecutorService getSchedule = Executors.newSingleThreadScheduledExecutor();
        getSchedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Your task that needs to run every second

                Date date = new Date();
                List<Schedule> completedTasks = new ArrayList<>();
               while(!pq.isEmpty() && (pq.peek().getSchdeuledate().before(date) || pq.peek().getSchdeuledate().equals(date))){

                   Schedule schedule = pq.poll();
                   completedTasks.add(schedule);

                   // call your api here
                   mailSender.sendEmail(schedule.getEmail(), schedule.getScheduleTitle(), schedule.getScheduleDescription());
//                   System.out.println(schedule.getSchdeuledate() + " " + new Date());
               }

//                System.out.println("Completed successfully page containing " + completedTasks.size() + "elements");

               // delete the completedTasks from the database.
                if(!completedTasks.isEmpty()) scheduleRepo.deleteAll(completedTasks);

                // terminate thread when pq is empty
                if(pq.isEmpty())  getSchedule.shutdownNow();
            }
        }, 0, 20, TimeUnit.SECONDS);
    }

    public void addTasksToPQ(int j, List<Schedule> schedules) {
        fixedExecuter.execute (new Runnable() {
            @Override
            public void run() {
                PriorityQueue<Schedule> pq = new PriorityQueue<Schedule>(CAPACITY, new scheudleComparator());
                int k = j;
                while(k < j + PAGE_SIZE && k < schedules.size()){
                    Schedule schedule = schedules.get(k);
                    pq.add(schedule);
                    k++;
                }
                getScheduledTask(pq);
            }
        });
    }
>>>>>>> 055cab5 (Initial commit)
}
