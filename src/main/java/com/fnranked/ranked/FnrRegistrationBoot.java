package com.fnranked.ranked;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FnrRegistrationBoot {

    public static void main(String[] args) {
        SpringApplication.run(FnrRegistrationBoot.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> ctx.getBean(FnrRegistration.class).startBot();
    }
}