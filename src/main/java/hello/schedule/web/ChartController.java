package hello.schedule.web;

import hello.schedule.domain.schedule.Schedule;
import hello.schedule.domain.schedule.ScheduleDTO;
import hello.schedule.domain.schedule.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/charts")
public class ChartController {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ChartController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/example")
    public String show(Model model, @ModelAttribute("selectedIds") List<Long> selectedIds) {

        List<Schedule> schedules = scheduleRepository.findAllById(selectedIds);
        List<ScheduleDTO> scheduleDTOs = schedules.stream()
                .map(schedule -> {
                    ScheduleDTO dto = new ScheduleDTO();
//                    dto.setId(schedule.getId());
                    dto.setName(schedule.getName());
                    dto.setStartTime(schedule.getStartTime());
                    dto.setDeadline(schedule.getDeadline());
                    dto.setDifficulty(schedule.getDifficulty());
                    dto.setUrgency(schedule.getUrgency());
                    dto.setImportance(schedule.getImportance());
                    dto.setRestTime(schedule.getRestTime());
                    dto.setStress(schedule.getStress());
                    return dto;
                })
                .toList();
        model.addAttribute("schedules", scheduleDTOs);

        return "charts/example";
    }

}

