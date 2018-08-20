package OnlineMarket.listener;

import OnlineMarket.model.User;
import OnlineMarket.service.UserService;
import OnlineMarket.service.SendMailConstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import org.thymeleaf.exceptions.TemplateProcessingException;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    SendMailConstruction sendMailConstruction;

    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        String token;
        token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);
        try {
            sendMailConstruction.sendVerificationEmail(user, token, event.getRequest());
        } catch (MailSendException | MessagingException | TemplateProcessingException e) {
            e.printStackTrace();
        }
    }


}