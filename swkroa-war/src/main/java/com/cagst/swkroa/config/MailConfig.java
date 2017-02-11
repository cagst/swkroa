package com.cagst.swkroa.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration class for the system's Mail framework.
 *
 * @author Craig Gaskill
 */
@Configuration
public class MailConfig {
  @Bean(name = "mailService")
  public MailSender getMailService() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("outbound.att.net");
    mailSender.setPort(25);
    mailSender.setUsername("cgaskill23824@att.net");
    mailSender.setPassword("7DfcLtychG2HnxYhDaef");

    Properties mailProperties = new Properties();
    mailProperties.setProperty("mail.smtp.auth", "true");
    mailProperties.setProperty("mail.smtp.starttls.enable", "true");
    mailProperties.setProperty("mail.smtp.timeout", "8500");

    mailSender.setJavaMailProperties(mailProperties);

    return mailSender;
  }
}
