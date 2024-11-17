package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.repository.MemberRepository;
import com.ifortex.bookservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    @Autowired
    public MemberServiceImpl(BookRepository bookRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Member findMember() {
        Book oldestBook = bookRepository.findAll().stream()
                .filter(book -> book.getGenres().contains("Romance"))
                .min(Comparator.comparing(Book::getPublicationDate))
                .orElse(null);
        return memberRepository.findAll().stream()
                .filter(member -> member.getBorrowedBooks().contains(oldestBook))
                .max(Comparator.comparing(Member::getMembershipDate))
                .orElse(null);
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll().stream()
                .filter(member -> member.getMembershipDate().isAfter(LocalDateTime.of(2023, 1, 1, 0, 0))
                        && member.getMembershipDate().isBefore(LocalDateTime.of(2023, 12, 31, 23, 59, 59)))
                .filter(member -> member.getBorrowedBooks().isEmpty())
                .toList();
    }
}
