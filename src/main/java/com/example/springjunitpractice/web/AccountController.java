package com.example.springjunitpractice.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.dto.ResponseDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountSaveReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.springjunitpractice.dto.account.AccountReqDto.AccountWithdrawReqDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountDepositRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountDetailsRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountListRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountSaveRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountTransferRespDto;
import com.example.springjunitpractice.dto.account.AccountRespDto.AccountWithdrawRespDto;
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

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number,
            @AuthenticationPrincipal PrincipalDetails principal) {
        accountService.계좌삭제(number, principal.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌가 삭제되었습니다.", null), HttpStatus.OK);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto,
            BindingResult bindingResult) {
        AccountDepositRespDto dto = accountService.계좌입금(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌에 입금이 완료되었습니다.", dto), HttpStatus.CREATED);
    }

    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody @Valid AccountWithdrawReqDto AccountWithdrawReqDto,
            BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal) {
        AccountWithdrawRespDto dto = accountService.계좌출금(principal.getUser().getId(), AccountWithdrawReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 출금이 완료되었습니다.", dto), HttpStatus.OK);
    }

    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transferAccount(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto,
            BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principal) {
        AccountTransferRespDto dto = accountService.계좌이체(principal.getUser().getId(), accountTransferReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 이체가 완료되었습니다.", dto), HttpStatus.OK);
    }

    @GetMapping("/s/account/{number}")
    public ResponseEntity<?> findAccountDetails(@PathVariable Long number,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal PrincipalDetails principal) {
        AccountDetailsRespDto dto = accountService.계좌상세보기(number, principal.getUser().getId(), page);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세보기가 조회되었습니다.", dto), HttpStatus.OK);
    }
}
