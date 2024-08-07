package hello.schedule.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("members/add")
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult){

        log.info(" id = {}, LoginId={}, password={}, name={}", member.getId(), member.getLoginId() , member.getPassword(), member.getName());

        if(bindingResult.hasErrors()){

            return "members/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }
}
