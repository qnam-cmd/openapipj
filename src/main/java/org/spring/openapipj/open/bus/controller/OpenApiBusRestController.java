package org.spring.openapipj.open.bus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.openapipj.open.bus.dto.busList.BusDto;
import org.spring.openapipj.open.bus.dto.busStation.BusStationDto;
import org.spring.openapipj.open.bus.service.BusService;
import org.spring.openapipj.open.bus.service.BusStationService;
import org.spring.openapipj.open.util.OpenApiBusUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/open/bus")
@RequiredArgsConstructor
@Log4j2
public class OpenApiBusRestController {
    private final BusService busService;
    private final BusStationService busStationService;

    @Value("${open.data.bus.serviceKey}")
    private String key;

    @GetMapping("/busList")
    public ResponseEntity<?> busList(@RequestParam(required = false)String searchVal) throws IOException {
        // 공공데이터 API호출
        String busListResult = OpenApiBusUtil.getBusList(key, searchVal);
        log.info("==" + busListResult + "==");
        // DB저장
        busService.insertBusList(busListResult);
        // DB저장 정보 get
        List<BusDto> busList2 = busService.busListFn(searchVal);
        // 반환
        Map<String ,Object> busMap = new HashMap<>();
        busMap.put("busList",busList2);
        return ResponseEntity.status(HttpStatus.OK).body(busMap);
    }

    @GetMapping("/stationList")
    public ResponseEntity<?> stationList(@RequestParam(name = "busRouteId") String busRouteId) throws IOException {
        // 1. 공공데이터 API호출(정류장 목록용 유틸 메서드 호출)
        String stationResult = OpenApiBusUtil.getStationList(key, busRouteId);
        log.info("== 외부 API정류장 데이터 수신 완료 ==");
        // 2. 파싱 및 DB 저장
        busStationService.insertBusStations(stationResult);
        // 3. DB에서 정류장목록 가져오기
        List<BusStationDto> stationList = busStationService.busStationListFn(busRouteId);
        // 반환
        Map<String ,Object> resultMap = new HashMap<>();
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

}
