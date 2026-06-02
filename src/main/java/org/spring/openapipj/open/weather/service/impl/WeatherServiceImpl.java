package org.spring.openapipj.open.weather.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.openapipj.open.weather.dto.WeatherApiDto;
import org.spring.openapipj.open.weather.repository.WeatherRepository;
import org.spring.openapipj.open.weather.service.WeatherService;
import org.spring.openapipj.open.weather.entity.WeatherEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    // JSON -> JAVA
    private final ObjectMapper objectMapper;

    @Override
    public void insertWeather(String responseBody) {

        try {
            WeatherApiDto weatherApiDto = objectMapper.readValue(responseBody, WeatherApiDto.class);

            log.info("API 응답 완료:{}", weatherApiDto);
            Optional<WeatherEntity> weatherEntity = weatherRepository.findByName(weatherApiDto.getName());
            // 2. 중복 체크: lat, lon, country, name 기준으로 중복체크
            //
            if(weatherEntity.isEmpty()) {
                WeatherEntity newEntity = WeatherEntity.builder()
                        .name(weatherApiDto.getName())
                        .lat(weatherApiDto.getCoord().getLat())
                        .lon(weatherApiDto.getCoord().getLon())
                        .icon(weatherApiDto.getWeather().get(0).getIcon())
                        .temp_max(weatherApiDto.getMain().getTemp_max())
                        .temp_min(weatherApiDto.getMain().getTemp_min())
                        .country(weatherApiDto.getSys().getCountry())
                        .build();
                weatherRepository.save(newEntity);
                log.info("저장 완료");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("API 응답 에러");
        }
    }
}
