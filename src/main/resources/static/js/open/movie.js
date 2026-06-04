const sceneList = document.querySelector('.scene-list');
const sceneContent = document.querySelector('.scene-content');
const tTable = document.querySelector('.t-table');
const key = "9a0cd6d0a47cef6a190b009d6d462797"; // 인증키
const movieSearch = "searchMovieList.json"; // 가져올 내용
const itemPerPage = "10";   // 가져올 갯수
const openStartDt = 2026; // 년도

const sceneListFn=()=> {
    const apiURL = `http://kobis.or.kr/kobisopenapi/webservice/rest/movie/${movieSearch}?key=${key}&itemPerPage=${itemPerPage}&openStartDt=${openStartDt}`;
    fetch(apiURL)
        .then(res=>res.json())
        .then(rs=> {
            console.log(rs);
            // 영화 목록
            const movieListResult = rs.movieListResult;
            let html = ``;
            movieListResult.movieList.forEach(el=> {
                html += `
                    <tr>
                        <td>${el.movieNm}</td>
                        <td>${el.openDt}</td>
                        <td>${el.genreAlt}</td>
                        <td>${el.nationAlt}</td>
                        <td>${el.prdtStatNm}</td>
                        <td>${el.movieCd}</td>
                        <td><span onclick="sceneDetailFn(${el.movieCd})">${el.movieNm}</span></td>
                    </tr>
                `;
            })
            html += `
                <tr><td colspan="7" >출처: ${movieListResult.source}, 총편수: ${movieListResult.totCnt}</td></tr>
            `;  // tTable.innerHTML = html;
            sceneContent.innerHTML = html;
        })
        .catch(err=>console.log(err));
}

// 영화 상세조회
const sceneDetailFn =(movieCd)=> {
    const apiURL = `http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=${key}&movieCd=${movieCd}`;

    fetch(apiURL)
        .then(res => res.json())
        .then(rs=> {
            document.querySelector('.movie-detail-modal').style.display = 'flex';

            const movieInfoResult = rs.movieInfoResult;
            const movieInfo = movieInfoResult.movieInfo;
            const openDt = movieInfo.openDt;
            const movieNm = movieInfo.movieNm;
            const movieCdVal = movieInfo.movieCd;
            const prdtStatNm = movieInfo.prdtStatNm;
            const showTm = movieInfo.showTm;
            const director = movieInfo.directors.length > 0 ? movieInfo.directors[0].peopleNm : '정보없음';
            const actors = movieInfo.actors.map(actor => actor.peopleNm).join(', ');
            const source = movieInfoResult.source;

            const html1 = `
                <span class="modal-close-btn" onclick="closeModal()">X</span>
                <h1>${movieNm} (${movieCdVal})</h1>
                <ul>
                    <li>개봉일: ${openDt}</li>
                    <li>상태: ${prdtStatNm}</li>
                    <li>상영시간: ${showTm}</li>
                    <li>감독: ${director}</li>
                    <li>배우: ${actors}</li>
                    <li>출처: ${source}</li>
                </ul>
            `;
            // 기존 구조 유지하고 내용만 교체
            document.querySelector('.movie-detail-modal-con').innerHTML = html1;
        })
        .catch(err => alert("영화 상세 정보 로딩중 오류 발생: "+err));
};

const closeModal=()=> {
    document.querySelector('.movie-detail-modal').style.display ='none';
}

// 박스오피스
const boxOfficeFn=()=> {
    // const apiURL=`https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json?key=${key}&targetDt=20260301&weekGb=0`;
    const apiURL = `/api/open/movie/boxOffice/20260301/0`;
    fetch(apiURL)
        .then(res=>res.json())
        .then(rs=>{
            console.log(rs)
            // 컨트롤러에서 map.put("boxOfficeResult", boxOfficeResult) 했으므로 rs.boxOfficeResult로 접근
            const boxOfficeResult = rs.boxOfficeResult;
            let html2 = ``;
            boxOfficeResult.forEach(el=> {
                html2 += `
                    <tr>
                        <td>${el.rank}</td>
                        <td>${el.movieNm}</td>
                        <td>${el.openDt}</td>
                        <td>${el.audiAcc}</td>
                        <td>${el.salesAcc}</td>
                        <td>${el.movieCd}</td>
                        <td><button onclick="sceneDetailFn(${el.movieCd})">보기</button></td>
                    </tr>
                `;
            })
            const html1= `
                <thead>
                    <tr>
                        <td colspan="7"><span>${boxOfficeResult.boxofficeType}</span> . <span>${boxOfficeResult.showRange}</span></td>    
                    </tr>
                </thead>
                <tbody class="scene-content">
                    ${html2}
                </tbody>
            </table>
            `;
            tTable.innerHTML=html1;
        })
        .catch(err=>console.log(err));
}


(()=>{
    sceneListFn();
})();