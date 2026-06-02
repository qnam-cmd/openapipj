package org.spring.openapipj.open.movie.controller;

import lombok.RequiredArgsConstructor;
import org.spring.openapipj.open.movie.dto.MovieDto;
import org.spring.openapipj.open.movie.service.MovieService;
import org.spring.openapipj.open.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/open/movie")
@RequiredArgsConstructor
public class OpenApiMovieRestController {
    private final MovieService movieService;
//    private final MoveDetailService moveDetailService;

    @Value("${open.kobis.serviceKey}")
    private String key;

    @GetMapping("/movieList")
    public ResponseEntity<?> movieList() {
        String movieSearch = "searchMovieList.json";    // 가져올 내용
        String itemPerPage = "100"; // 가져올 갯수
        String openStartDt = "2026"; // 연도
        String apiURL = "http://kobis.or.kr/kobisopenapi/webservice/rest/movie/" + movieSearch + "?key=" + key
            + "&itemPerPage=" + itemPerPage + "&openStartDt=" + openStartDt;
        Map<String ,String > requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");  //JSON데이터
        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
        List<MovieDto> movieDtos = movieService.insertResponseBody(responseBody);

        System.out.println(movieDtos+"movieDtos");

        Map<String ,List<MovieDto>> movie = new HashMap<>();
        movie.put("movie",movieDtos);
        return ResponseEntity.status(HttpStatus.OK).body(movie);

    }
}
