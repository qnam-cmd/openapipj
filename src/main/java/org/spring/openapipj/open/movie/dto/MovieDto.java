package org.spring.openapipj.open.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private Long id;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String prdtYear;
    private String repGenreNm;
    private String repNationNm;
    private String typeNm;
    private String companyCd;
    private String companyNm;
    private String openDt;
    private String genreAlt;
    private String prdtStatNm;
    private String source;
    private String totCnt;
}
