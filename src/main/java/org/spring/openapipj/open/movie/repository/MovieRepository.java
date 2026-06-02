package org.spring.openapipj.open.movie.repository;

import org.spring.openapipj.open.movie.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByMovieCd(String movieCd);
}
