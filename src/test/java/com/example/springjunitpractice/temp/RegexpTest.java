package com.example.springjunitpractice.temp;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class RegexpTest {
    
    @Test
    public void username_test() throws Exception {
        String username = "user";
        boolean result = Pattern.matches("^[a-z0-9]{4,16}$", username);
        System.out.println("테스트: " + result);
    }

    @Test
    public void password_text() throws Exception {
        String password = "1234";
        boolean result = Pattern.matches("^[a-z0-9]{4,16}$", password);
        System.out.println("테스트: " + result);
    }

    @Test
    public void email_text() throws Exception {
        String email = "user@naver.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{4,16}@[a-zA-Z0-9]{2,16}\\.[a-zA-Z]{2,3}$", email);
        System.out.println("테스트: " + result);
    }

    @Test
    public void fullname_text() throws Exception {
        String email = "User";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{2,20}$", email);
        System.out.println("테스트: " + result);
    }
}
