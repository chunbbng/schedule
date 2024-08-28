package hello.schedule.web;

import hello.schedule.SessionConst;
import hello.schedule.domain.algorithm.TaskAllocation;
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

import java.time.LocalDate;
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
    private final TaskAllocation taskAllocation;



    @GetMapping("/cabinet")
    public String sendCabinet(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              RedirectAttributes redirectAttributes, Model model) {
        if (loginMember == null) {
            return "redirect:/login";
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

    @PostMapping("/cabinet/delete")
    public String deleteSelectedSchedules(@RequestBody SelectionRequest request, RedirectAttributes redirectAttributes) {
        try {
            List<Long> selectedIds = request.getSelectedSchedules();

            // 선택된 스케줄을 삭제합니다.
            for (Long selectedId : selectedIds) {
                scheduleRepository.deleteById(selectedId);
            }

            // 삭제 후 다시 Cabinet 페이지로 리다이렉트
            return "redirect:/cabinet";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/errorPage"; // 에러 시 리다이렉트
        }
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

        Member member = (loginMember != null) ? loginMember : memberRepository.findByLoginId("guest")
                .orElseThrow(() -> new IllegalArgumentException("Guest member not found"));

        Long memberId = member.getId();
        String loginId = member.getLoginId();
        log.info("Using member with memberId={}, loginId={}", memberId, loginId);


        LocalDateTime end = LocalDateTime.parse(deadline);

        Schedule savedSchedule = scheduleService.saveSchedule(name, end, difficulty, urgency, importance, restTime, stress, memberId);
        // 로그 추가
        System.out.println("Saved Schedule: " + savedSchedule);
        redirectAttributes.addAttribute("loginId", loginId);
        redirectAttributes.addAttribute("id", savedSchedule.getId());
        return "redirect:/select/{loginId}/{id}";
    }

    @GetMapping("/select/{loginID}/{id}")
    public String task(@PathVariable Long id, Model model,
                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        schedule.ifPresent(s -> model.addAttribute("schedule", s));
        model.addAttribute("id", id);

        if (loginMember == null) {
            // 로그인된 사용자가 없는 경우 처리
            // 예: 게스트 사용자 처리 또는 로그인 페이지로 리다이렉트
            Member guestMember = memberRepository.findByLoginId("guest")
                    .orElseThrow(() -> new IllegalArgumentException("Guest member not found"));
            model.addAttribute("loginId", guestMember.getLoginId());
            log.info("Guest user accessed: guestId={}", guestMember.getLoginId());
        } else {
            model.addAttribute("loginId", loginMember.getLoginId());
            log.info("loginMember={}", loginMember.getLoginId());
        }

        return "select";
    }

    @PostMapping("/select/{loginId}/{id}")
    public String focusedSchedule(@RequestParam("option") String option, @PathVariable String loginId, @PathVariable Long id, Model model,
                                  @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {


        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if (scheduleOptional.isPresent()) {
            Schedule schedule = scheduleOptional.get();
            int totalScore = TaskAllocation.calculateTotalScore(schedule.getDifficulty(), schedule.getUrgency(), schedule.getImportance(), schedule.getStress());
            LocalDateTime deadLine = schedule.getDeadline();
            LocalDateTime startTime = schedule.getStartTime();
            int availableDays = (int) (deadLine.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay());
            int adjustDays = taskAllocation.calculateAdjustDays(availableDays, option);
            double adjustTime = TaskAllocation.calculateAdjustTime(totalScore);

            schedule.setAdjustDays(adjustDays);
            schedule.setAdjustTime(adjustTime);

            model.addAttribute("schedule", schedule);
            model.addAttribute("adjustDays", adjustDays);
            model.addAttribute("adjustTime", adjustTime);
            model.addAttribute("loginId", loginId);
            model.addAttribute("id", id);

            if (loginMember == null) {
                // 비회원인 경우 게스트 정보를 모델에 추가
                Member guestMember = memberRepository.findByLoginId("guest")
                        .orElseThrow(() -> new IllegalArgumentException("Guest member not found"));
                model.addAttribute("loginId", guestMember.getLoginId());
                model.addAttribute("message", "You are viewing as a guest user.");
                log.info("Guest user accessed result page: guestId={}", guestMember.getLoginId());
            } else {
                model.addAttribute("loginId", loginMember.getLoginId());
                log.info("loginMember={} accessed result page", loginMember.getLoginId());
            }
        } else {
            return "redirect:/error";
        }
        return "result";
    }


    @GetMapping("/calendarCheck/{loginId}/{id}")
    public String calendarCheck(@PathVariable Long id, Model model) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        if (schedule.isPresent()) {
            Schedule actualSchedule = schedule.get();
            ScheduleResult result = concentrateSchedule.process(schedule.get());
            model.addAttribute("adjustDays", result.getAdjustDays());
            model.addAttribute("adjustTime", result.getAdjustTime());
            model.addAttribute("startTime", actualSchedule.getStartTime());
            model.addAttribute("deadLine", actualSchedule.getDeadline());
            model.addAttribute("id", id);
            model.addAttribute("scheduleName", actualSchedule.getName());
        }
        return "calendarCheck";
    }
}
