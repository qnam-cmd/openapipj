package org.spring.openapipj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/open")
public class OpenApiController {

    @Value("${kakao.map.appkey}")
    public String kakaoMapKey;

    @GetMapping("/weather")
    public String weather(Model model) {
        model.addAttribute("kakaoMapKey", kakaoMapKey);
        return "/open/weather";
    }

    @GetMapping("/movie")
    public String movie() {
        return "/open/movie";
    }

    @GetMapping("/bus")
    public String bus(Model model) {
        model.addAttribute("kakaoMapKey", kakaoMapKey);
        return "/open/bus";
    }


}
