package testcode.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import testcode.cafekiosk.spring.client.mail.MailSendClient;
import testcode.cafekiosk.spring.domain.history.mail.MailSendHistory;
import testcode.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)//이렇게 해야 어노테이션으로 Mock을 만들 수 있음
class MailServiceTest {

    @Mock private MailSendClient mailSendClient;
    @Mock private MailSendHistoryRepository mailSendHistoryRepository;

    /**
     * MailService 의 생성자를 보고
     * Mock 객체로 선언된 애들을 injection 해준다
     */
    @InjectMocks private MailService mailService;

    @DisplayName("메일 전송 테스트 순수 Mockito")
    @Test
    void sendMail(){
        //given
        //Mock 객체 생성
        //annotaion으로 만들 수 있음
//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);
        /**
         * 얘도 어노테이션으로 만들 수 있음
         */
//        MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);//2개의 Mock 객체를 가지고 있는 메일 서비스 생성

        /**
         * given 위치인데 when이라는 문법을 사용하게 돼서
         * BDDMockito 가 생김
         */
//        Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(true);

        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                        .willReturn(true);


        //when
        boolean result = mailService.sendMail("", "", "", "");

        //then
        assertThat(result).isTrue();
        //한번 호출 됐는지 검증하는 method
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

    /**
     * Spy: 한 객체에서 일부만 stubbing 하고 싶을 때 사용
     * Spy는 실제 객체를 기반으로 만들어지기 때문에 when사용불가(stubbing)
     * do를 사용해서 해야함
     * Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
     *        .thenReturn(true); -> @Spy를 사용하려면
     * doReturn(true)
     *        .when(mailSendClient)
     *        .sendEmail(anyString(), anyString(), anyString(), anyString());
     *
     * 한 객체에서 일부는 진짜, 일부는 stubbing 하고 싶을때 spy 사용
     */

}