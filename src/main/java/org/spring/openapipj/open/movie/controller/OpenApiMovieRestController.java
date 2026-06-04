package org.spring.openapipj.open.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.openapipj.open.movie.dto.MovieDto;
import org.spring.openapipj.open.movie.dto.boxOffice.BoxOfficeDto;
import org.spring.openapipj.open.movie.service.BoxOfficeService;
import org.spring.openapipj.open.movie.service.MovieDetailService;
import org.spring.openapipj.open.movie.service.MovieService;
import org.spring.openapipj.open.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/open/movie")
@RequiredArgsConstructor
public class OpenApiMovieRestController {

    private final BoxOfficeService boxOfficeService;
    private final MovieService movieService;
    private final MovieDetailService movieDetailService;

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

    //별도의 테이블에 저장
    @GetMapping("/movieDetail/{movieCd}")
    public ResponseEntity<?> movieDetail(@PathVariable("movieCd") String movieCd) {
        String apiURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=" + key
                + "&movieCd=" + movieCd;
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-type", "application/json"); //JSON데이터

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
        System.out.println(responseBody+" <<<responseBody");

        movieDetailService.movieInfoResultFn(responseBody);
        Map<String, String> movie = new HashMap<>();
        movie.put("movie", responseBody);

        System.out.println(movie+" <<< movie");

        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    // DB데이터 get -> 기존에  movieList 테이블리스트 중에서 찾기
    @GetMapping("/movieDetailJava/{movieCd}")
    public ResponseEntity<?> movieDetailJava(@PathVariable("movieCd") String movieCd) {
//        MovieDetailDto movieDetailDto= movieDetailService.movieInfoJava(movieCd);
        MovieDto movieDto= movieService.movieInfoJava(movieCd);
        System.out.println(movieDto+" <<< movieDto");
        Map<String, MovieDto> movie = new HashMap<>();
        movie.put("movie", movieDto);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @GetMapping("/boxOffice/{targetDt}/{weekGb}")
    public ResponseEntity<?> boxOfficeJava(@PathVariable("targetDt") String targetDt,
                                           @PathVariable("weekGb")String weekGb) {

        log.info("===========targetDt: " + targetDt + "=============");
        log.info("===========weekGb: " + weekGb + "=============");
        String movieSearch = "searchWeeklyBoxOfficeList.json";  // 가져올내용
        String apiURL = "https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/" + movieSearch + "?key=" + key
                + "&targetDt="+ targetDt + "&weekGb=" + weekGb;
        Map<String ,String > requestHeaders = new HashMap<>();
        requestHeaders.put("Content-Type","application/json");  // JSON 데이터

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);

        List<BoxOfficeDto> boxOfficeResult = boxOfficeService.insertBoxOfficeBody(responseBody);

        Map<String ,List<BoxOfficeDto>> map = new HashMap<>();
        map.put("boxOfficeResult", boxOfficeResult);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // DB데이터 get
    @GetMapping("/boxOfficeDetailJava/{movieCd}")
    public ResponseEntity<?> boxOfficeDetailJava(@PathVariable("movieCd")String movieCd) {
        BoxOfficeDto boxOfficeDto = boxOfficeService.boxOfficeMovieInfoJava(movieCd);
        Map<String ,BoxOfficeDto> movie = new HashMap<>();
        movie.put("movie", boxOfficeDto);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

}
