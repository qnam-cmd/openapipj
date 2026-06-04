package org.spring.openapipj.open.movie.repository;

import org.spring.openapipj.open.movie.entity.BoxOfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxOfficeRepository extends JpaRepository<BoxOfficeEntity,Long> {
    List<BoxOfficeEntity> findByMovieCd(String movieCd);
}
