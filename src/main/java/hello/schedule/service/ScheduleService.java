package hello.schedule.service;

import hello.schedule.domain.member.Member;
import hello.schedule.domain.member.MemberRepository;
import hello.schedule.domain.schedule.Schedule;
import hello.schedule.domain.schedule.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Schedule saveSchedule(String name, LocalDateTime deadline, int difficulty, int urgency, int importance, int restTime, int stress, Long memberId) {
        Schedule schedule = new Schedule(name, deadline, difficulty, urgency, importance, restTime, stress, memberId);

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + id));
    }

    @Transactional
    public List<Schedule> getSchedulesByMemberId(Long memberId) {
        return scheduleRepository.findByMemberId(memberId);
    }
}