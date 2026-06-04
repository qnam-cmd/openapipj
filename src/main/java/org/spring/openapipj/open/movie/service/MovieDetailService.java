package org.spring.openapipj.open.movie.service;

import org.spring.openapipj.open.movie.dto.movie.MovieDetailDto;

public interface MovieDetailService {


    void movieInfoResultFn(String responseBody);

    MovieDetailDto movieInfoJava(String movieCd);
}
