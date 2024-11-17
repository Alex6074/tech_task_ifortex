package com.ifortex.bookservice.controller;

import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(@Qualifier("memberServiceImpl") MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("amateur")
    public Member findMember() {
        return memberService.findMember();
    }

    @GetMapping
    public List<Member> findMembers() {
        return memberService.findMembers();
    }
}
