package org.spring.openapipj.open.bus.service;

import org.spring.openapipj.open.bus.dto.busList.BusDto;

import java.util.List;

public interface BusService {
    void insertBusList(String busListResult);
    List<BusDto> busListFn(String searchVal);
}
