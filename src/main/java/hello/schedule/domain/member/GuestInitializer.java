package hello.schedule.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.findByLoginId("guest").isEmpty()) {
            Member guest = new Member();
            guest.setLoginId("guest");
            guest.setName("Guest User");
            guest.setPassword("guest"); // 실제로는 비밀번호가 필요 없지만, 필드가 필수라면 설정 필요
            memberRepository.save(guest);
        }
    }
}
