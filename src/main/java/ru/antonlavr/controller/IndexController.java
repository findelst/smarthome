package ru.antonlavr.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.antonlavr.model.Birthday;
import ru.antonlavr.model.News;
import ru.antonlavr.model.PhoneBalance;
import ru.antonlavr.model.Weather;
import ru.antonlavr.repository.BirthdayRepository;
import ru.antonlavr.repository.NewsRepository;
import ru.antonlavr.repository.PhoneBalanceRepository;
import ru.antonlavr.repository.WeatherRepository;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    WeatherRepository weatherRepository;
    @Autowired
    PhoneBalanceRepository phoneBalanceRepository;
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    BirthdayRepository birthdayRepository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("weathers", (List<Weather>) weatherRepository.findByCurrent(true));
        model.addAttribute("phoneBalances", (List<PhoneBalance>) phoneBalanceRepository.findByCurrent(true));
        model.addAttribute("news", (List<News>) newsRepository.findAll());
        model.addAttribute("birthdayList", (List<Birthday>) birthdayRepository.findAll());

        return "index";
    }

}
