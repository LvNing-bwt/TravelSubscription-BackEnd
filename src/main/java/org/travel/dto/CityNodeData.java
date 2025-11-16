package org.travel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityNodeData {
    private Long id;
    private String locationId;
    private String name;
    private boolean subscribable;
}
