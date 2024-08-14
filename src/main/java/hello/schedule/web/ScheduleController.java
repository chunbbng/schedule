package hello.schedule.web;

import hello.schedule.SessionConst;
import hello.schedule.domain.member.Member;
import hello.schedule.domain.member.MemberRepository;
import hello.schedule.domain.schedule.Schedule;
import hello.schedule.domain.schedule.ScheduleRepository;
import hello.schedule.domain.schedule.ScheduleResult;
import hello.schedule.service.ConcentrateSchedule;
import hello.schedule.service.ScheduleService;
import hello.schedule.session.SessionManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ConcentrateSchedule concentrateSchedule;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ScheduleService scheduleService;


    @GetMapping("/cabinet")
    public String sendCabinet(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              RedirectAttributes redirectAttributes, Model model) {
        if (loginMember == null) {
            return "redirect:/";
        }
        String loginId = loginMember.getLoginId();

        return "redirect:/cabinet/" + loginId;
    }

    @PostMapping("/cabinet")
    public String processSelectedSchedules(@RequestBody SelectionRequest request,
                                           RedirectAttributes redirectAttributes) {
        try {
            List<Long> selectedIds = request.getSelectedSchedules();

            for (Long selectedId : selectedIds) {
                log.info("selectedId={}", selectedId);
            }
            redirectAttributes.addAttribute("selectedIds", selectedIds);

            // 성공 시 리다이렉트
            return "redirect:/charts/example";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/errorPage"; // 에러 시 리다이렉트
        }
    }


    public static class SelectionRequest {
        private List<Long> selectedSchedules;

        public List<Long> getSelectedSchedules() {
            return selectedSchedules;
        }

        public void setSelectedSchedules(List<Long> selectedSchedules) {
            this.selectedSchedules = selectedSchedules;
        }
    }

    @GetMapping("/cabinet/{loginID}")
    public String cabinet(@PathVariable String loginID,
                          @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                          Model model) {

        // 로그인한 사용자와 관련된 Member 객체를 가져옵니다
        Member member = memberRepository.findByLoginId(loginID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login Id:" + loginID));

        // 사용자의 모든 schedules를 가져옵니다
        List<Schedule> schedules = scheduleRepository.findByMemberId(loginMember.getId());

        // 모델에 추가합니다
        model.addAttribute("member", member);
        model.addAttribute("schedules", schedules);
        String loginId = member.getLoginId();
        model.addAttribute("loginId", loginId);
        log.info("loginMember={}", loginId);

        return "cabinet";
    }

    @GetMapping("/create")
    public String add(){
        return "/create";
    }

    @PostMapping("/create")
    public String addSchedule(@RequestParam String name,
                              @RequestParam String deadline,
                              @RequestParam int difficulty,
                              @RequestParam int urgency,
                              @RequestParam int importance,
                              @RequestParam int restTime,
                              @RequestParam int stress,
                              RedirectAttributes redirectAttributes,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember){
        LocalDateTime end = LocalDateTime.parse(deadline);

        Schedule savedSchedule = scheduleService.saveSchedule(name, end, difficulty, urgency, importance, restTime, stress, loginMember.getId());
        // 로그 추가
        System.out.println("Saved Schedule: " + savedSchedule);
        redirectAttributes.addAttribute("loginId", loginMember.getLoginId());
        redirectAttributes.addAttribute("id", savedSchedule.getId());
        return "redirect:/select/{loginId}/{id}";
    }

    @GetMapping("/select/{loginID}/{id}")
    public String task(@PathVariable Long id, Model model,
                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        schedule.ifPresent(s -> model.addAttribute("schedule", s));
        model.addAttribute("id", id);
        model.addAttribute("loginId", loginMember.getLoginId());
        return "select";
    }

    @GetMapping("/result/focus/{loginId}/{id}")
    public String focusedSchedule(@PathVariable String loginId, @PathVariable Long id, Model model) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if (scheduleOptional.isPresent()) {
            Schedule schedule = scheduleOptional.get();
            ScheduleResult result = concentrateSchedule.process(schedule);
            model.addAttribute("schedule", schedule);
            model.addAttribute("result", result);
            model.addAttribute("loginId", loginId);
            model.addAttribute("id", id);
        } else {
            return "redirect:/error";
        }
        return "result";
    }


    @GetMapping("/calendarCheck/{loginId}/{id}")
    public String calendarCheck(@PathVariable Long id, Model model) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (schedule.isPresent()) {
            ScheduleResult result = concentrateSchedule.process(schedule.get());
            model.addAttribute("adjustDays", result.getAdjustDays());
            model.addAttribute("adjustTime", result.getAdjustTime());
        }
        return "calendarCheck";
    }
}
