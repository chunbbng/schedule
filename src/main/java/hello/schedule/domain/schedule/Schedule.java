package hello.schedule.domain.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime deadline;

    private int difficulty;
    private int urgency;
    private int importance;
    private int restTime;
    private PreferenceTime preferenceTime;
    private int stress;
    private Long memberId;

    @Column(nullable = true)
    private int adjustDays = 0;
    @Column(nullable = true)  // nullable = true로 설정하여 null 값 허용
    private double adjustTime = 0.0;  // 기본값을 0.0으로 설정

    public enum PreferenceTime {
        MORNING,
        AFTERNOON,
        EVENING,
        NIGHT
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "existing_foreign_key_column_name")
    @JsonIgnore
    private Member member;

    public Schedule() {
    }

    public Schedule(String name, LocalDateTime deadline, int difficulty,
                    int urgency, int importance, int restTime, int stress, Long memberId) {
        this.name = name;
        this.startTime = LocalDateTime.now();
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.urgency = urgency;
        this.importance = importance;
        this.restTime = restTime;
        this.stress = stress;
        this.memberId = memberId;
    }
}
