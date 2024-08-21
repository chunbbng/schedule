package hello.schedule;

import hello.schedule.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        if (loginMember != null) {
            model.addAttribute("memberName", loginMember.getName());
            model.addAttribute("isLoggedIn", true); // 로그인 여부 추가
        } else {
            model.addAttribute("isLoggedIn", false); // 로그아웃 상태
        }
        return "index"; // 메인 페이지 템플릿 이름
    }
}
