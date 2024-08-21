package hello.schedule.domain.schedule;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CalendarDTO {
    private List<String> selectedDates;
    private String scheduleName;
}

