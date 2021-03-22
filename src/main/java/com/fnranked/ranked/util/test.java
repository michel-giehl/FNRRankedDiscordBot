package com.fnranked.ranked.util;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class test {

    public static void main(String[] args) {

        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken("NTIwMTY5Nzc1MjUxMzI0OTQ4.XiM1mg.tIV62ITmw14j0M3JeVUAXCUIJpE");
        try {
            JDA jda = jdaBuilder.build();
            System.out.println(jda.getSelfUser().getId());
        } catch (LoginException e1) {
            e1.printStackTrace();
        }
        //Ignore DM context exceptions
    }
}
