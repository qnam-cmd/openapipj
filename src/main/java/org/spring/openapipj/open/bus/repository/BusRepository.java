package org.spring.openapipj.open.bus.repository;

import org.spring.openapipj.open.bus.entity.BusEntity;
import org.spring.openapipj.open.bus.entity.BusStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<BusEntity,Long> {
    Optional<BusEntity> findByBusRouteId(String busRouteId);
    List<BusEntity> findByBusRouteNmContaining(String searchVal);
}
