package com.example.springjunitpractice.dto.user;

import com.example.springjunitpractice.domain.user.User;

import lombok.Getter;

public class UserRespDto {

    @Getter
    public static class JoinRespDto {

        private Long id;
        private String username;
        private String fullname;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }

}
