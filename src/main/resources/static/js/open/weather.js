const search = document.querySelector("#search")
const city = document.querySelector(".city .con")
const temp_min = document.querySelector(".temp_min .con")
const temp_max = document.querySelector(".temp_max .con")
const sunrise = document.querySelector(".sunrise .con")
const sunset = document.querySelector(".sunset .con")

// OpenWeather API Key 보안문제때문에 자바스크립트에서는 테스트로만쓰자
const key = `420a12d0bb4330371d33cb26b0b89588`;


const weatherSearchFn=()=> {
    const appURL = `https://api.openweathermap.org/data/2.5/weather?q=${search.value}&appid=${key}`;

    fetch(appURL)
        .then(res=> res.json())
        .then(rs=> {
            console.log(rs)
            console.log(rs.name)
            city.innerText=rs.name
            temp_max.innerText=Math.round(rs.main.temp_max-272.15).toFixed(2);
            temp_min.innerText=Math.round(rs.main.temp_min-272.15).toFixed(2);
            sunrise.innerText=new Date(rs.sys.sunrise*1000).toLocaleString();
            sunset.innerText=new Date(rs.sys.sunset*1000).toLocaleString();

            const lat = rs.coord.lat;
            const lon = rs.coord.lon;
            weatherMapFn(lat,lon);

        })
        .catch(err=>alert(err));
}

function weatherMapFn(lat,lon) {
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(lat, lon), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

// 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption);

}


// 즉시 실행함수
(()=>{
    weatherSearchFn();
})();