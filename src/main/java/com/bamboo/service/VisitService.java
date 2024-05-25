package com.bamboo.service;

import com.bamboo.config.oauth.MyOAuth2MemberService;
import com.bamboo.entity.Visit;
import com.bamboo.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    private Set<String> visitedEmails = new HashSet<>();

    public void countVisit() {
        String email = getCurrentUserEmail();
        LocalDate today = LocalDate.now();

        if (visitedEmails.contains(email + today.toString())) {
            // 이메일이 이미 오늘 방문한 경우 아무 작업도 하지 않음
            return;
        } else {
            visitedEmails.add(email + today.toString());
        }

        Optional<Visit> visitOptional = visitRepository.findById(today);
        Visit visit = visitOptional.orElseGet(() -> new Visit());
        visit.setDate(today);

        if (visit.getVisitNum() == null) {
            visit.setVisitNum(1L);
        } else {
            visit.setVisitNum(visit.getVisitNum() + 1);
        }

        visitRepository.save(visit);
    }

    private String getCurrentUserEmail() {
        String email;
        if (MyOAuth2MemberService.loginType == null) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            email = authentication.getName();
        } else {
            email = MyOAuth2MemberService.userEmail;
        }
        return email;
    }

    public List<Visit> getLastWeekVisits() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return visitRepository.findByDateBetween(startDate, endDate);
    }

    public List<Visit> getLastMonthVisits() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        return visitRepository.findByDateBetween(startDate, endDate);
    }

    public List<Visit> getLastYearVisits() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);
        return visitRepository.findByDateBetween(startDate, endDate);
    }
}
