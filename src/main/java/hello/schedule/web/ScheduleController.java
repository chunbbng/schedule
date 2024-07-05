package hello.schedule.web;

import hello.schedule.domain.Schedule;
import hello.schedule.domain.ScheduleRepository;
import hello.schedule.domain.ScheduleResult;
import hello.schedule.service.ConcentrateSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ConcentrateSchedule concentrateSchedule;

    private final ScheduleRepository scheduleRepository;

    @GetMapping ("/cabinet")
    public String cabinet(Model model){
        List<Schedule> schedules = scheduleRepository.findAll();
        model.addAttribute("schedules", schedules);
        return "/cabinet";
    }

    @GetMapping("/create")
    public String add(){
        return "/create";
    }

    @PostMapping("/create")
    public String addSchedule(Schedule schedule, RedirectAttributes redirectAttributes){
        Schedule savedSchedule = scheduleRepository.save(schedule);
        schedule.setStartTime(LocalDateTime.now());
        redirectAttributes.addAttribute("id", savedSchedule.getId());
        return "redirect:/select/{id}";
    }

    @GetMapping("/select/{id}")
    public String task(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id);
        model.addAttribute("schedule", schedule);
        model.addAttribute("id", id);
        return "select";
    }

    @GetMapping("/result/focus/{id}")
    public String focusedSchedule(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id);
        ScheduleResult result = concentrateSchedule.process(schedule);
        model.addAttribute("schedule", schedule);
        model.addAttribute("result", result);
        return "/result";
    }

    @GetMapping("/calendarCheck/{id}")
    public String calendarCheck(@PathVariable Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id);
        ScheduleResult result = concentrateSchedule.process(schedule);
        model.addAttribute("adjustDays", result.getAdjustDays());
        model.addAttribute("adjustTime", result.getAdjustTime());
        return "calendarCheck";
    }



}
