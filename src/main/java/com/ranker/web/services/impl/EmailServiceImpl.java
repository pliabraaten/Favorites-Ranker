package com.ranker.web.services.impl;

import com.ranker.web.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
        // For development: Just log the reset link
        // In production, you'd send a real email here

        logger.info("===========================================");
        logger.info("PASSWORD RESET EMAIL");
        logger.info("To: " + toEmail);
        logger.info("Reset Link: " + resetUrl);
        logger.info("===========================================");
    }
}

//// FIXME: PRODUCTION CODE
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Override
//    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Password Reset Request - Favorites Ranker");
//        message.setText("Click the link below to reset your password:\n\n"
//                + resetUrl
//                + "\n\nThis link will expire in 30 minutes.\n\n"
//                + "If you didn't request this, please ignore this email.");
//
//        mailSender.send(message);
//    }
//}