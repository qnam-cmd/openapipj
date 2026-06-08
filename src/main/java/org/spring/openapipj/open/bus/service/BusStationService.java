package org.spring.openapipj.open.bus.service;

import org.spring.openapipj.open.bus.dto.busStation.BusStationDto;
import java.util.List;

public interface BusStationService {
    public void insertBusStations(String busStationList);
    List<BusStationDto> busStationListFn(String busRouteId);
}
