//package com.bamboo.controller;
//
//import com.bamboo.entity.Visit;
//import com.bamboo.service.VisitService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@RequiredArgsConstructor
//public class GraphController {
//
//    private final VisitService visitService;
//
//    @GetMapping("/graph")
//    public String showGraph(Model model) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        List<Visit> visits = visitService.getLastWeekVisits();
//        List<String> visitData = visits.stream()
//                .map(visit -> String.format("{\"date\": \"%s\", \"count\": %d}", visit.getDate().format(formatter), visit.getVisitNum()))
//                .collect(Collectors.toList());
//
//        model.addAttribute("visitData", visitData.toString());
//        return "graph";
//    }
//}

package com.bamboo.controller;

import com.bamboo.entity.Visit;
import com.bamboo.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GraphController {

    private final VisitService visitService;

    @GetMapping("/graph")
    public String showGraph(@RequestParam(required = false, defaultValue = "week") String period, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Visit> visits;

        switch (period) {
            case "month":
                visits = visitService.getLastMonthVisits();
                break;
            case "year":
                visits = visitService.getLastYearVisits();
                break;
            case "week":
            default:
                visits = visitService.getLastWeekVisits();
                break;
        }

        List<String> visitData = visits.stream()
                .map(visit -> String.format("{\"date\": \"%s\", \"count\": %d}", visit.getDate().format(formatter), visit.getVisitNum()))
                .collect(Collectors.toList());

        model.addAttribute("visitData", visitData.toString());
        model.addAttribute("period", period);
        return "admin/graph";
    }
}

