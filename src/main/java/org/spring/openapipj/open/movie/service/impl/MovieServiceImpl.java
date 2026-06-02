package org.spring.openapipj.open.movie.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.openapipj.open.movie.dto.MovieDto;
import org.spring.openapipj.open.movie.dto.MovieItem;
import org.spring.openapipj.open.movie.dto.MovieListResponse;
//import org.spring.openapipj.open.movie.repository.MovieDetailRepository;
import org.spring.openapipj.open.movie.repository.MovieRepository;
import org.spring.openapipj.open.movie.service.MovieService;
import org.spring.openapipj.open.movie.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
//    private final MovieDetailRepository movieDetailRepository;

    @Override
    public List<MovieDto> insertResponseBody(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        MovieListResponse movieListResponse = null;
        try {
            // JSON 문자열을 Java 객체로 변환
            movieListResponse = objectMapper.readValue(responseBody, MovieListResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(movieListResponse == null || movieListResponse.getMovieListResult()== null){
            return null;
        }
        List<MovieItem> movieItems = movieListResponse.getMovieListResult().getMovieList();
        AtomicReference<String> companyCd = new AtomicReference<>("");
        AtomicReference<String> companyNm = new AtomicReference<>("");
        for (MovieItem movieItem : movieItems) {
            // 회사정보가 여러개일 경우 마지막 값을 사용
            movieItem.getCompanys().forEach(company -> {
                companyCd.set(company.getCompanyCd());
                companyNm.set(company.getCompanyNm());
            });
            Optional<MovieEntity> movieEntity1 = movieRepository.findByMovieCd(movieItem.getMovieCd());
            if(!movieEntity1.isPresent()) {
                System.out.println("DEBUG: movieItem의 영화 제목 확인 -> " + movieItem.getMovieNm());
                MovieEntity movieEntity = MovieEntity.builder()
                        .movieCd(movieItem.getMovieCd())
                        .movieNm(movieItem.getMovieNm())
                        .movieNmEn(movieItem.getMovieNmEn())
                        .prdtYear(movieItem.getPrdtYear())
                        .repNationNm(movieItem.getRepNationNm())
                        .typeNm(movieItem.getTypeNm())
                        .openDt(movieItem.getOpenDt())
                        .genreAlt(movieItem.getGenreAlt())
                        .prdtStatNm(movieItem.getPrdtStatNm())
                        .getCompanyCd(companyCd.get())
                        .getCompanyNm(companyNm.get())
                        .build();
                System.out.println("데이터 저장 시도 중...");
                movieRepository.save(movieEntity);
            }
        }
        List<MovieEntity> movieEntities = movieRepository.findAll();
        if(movieEntities.isEmpty()) {
            throw new NullPointerException("목록이 없습니다.");
        }
        String totCnt = movieListResponse.getMovieListResult().getTotCnt();
        String source = movieListResponse.getMovieListResult().getSource();
        return movieEntities.stream().map(el->
                MovieDto.builder()
                        .id(el.getId())
                        .movieCd(el.getMovieCd())
                        .movieNm(el.getMovieNm())
                        .movieNmEn(el.getMovieNmEn())
                        .prdtYear(el.getPrdtYear())
                        .repGenreNm(el.getRepGenreNm())
                        .repNationNm(el.getRepNationNm())
                        .typeNm(el.getTypeNm())
                        .openDt(el.getOpenDt())
                        .genreAlt(el.getGenreAlt())
                        .prdtStatNm(el.getPrdtStatNm())
                        .getCompanyCd(el.getGetCompanyCd())
                        .getCompanyNm(el.getGetCompanyNm())
                        .source(source) //영화사이름
                        .totCnt(totCnt) // 총제작편수
                .build()).toList();

    }

    @Override
    public MovieDto movieInfoJava(String movieCd) {
        return null;
    }

    @Override
    public Page<MovieDto> getMovieList(Pageable pageable, String subject, String search) {
        return null;
    }
}
