package hello.schedule.service;

import hello.schedule.domain.Schedule;
import hello.schedule.domain.ScheduleResult;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ConcentrateSchedule {
    public ScheduleResult process(Schedule schedule) {
        ScheduleResult result = new ScheduleResult();
        result.setAdjustDays(calculateAdjustDays(schedule));
        result.setAdjustTime(calculateAdjustTime(schedule));
        return result;
    }

    private int calculateAdjustDays(Schedule schedule) {
        return 5;
    }

    private int calculateAdjustTime(Schedule schedule) {

        LocalDateTime startTime = schedule.getStartTime();
        LocalDateTime deadline = schedule.getDeadline();

        if (startTime == null || deadline == null) {
            System.out.println("startTime = " + startTime);
            System.out.println("deadline = " + deadline);
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(startTime.toLocalDate(), deadline.toLocalDate());
    }
}
