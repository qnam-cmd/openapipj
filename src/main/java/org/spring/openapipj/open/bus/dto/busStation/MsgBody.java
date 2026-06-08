package org.spring.openapipj.open.bus.dto.busStation;

import lombok.Data;
import org.spring.openapipj.open.bus.dto.busList.BusItem;

import java.util.List;

@Data
public class MsgBody {
    private List<BusItemStation> itemList;
}
