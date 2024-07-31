package hello.schedule.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.Period;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this.startTime = LocalDateTime.now();
    }

    public Schedule(String name, LocalDateTime deadline, int difficulty,
                    int urgency, int importance, int restTime, int stress) {
        this.name = name;
        this.startTime = LocalDateTime.now();
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.urgency = urgency;
        this.importance = importance;
        this.restTime = restTime;
        this.stress = stress;
    }
}
