const search = document.querySelector('#search');
const bus1 = document.querySelector('#bus1');
const busStationCon = document.querySelector('.bus-station-con')
// 공공데이터 포털 버스 API 키 (URL인코딩됨)
const ServiceKey = "382bc0a758e0913a27c474518fb0349c4e16e4e59d7ec7f354bca7c327b60419";
// CORS 우회 프록시 (개발용)
// 실제 운영시 백엔드에서 직접 호출하거나 프록시 서버 필요
const proxy = "https://cors-anywhere.herokuapp.com/";
// 버스노선 검색
const busSearchFn = () => {
    const searchVal = search.value.trim();
    if (!searchVal) return alert("노선 번호를 입력하세요.");
    const apiURL = `${proxy}http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList?ServiceKey=${ServiceKey}&strSrch=${searchVal}&resultType=json`;
    fetch(apiURL)
        .then(res=> res.json())
        .then(data=> {
            console.log(data);
            const itemList = data.msgBody.itemList;
            if(!itemList || itemList.length === 0) {
                bus1.innerHTML = "<tr><td colspan='9'>검색 결과가 없습니다.</td></tr>";
                return;
            }
            let html1 = ``;
            itemList.forEach(el => {
                html1 += `
                    <tr>
                        <td>${el.busRouteNm}</td>
                        <td>${el.routeType}</td>
                        <td>${el.stStationNm}</td>
                        <td>${el.edStationNm}</td>
                        <td>${el.firstBusTm}</td>
                        <td>${el.lastBusTm}</td>
                        <td>${el.term}</td>
                        <td onclick="stationListFn('${el.busRouteId}')">정류장 정보</td>
                        <td>${el.corpNm}</td>
                    </tr>
                `;
            })
            bus1.innerHTML = html1;
        }).catch(err=>console.error("버스 노선 검색 에러:",err))
}


const stationListFn=(busRouteId)=>{
    const apiURL = `${proxy}http://ws.bus.go.kr/api/rest/busRouteInfo/getStationByRoute?ServiceKey=${ServiceKey}&busRouteId=${busRouteId}&resultType=json`;
    fetch(apiURL)
        .then(res=>res.json())
        .then(data=> {
            console.log(data);
            const itemList = data.msgBody.itemList;
            if(!itemList || itemList.length === 0) {
                busStationCon.innerHTML = "<p>정류장 정보가 없습니다.</p>";
                return;
            }
            let html1= `<ul>`;
            itemList.forEach(el=> {
                console.log(el.stationNm + ", "+el.gpsX + ", " + el.gpsY)
                html1 += `
                    <li style="cursor:pointer" onclick="stationOne('${el.stationNm}',${el.gpsX}, ${el.gpsY})"> ${el.stationNm}</li>
                `;
            })
            html1 += `</ul>`;
            busStationCon.innerHTML = html1;
            // 카카오 지도에 정류장 마커 표시
            kakaoMapBusStationList(itemList);
        })
        .catch(err=>console.error("정류장 목록 불러오기 에러:",err));
}


const closeModal=(e)=> {
    busModal.classList.remove("show");
}

(()=>{
    busSearchFn();
})();


const busModal = document.querySelector('.bus-detail-modal');
const busModalCon = document.querySelector('.bus-detail-modal .bus-detail-modal-con');
const stationOne = (stNm, gX, gY) => {
    console.log(stNm + "," + gX + "," + gY);
    busModal.classList.add('show');
    document.querySelector('.bus-detail-modal .bus-detail-modal-con h1').innerText = `${stNm}`;
    const mapContainer = document.getElementById('map-view'),   // 지도 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(gY,gX),   // 지도 중심좌표
            level: 3    // 지도확대레벨
        };
    const map = new kakao.maps.Map(mapContainer, mapOption);    // 지도를 생성합니다.
    const markerPosition = new kakao.maps.LatLng(gY, gX);   // 지도의 중심좌표
    const marker = new kakao.maps.Marker({
        position: markerPosition
    })
    marker.setMap(map);
}
// 카카오 지도에 정류장 위치표시
const kakaoMapBusStationList = (itemList) => {
    const positions = itemList.map(el => ({
        title: el.stationNm,
        latlng: new kakao.maps.LatLng(parseFloat(el.gpsY), parseFloat(el.gpsX))
    }))
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: positions[0].latlng,
        level: 5
    };
    const imageSrc = "https://t1.daumCdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
    const imageSize = new kakao.maps.Size(24,35);
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
    positions.forEach(pos => {
        new kakao.maps.Marker({
            map,
            position: pos.latlng,
            title: pos.title,
            image: markerImage
        });
    })
}