package testcode.cafekiosk.spring.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailSendClient {
    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
        //메일 전송
        log.info("메일 전송");
        throw new IllegalArgumentException("메일 전송");
        /**
         * 메일이 전송되면 예외 발생하도록
         * 이유: 테스트를 돌릴때마다 계속 메일 전송을 해야할까?
         * 테스트 메일 계정을 놓고 메일을 전송하는것도 방법이지만 시간과 비용 낭비를 줄이기 위해
         * mocking을 사용
         */
    }
}
