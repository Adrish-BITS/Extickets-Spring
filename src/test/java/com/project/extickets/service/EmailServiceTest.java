package com.project.extickets.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

class EmailServiceTest {

	@Mock
	private JavaMailSender mailSender;

	@InjectMocks
	private EmailService emailService;

	private MimeMessage mimeMessage;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mimeMessage = mock(MimeMessage.class);
		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
	}

	@Test
	void testSendEmailSuccess() {
		emailService.sendEmail("test@example.com", "Test Subject", "<h1>Hello</h1>");
		verify(mailSender, times(1)).createMimeMessage();
		verify(mailSender, times(1)).send(mimeMessage);
	}

	@Test
    void testSendEmailThrowsException() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException(new jakarta.mail.MessagingException("Simulated failure")))
                .when(mailSender).send(mimeMessage);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                emailService.sendEmail("fail@example.com", "Fail", "Body")
        );

        assertFalse(exception.getMessage().contains("Failed to send email"));
    }
}
