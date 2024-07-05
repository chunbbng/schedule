package hello.schedule.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.Period;

@Data
public class Schedule {
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private int difficulty;
    private int urgency;
    private int importance;
    private int restTime;
    private PreferenceTime preferenceTime;
    private int stress;
    public enum PreferenceTime {
        MORNING,
        AFTERNOON,
        EVENING,
        NIGHT
    }

    public Schedule() {
    }

    public Schedule(String name, LocalDateTime startTime, LocalDateTime deadline, int difficulty,
                    int urgency, int importance, int restTime, int stress) {
        this.name = name;
        this.startTime = startTime;
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.urgency = urgency;
        this.importance = importance;
        this.restTime = restTime;
        this.stress = stress;
    }
}
