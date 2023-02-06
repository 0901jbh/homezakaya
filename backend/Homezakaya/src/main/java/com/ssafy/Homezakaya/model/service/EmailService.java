package com.ssafy.Homezakaya.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Random;

//@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    
    private final JavaMailSender javaMailSender;
    
    // 인증번호 생성
    private final String ePw = createKey();

    @Value("${spring.mail.username}")
    private String id;

    // 메일 양식 작성
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
//        log.info("보내는 대상 : " + to);
//        log.info("인증 번호 : " + ePw);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("HOMEZAKAYA 인증코드");

        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "Admin"));

        return message;
    }

    // 인증코드 생성
    private String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for(int i = 0; i <6; i++){  // 6자리 코드
            key.append(rnd.nextInt(10));
        }
        return key.toString();
    }

    // 메일 발송

    /**
     * 
     * @param to : 인증번호를 받을 메일 주소
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public String sendSimpleMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = createMessage(to);    // 메일로 전송할 내용 담기
        try{
            javaMailSender.send(message);
        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드 서버로 리턴
    }
}
