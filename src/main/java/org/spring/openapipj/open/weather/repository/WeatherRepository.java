package org.spring.openapipj.open.weather.repository;

import org.spring.openapipj.open.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    Optional<WeatherEntity> findByName(String name);
}
