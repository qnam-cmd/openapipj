package org.spring.openapipj.open.weather.controller;

import lombok.RequiredArgsConstructor;
import org.spring.openapipj.open.weather.service.WeatherService;
import org.spring.openapipj.open.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/open/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @Value("${open.openWeatherMap.serviceKey}")
    private String key;

    @GetMapping("/search/{q}")
    public ResponseEntity<?> search(@PathVariable("q")String q) {
        // URL
        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q="+q+"&appid="+key;

        // Header
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");  // JSON 데이터

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
        System.err.println(responseBody+"<<< responseBody");

        // JSOn -> JAVA 연결 -> DTO에 추가 -> Entity -> DB저장
        weatherService.insertWeather(responseBody);

        Map<String, String> weather = new HashMap<>();
                weather.put("weather", responseBody);   //"weather"이름으로
        return ResponseEntity.status(HttpStatus.OK).body(weather);
    }
}
