package com.example.springjunitpractice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjunitpractice.config.auth.PrincipalDetails;
import com.example.springjunitpractice.dto.ResponseDto;
import com.example.springjunitpractice.dto.transaction.TransactionRespDto.TransactionListRespDto;
import com.example.springjunitpractice.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/s/account/{number}/transaction")
    public ResponseEntity<?> findTransactionList(@PathVariable Long number,
            @RequestParam(value = "gubun", defaultValue = "ALL") String gubun,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal PrincipalDetails principal) {
        TransactionListRespDto dto = transactionService.입출금내역보기(principal.getUser().getId(), number, gubun, page);
        return new ResponseEntity<>(new ResponseDto<>(1, "입출금내역 조회가 완료되었습니다.", dto), HttpStatus.OK);
    }
}
