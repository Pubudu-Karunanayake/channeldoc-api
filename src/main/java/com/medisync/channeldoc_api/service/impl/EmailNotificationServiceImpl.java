package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.event.AppointmentBookedEvent;
import com.medisync.channeldoc_api.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendBookingConfirmation(AppointmentBookedEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getPatientEmail());
            helper.setSubject("Booking Confirmation - " + event.getAppointmentNumber());
            helper.setText(buildEmailContent(event), true);

            mailSender.send(message);
            log.info("Confirmation email sent successfully for appointment [{}] to [{}]",
                    event.getAppointmentNumber(), event.getPatientEmail());

        } catch (MessagingException e) {
            log.error("Failed to send confirmation email for appointment [{}] to [{}]: {}",
                    event.getAppointmentNumber(), event.getPatientEmail(), e.getMessage(), e);
        }
    }

    private String buildEmailContent(AppointmentBookedEvent event) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2 style="color: #2c3e50;">Appointment Booking Confirmation</h2>
                    <hr/>
                    <p>Dear <strong>%s</strong>,</p>
                    <p>Your appointment has been booked successfully. Here are the details:</p>
                    <table style="border-collapse: collapse; width: 100%%; max-width: 500px;">
                        <tr>
                            <td style="padding: 8px; font-weight: bold;">Appointment No:</td>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr style="background-color: #f2f2f2;">
                            <td style="padding: 8px; font-weight: bold;">Doctor:</td>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 8px; font-weight: bold;">Hospital:</td>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr style="background-color: #f2f2f2;">
                            <td style="padding: 8px; font-weight: bold;">Date:</td>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 8px; font-weight: bold;">Time:</td>
                            <td style="padding: 8px;">%s</td>
                        </tr>
                        <tr style="background-color: #f2f2f2;">
                            <td style="padding: 8px; font-weight: bold;">Fee:</td>
                            <td style="padding: 8px;">Rs. %.2f</td>
                        </tr>
                    </table>
                    <p style="margin-top: 20px;">Thank you for choosing MediSync ChannelDoc.</p>
                </body>
                </html>
                """.formatted(
                event.getPatientName(),
                event.getAppointmentNumber(),
                event.getDoctorName(),
                event.getHospitalName(),
                event.getAppointmentDate(),
                event.getSlotTime(),
                event.getConsultationFee()
        );
    }
}
