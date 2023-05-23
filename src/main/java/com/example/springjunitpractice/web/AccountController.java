package com.example.springjunitpractice.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.dto.ResponseDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountListRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.example.springjunitpractice.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto,
            BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal) {
        AccountSaveRespDto dto = accountService.계좌등록(principal.getUser().getId(), accountSaveReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌가 등록되었습니다.", dto), HttpStatus.CREATED);
    }

    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findUserAccounts(@AuthenticationPrincipal PrincipalDetails principal) {
        AccountListRespDto dto = accountService.계좌목록보기_유저별(principal.getUser().getId(),
                principal.getUser().getFullname());
        return new ResponseEntity<>(new ResponseDto<>(1, "전체 계좌 목록이 조회되었습니다.", dto), HttpStatus.OK);
    }
}
