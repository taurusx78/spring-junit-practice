package com.example.springjunitpractice.temp;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class RegexpTest {
    
    @Test
    public void username_test() throws Exception {
        String value = "user";
        boolean result = Pattern.matches("^[a-z0-9]{4,16}$", value);
        System.out.println("테스트: " + result);
    }

    @Test
    public void password_test() throws Exception {
        String value = "1234";
        boolean result = Pattern.matches("^[a-z0-9]{4,16}$", value);
        System.out.println("테스트: " + result);
    }

    @Test
    public void email_test() throws Exception {
        String value = "user@naver.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{4,16}@[a-zA-Z0-9]{2,16}\\.[a-zA-Z]{2,3}$", value);
        System.out.println("테스트: " + result);
    }

    @Test
    public void fullname_test() throws Exception {
        String value = "User";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{2,20}$", value);
        System.out.println("테스트: " + result);
    }

    @Test
    public void account_gubun_test() throws Exception {
        String value = "DEPOSIT";
        boolean result = Pattern.matches("^(DEPOSIT)$", value);
        System.out.println("테스트: " + result);
    }

    @Test
    public void account_phone_test() throws Exception {
        String value = "01098769147";
        boolean result = Pattern.matches("^(01)[0-9]{9}$", value);
        System.out.println("테스트: " + result);
    }
}
