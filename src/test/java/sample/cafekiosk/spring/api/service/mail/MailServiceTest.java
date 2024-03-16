package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.MailSendClient;
import sample.cafekiosk.spring.domain.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.mail.MailSendHistoryRepository;


// 모키토 사용할 꺼야 선언..! 이거 없으면 @Mock 적용이 안됨
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

  @Mock
//  @Spy // when() 사용x
  MailSendClient mailSendClient;

  @Mock
  MailSendHistoryRepository mailSendHistoryRepository;

  // Mock 객체 자동으로 DI
  @InjectMocks
  MailService mailService;

  @Test
  @DisplayName("메일 전송 테스트")
  void sendMail() {
    // given

//    // @Spy 일때
//    doReturn(true)
//        .when(mailSendClient)
//            .sendEmail(anyString(),anyString(),anyString(),anyString());

    // @Mock 일때
//    when(mailSendClient.sendEmail(
//            anyString(),
//            anyString(),
//            anyString(),
//            anyString())
//    ).thenReturn(true);

    // @BDDMockito 일때 (@Mock 일때 사용가능 -> BDD 스타일)
    BDDMockito.given(mailSendClient
            .sendEmail(anyString(), anyString(), anyString(), anyString()))
        .willReturn(true);

    // when
    boolean result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1))
        .save(any(MailSendHistory.class));
  }

}