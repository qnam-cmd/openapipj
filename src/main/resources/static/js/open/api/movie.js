// =====================================  박스오피스 Fn  ============================================== //

const boxOfficeFn2 =()=> {
    let apiURL = "";
    const dateInputEl = document.getElementById("targetDt");
    const weekGbEl = document.querySelector("input[name='weekGb']:checked");
    // 오늘 날짜 구하기
    const getToday =()=> {
        const today = new Date();
        const yyyy = today.getFullYear();   // 연도 2025
        const mm = String(today.getMonth()+1).padStart(2,'0');  // 9 -> 09
        const dd = String(parseInt(today.getDate())-7).padStart(2,'0'); // 9 -> 09
        return `${yyyy}${mm}${dd}`;
    };
    // 요소가 없을 경우 기본값으로 요청
    if (!dateInputEl || !weekGbEl) {
        apiURL = `/api/open/movie/boxOffice/${getToday()}/0`;   // 오늘날짜, 주간(0) 기본
    } else {
        const dateInputVal = dateInputEl.value;
        const weekGb = weekGbEl.value;
        if (!dateInputVal) {
            alert("날짜를 선택해주세요");
            return;
        }
        const targetDt = dateInputVal.replaceAll("-","");
        apiURL = `/api/open/movie/boxOffice/${targetDt}/${weekGb}`;
    }
    // 나머지 fetch 코드는 동일하게 유지
    fetch(apiURL)
        .then(res=>res.json())
        .then(rs=> {
            const boxOfficeResult = rs.boxOfficeResult;
            if (!boxOfficeResult || boxOfficeResult.length ===0) {
                alert("조회된 박스오피스 데이터가 없습니다.");
                return;
            }
            let html2 = "";
            boxOfficeResult.forEach(el => {
                html2 += `
                    <tr>
                        <td>${el.rank}</td>
                        <td>${el.movieNm}</td>
                        <td>${el.openDt}</td>
                        <td>${Number(el.audiAcc).toLocaleString()}</td>
                        <td>${Number(el.salesAcc).toLocaleString()}</td>
                        <td>${el.movieCd}</td>
                        <td><button onclick="boxOfficeDetailFn('${el.movieCd}')">보기</button></td>
                    </tr>
                `;
            });
            const html1 = `
                <thead>
                    <tr>
                        <td colspan="7" " style="text-align:right;padding: 1vmin; box-sizing:border-box">
                            <div class="box-office-form" style="margin-bottom: 20px;">
                                <label for="targetDt">날짜선택(YYYY-MM-DD):</label>
                                <input type="date" id="targetDt">
                                <label style="margin-left: 20px;" >주간 구분</label>
                                <label><input type="radio" name="weekGb" value="0">주간</label>
                                <label><input type="radio" name="weekGb" value="1">주말</label>
                                <label><input type="radio" name="weekGb" value="2">평일</label>
                                <button onclick="boxOfficeFn2()" style="margin-left: 20px">조회</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="7" style="text-align:right;padding: 1vmin; box-sizing:border-box">
                        <span>${boxOfficeResult[0].boxofficeType}</span> · <span>${boxOfficeResult[0].showRange}</span>
                        </td>
                    </tr>
                     <tr>
                         <th>순위</th>
                         <th>제목</th>
                         <th>개봉일</th>
                         <th>누적관객수</th>
                         <th>누적매출액</th>
                         <th>영화코드</th>
                         <th>보기</th>
                     </tr>
                </thead>
                <tbody class="scene-content">
                    ${html2}
                </tbody>
            `;
            const tTable = document.querySelector(".t-table");
            if (tTable) {
                tTable.innerHTML = html1;
            } else {
                console.error("테이블 요소(.t-table)를 찾을 수 없습니다.");
            }
        })
        .catch(err => {
            console.log("API 호출 중 에러:",err);
            alert("박스오피스 데이터를 불러오지 못했습니다.");
        })
}


//영화상세조회
const boxOfficeDetailFn = (movieCd) => {
    const apiURL = `/rest/boxOfficeDetailJava/${movieCd}`;

    fetch(apiURL)
        .then(res => res.json())
        .then(rs => {
            console.log(rs); console.log(rs.movie);
            document.querySelector('.movie-detail-modal').style.display = 'flex';
            const movieStr = rs.movie;
            const movie = movieStr;
            const movieInfoResult = movie;
            const movieInfo = movieInfoResult;

            // 영화 정보 추출
            const movieId = movieInfo.id; //삭제시
            const movieNm = movieInfo.movieNm;
            const movieCdVal = movieInfo.movieCd;
            const openDt = movieInfo.openDt;
            const rank = movieInfo.rank;
            const audiAcc = movieInfo.audiAcc;
            const salesAcc = movieInfo.salesAcc;

            const html1 = `
                <span class="modal-close-btn" onclick="closeModal()">X</span>
                <h1>${movieNm} (${movieCdVal})</h1>
                <ul>
                    <li>개봉일: ${openDt}</li>
                    <li>영화명: ${movieNm}</li>
                    <li>랭킹: ${rank}</li>
                    <li>누적관객수: ${audiAcc}</li>
                    <li>누적매출액: ${salesAcc}</li>
                    <li><button>삭제</button></li>
                </ul>
            `;
            // 기존 구조 유지하고 내용만 교체
            document.querySelector('.movie-detail-modal-con').innerHTML = html1;
        })
        .catch(err => alert("영화 상세 정보 로딩 중 오류 발생: " + err.message));
}

const closeModal=()=> {
    document.querySelector('.movie-detail-modal').style.display = 'none';
}