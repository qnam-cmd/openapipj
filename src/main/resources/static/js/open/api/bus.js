const search = document.querySelector('#search');
const bus1 = document.querySelector('#bus1');
const busStation = document.querySelector('.bus-station-con');

// 1. 버스노선 검색
const busSearchFn=()=> {
    const searchVal = search.value.trim();
    if(!searchVal) return alert("버스노선을 입력하세요.");
    const apiURL = `/api/open/bus/busList?searchVal=${searchVal}`;
    fetch(apiURL)
        .then(res => res.json())
        .then(data=>{
            console.log(data);
            const itemList = data.itemList;
            if(!itemList || itemList.length === 0) {
                bus1.innerHTML = "<tr><td colspan='9' style='text-align:center;'>검색 결과가 없습니다.</td></tr>";
                return;
            }let html1 = ``;
            itemList.forEach(el => {
                // 주의: el.busRouteNm 등의 변수명은 Java의 BusDto 필드명과 완벽히 일치해야 합니다.
                html1 += `
                    <tr>
                        <td>${el.busRouteNm}</td>
                        <td>${el.routeType}</td>
                        <td>${el.stStationNm}</td>
                        <td>${el.edStationNm}</td>
                        <td>${el.firstBusTm}</td>
                        <td>${el.lastBusTm}</td>
                        <td>${el.term}</td>
                        <td><button onclick="stationListFn('${el.busRouteId}')">정류장 정보</button></td>
                        <td>${el.corpNm}</td>
                    </tr>
                `;
            });
            bus1.innerHTML = html1;
        })
        .catch(err => console.error("버스 노선 검색 에러:", err));
}

// 2. 버스 정류장 목록 검색
const stationListFn=(busRouteId)=>{
    const apiURL = `/api/open/bus/stationList?busRouteId=${busRouteId}`;
    fetch(apiURL)
        .then(res=>res.json())
        .then(data=> {
            console.log(data);
            const itemList = data.itemList;
            if(!itemList || itemList.length === 0) {
                busStationCon.innerHTML = "<p>정류장 정보가 없습니다.</p>";
                return;
            }

            let html1 = `<ul>`;
            itemList.forEach(el => {
                html1 += `
                    <li style="cursor:pointer" onclick="stationOne('${el.stationNm}', ${el.gpsX}, ${el.gpsY})"> 
                        ${el.stationNm}
                    </li>
                `;
            });
            html1 += `</ul>`;
            busStationCon.innerHTML = html1;

            // 카카오 지도에 정류장 마커 표시
            kakaoMapBusStationList(itemList);
        })
        .catch(err => console.error("정류장 목록 불러오기 에러:", err));

}

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


    // 모달이 열린 직후 지도의 레이아웃을 재계산하고 중심을 다시 잡습니다.
    setTimeout(() => {
        map.relayout();
        map.setCenter(markerPosition);
    }, 10); // 10ms의 아주 짧은 딜레이를 주어 화면 렌더링 후 실행되게 함


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
    const map = new kakao.maps.Map(mapContainer, mapOption);
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



const closeModal = (e) => {
    busModal.classList.remove("show");
};