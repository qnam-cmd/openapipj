package org.spring.openapipj.open.movie.service;

import org.spring.openapipj.open.movie.dto.boxOffice.BoxOfficeDto;

import java.util.List;

public interface BoxOfficeService {
    List<BoxOfficeDto> insertBoxOfficeBody(String boxOfficeBody);
    BoxOfficeDto boxOfficeMovieInfoJava(String movieCd);
}
