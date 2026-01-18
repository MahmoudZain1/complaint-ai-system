package com.ecommerce.complaints.service;

import com.ecommerce.complaints.config.EmailConfig;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.repository.api.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final EmailConfig emailConfig;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Async
    public void sendComplaintResponse(String customerEmail, String subject, String response)  {
          try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailConfig.getUsername());
            helper.setTo(customerEmail);
            helper.setSubject("Response to Your Complaint - " + subject);

            String htmlContent = buildComplaintResponseEmail(subject, response);
            helper.setText(htmlContent, true);

            mailSender.send(message);
    } catch (Exception e) {
    }
    }


    @Async
    public void notifyEmployeesOfPendingApproval(String complaintSubject, Long complaintId) {
            List<User> employees = userRepository.findByRoleIn(
                    List.of(UserRole.EMPLOYEE, UserRole.MANAGER, UserRole.ADMIN)
            );
            for (User employee : employees) {
                if (employee.isActive()) {
                    sendPendingApprovalNotification(
                            employee.getEmail(), employee.getName(), complaintSubject, complaintId
                    );
                }
            }
    }


    @Async
    public void notifyCustomerUnderReview(String customerEmail, String subject)  {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getUsername());
            helper.setTo(customerEmail);
            helper.setSubject("Your Complaint is Under Review - " + subject);

            String htmlContent = buildUnderReviewEmail(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
        }
    }


    private void sendPendingApprovalNotification(String email, String name,
                                                 String subject, Long complaintId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailConfig.getUsername());
            helper.setTo(email);
            helper.setSubject("New Response Pending Your Approval");

            String htmlContent = buildPendingApprovalEmail(name, subject, complaintId);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
        }
    }


    private String buildComplaintResponseEmail(String subject, String response) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; }
                    .response { background-color: white; padding: 15px; border-left: 4px solid #4CAF50; }
                    .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Response to Your Complaint</h2>
                    </div>
                    <div class="content">
                        <p>Dear Valued Customer,</p>
                        <p>Thank you for bringing this matter to our attention. We have reviewed your complaint regarding:</p>
                        <p><strong>%s</strong></p>
                        <div class="response">
                            <h3>Our Response:</h3>
                            <p>%s</p>
                        </div>
                        <p>If you have any further questions or concerns, please don't hesitate to reach out to us.</p>
                        <p>Best regards,<br><strong>%s</strong></p>
                    </div>
                    <div class="footer">
                        <p>This is an automated message. Please do not reply to this email.</p>
                        <p>© %d %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                subject,
                response.replace("\n", "<br>"),
                emailConfig.getCompanyName(),
                LocalDateTime.now().getYear(),
                emailConfig.getCompanyName()
        );
    }

    private String buildUnderReviewEmail(String subject) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #2196F3; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; }
                    .status { background-color: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 15px 0; }
                    .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Complaint Under Review</h2>
                    </div>
                    <div class="content">
                        <p>Dear Valued Customer,</p>
                        <p>We have received your complaint regarding:</p>
                        <p><strong>%s</strong></p>
                        <div class="status">
                            <h3>📋 Status Update</h3>
                            <p>Your complaint is currently under review by our customer service team. We are working on preparing a comprehensive response for you.</p>
                        </div>
                        <p>We appreciate your patience and will get back to you as soon as possible.</p>
                        <p>Estimated response time: <strong>24-48 hours</strong></p>
                        <p>Best regards,<br><strong>%s</strong></p>
                    </div>
                    <div class="footer">
                        <p>This is an automated message. Please do not reply to this email.</p>
                        <p>© %d %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                subject,
                emailConfig.getCompanyName(),
                LocalDateTime.now().getYear(),
                emailConfig.getCompanyName()
        );
    }

    private String buildPendingApprovalEmail(String name, String subject, Long complaintId) {
        String reviewUrl = "baseUrl" + "/api/responses/pending";

        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }
                    .content { background-color: #f9f9f9; padding: 20px; margin: 20px 0; }
                    .alert { background-color: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 15px 0; }
                    .button { display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; margin: 10px 0; }
                    .footer { text-align: center; color: #666; font-size: 12px; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>⚠️ Response Pending Your Approval</h2>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <div class="alert">
                            <h3>New Response Requires Review</h3>
                            <p><strong>Complaint ID:</strong> #%d</p>
                            <p><strong>Subject:</strong> %s</p>
                            <p><strong>Time:</strong> %s</p>
                        </div>
                        <p>A new AI-generated response has been created and requires your approval before being sent to the customer.</p>
                        <p>Please review and take action:</p>
                        <a href="%s" class="button">Review Response</a>
                        <p>Thank you for your prompt attention to this matter.</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated notification from the Complaint Management System.</p>
                        <p>© %d %s. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                name,
                complaintId,
                subject,
                LocalDateTime.now().format(DATE_FORMATTER),
                reviewUrl,
                LocalDateTime.now().getYear(),
                emailConfig.getCompanyName()

        );
    }

}