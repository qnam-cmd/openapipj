package org.spring.openapipj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/open")
public class OpenApiJavaController {

    @Value("${kakao.map.appkey}")
    private String kakaoMapKey;

    @GetMapping("/weather")
    public String weather(Model model) {
        model.addAttribute("kakaoMapKey",kakaoMapKey);
        return "/open/api/weather";
    }

    @GetMapping("/movie")
    public String movie(Model model) {
        return "/open/api/movie";
    }

    @GetMapping("/bus")
    public String bus(Model model) { return "/open/api/bus";}

}
