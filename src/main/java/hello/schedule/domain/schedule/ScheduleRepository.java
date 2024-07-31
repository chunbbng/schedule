package hello.schedule.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
