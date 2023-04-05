package com.tafakkoor.e_learn.services;

import com.tafakkoor.e_learn.domain.AuthUser;
import com.tafakkoor.e_learn.repository.AuthUserRepository;
import com.tafakkoor.e_learn.repository.TokenRepository;
import com.tafakkoor.e_learn.utils.Util;
import com.tafakkoor.e_learn.utils.mail.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ScheduleService {

    private final AuthUserRepository authUserRepository;

    private final TokenRepository tokenRepository;


    public ScheduleService( AuthUserRepository authUserRepository, TokenRepository tokenRepository ) {
        this.authUserRepository = authUserRepository;
        this.tokenRepository = tokenRepository;
    }

    // send email to user that have not logged in for 3 days
    @Scheduled( fixedRate = 1000 * 60 * 60 * 24 ) // 1 day
    public void sendEmailToInactiveUsers() { // done
        List<AuthUser> users = authUserRepository.findByLastLoginBefore(LocalDateTime.now().plusDays(3));
        EmailService emailService = EmailService.getInstance();
        for ( AuthUser user : users ) {
            CompletableFuture.runAsync(() -> emailService.sendEmail(user.getEmail(),
                    Util.getInstance().generateBodyForInactiveUsers(user.getUsername())
                    , "Login to Your Account"));
        }
    }


    @Scheduled( fixedRate = ( 1000 * 60 * 10 ) ) //  10 minutes
    public void deleteExpiredTokens() { // done
        tokenRepository.deleteByCreatedAtBefore();
    }

    @Scheduled( fixedRate = 1000 * 60 * 10 ) // 10 MINUTE
    public void deleteInactiveUsers() {   // done
        authUserRepository.deleteByStatusInActive();
    }

    // schedule for a task to be executed every day at 12:00 am
    @Scheduled( cron = "0 0 12 * * *" )
    public void sendBirthdayEmails() { // done
        EmailService emailService = EmailService.getInstance();

        List<AuthUser> users = authUserRepository.findAllByBirtDate(LocalDateTime.from(LocalDate.now()));
        for ( AuthUser user : users ) {
            CompletableFuture.runAsync(() -> emailService.sendEmail(user.getEmail(),
                    Util.getInstance().generateBodyForBirthDay(user.getUsername())
                    , "Happy Birthday"));
        }

    }
}
