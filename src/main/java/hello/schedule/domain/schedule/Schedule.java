package hello.schedule.domain.schedule;

import hello.schedule.domain.member.Member;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idS;

    private String name;

    @DateTimeFormat(pattern = "yyyy:MM:dd hh:mm:ss")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "existing_foreign_key_column_name")
    private Member member;

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
