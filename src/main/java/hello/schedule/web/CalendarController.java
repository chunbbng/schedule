package hello.schedule.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.schedule.domain.schedule.CalendarDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(name = "dates", required = false) String datesStr,
                               @RequestParam(name = "scheduleName", required = false) String scheduleName,
                               Model model) {
        List<String> selectedDates = null;
        if (datesStr != null && !datesStr.isEmpty()) {
            // JSON 문자열을 리스트로 변환
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                selectedDates = objectMapper.readValue(datesStr, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // DTO 생성
        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setSelectedDates(selectedDates);
        calendarDTO.setScheduleName(scheduleName);
        log.info("calendarDTO={}", calendarDTO);

        // 모델에 DTO 추가
        model.addAttribute("calendarDTO", calendarDTO);

        return "calendar";
    }
}

