package OnlineMarket.service;

import OnlineMarket.form.config.GeneralConfig;
import OnlineMarket.model.Notification;
import OnlineMarket.model.Order;
import eu.bitwalker.useragentutils.UserAgent;
import OnlineMarket.form.config.ContactConfig;
import OnlineMarket.form.config.EmailSystemConfig;
import OnlineMarket.model.User;
import OnlineMarket.service.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Service
public class SendMailConstruction {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    @Qualifier("emailTemplateEngine")
    SpringTemplateEngine emailTemplateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendResetTokenEmail(
            HttpServletRequest request, String token, User user) throws MessagingException, TemplateInputException, MailSendException
    {

        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        ContactConfig contactConfig = configurationService.getContact();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());



        String url = processUrl(request);

        String urlResetToken = url + "change-password?id=" +
                user.getId() + "&token=" + token;

        final Context ctx = new Context();
        ctx.setVariable("name", user.getFirstName()+" "+user.getLastName());
        ctx.setVariable("location", contactConfig.getAddress());
        ctx.setVariable("signature", url);
        ctx.setVariable("action_url", urlResetToken);
        ctx.setVariable("operating_system", userAgent.getOperatingSystem().getName());
        ctx.setVariable("browser_name", userAgent.getBrowser().getName()+ " v" + userAgent.getBrowserVersion() );
        ctx.setVariable("support_url",  contactConfig.getEmail());

        final String html =  emailTemplateEngine.process("html/reset-password-email", ctx);

        constructEmail("Reset password", messageHelper, user, html);

        javaMailSender.send(message);
    }

    public void sendVerificationEmail(final User user, final String token, HttpServletRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        ContactConfig contactConfig = configurationService.getContact();

        String url = processUrl(request);

        String urlResetToken = url + "registration-confirm?token=" + token;

        final Context ctx = new Context();
        ctx.setVariable("name", user.getFirstName()+" "+user.getLastName());
        ctx.setVariable("location", contactConfig.getAddress());
        ctx.setVariable("signature", url);
        ctx.setVariable("login_url", url+"login");
        ctx.setVariable("systemName", configurationService.getGeneral().getTitle());
        ctx.setVariable("action_url", urlResetToken);
        ctx.setVariable("support_url",  contactConfig.getEmail());

        final String html =  emailTemplateEngine.process("html/verification-email", ctx);

        constructEmail("Verification Account", messageHelper, user, html);

        javaMailSender.send(message);
    }

    public void sendApprovedOrderTotUser(final Order order, final User user,HttpServletRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        final ContactConfig contactConfig = configurationService.getContact();
        final GeneralConfig generalConfig = configurationService.getGeneral();


        String url = processUrl(request);


        final Context ctx = new Context();
        ctx.setVariable("order", order);
        ctx.setVariable("general", generalConfig);
        ctx.setVariable("title", "Order #"+order.getId()+" was approved.");
        ctx.setVariable("contact", contactConfig);
        ctx.setVariable("systemName", configurationService.getGeneral().getTitle());
        ctx.setVariable("support_url",  contactConfig.getEmail());
        ctx.setVariable("location", contactConfig.getAddress());
        ctx.setVariable("signature", url);

        final String html =  emailTemplateEngine.process("html/confirm-order-email", ctx);

        constructEmail("Order #"+order.getId()+" was approved.", messageHelper, user, html);

        javaMailSender.send(message);
    }

    public void sendDeleteOrderToUser(Notification notification, HttpServletRequest request) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        ContactConfig contactConfig = configurationService.getContact();

        String url = processUrl(request);


        final Context ctx = new Context();
        ctx.setVariable("name", notification.getUser().getFirstName()+" "+notification.getUser().getLastName());
        ctx.setVariable("location", contactConfig.getAddress());
        ctx.setVariable("signature", url);
        ctx.setVariable("content", notification.getContent());
        ctx.setVariable("systemName", configurationService.getGeneral().getTitle());
        ctx.setVariable("support_url",  contactConfig.getEmail());

        final String html =  emailTemplateEngine.process("html/content-email", ctx);

        constructEmail("Your order wasn't approved", messageHelper, notification.getUser(), html);

        javaMailSender.send(message);
    }


    private void constructEmail(String subject, MimeMessageHelper mimeMessageHelper, User user, String content) throws MessagingException {
        EmailSystemConfig emailSystemConfig = configurationService.getEmail();

        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setText(content, true);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(emailSystemConfig.getUserName());

    }

    private String processUrl(HttpServletRequest request){
        final int serverPort = request.getServerPort();
        String url;
        if ((serverPort == 80) || (serverPort == 443)) {
            // No need to add the server port for standard HTTP and HTTPS ports, the scheme will help determine it.
            url = String.format("%s://%s/%s", request.getScheme(), request.getServerName(),request.getContextPath());
        } else {
            url = String.format("%s://%s:%s/%s", request.getScheme(), request.getServerName(), serverPort, request.getContextPath());
        }
        return url;
    }

}
