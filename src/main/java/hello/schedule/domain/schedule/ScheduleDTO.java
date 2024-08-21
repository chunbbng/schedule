package hello.schedule.domain.schedule;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
//    private Long id;
    private String name;
    private String startTime;
    private String deadline;
    private int difficulty;
    private int urgency;
    private int importance;
    private int restTime;
    private int stress;
}
