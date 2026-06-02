package org.spring.openapipj.open.weather.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather06")
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="weather_id")
    private Long id;

    private String name;
    private String lat;         // 위도
    private String lon;         // 도
    private String country;     // 국가
    private String temp_min;    // 최저온도
    private String temp_max;    // 최고온도
    private String icon;        // 이미지 아이콘

}
