package org.travel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceNodeData {
    private Long id;
    private String locationId;
    private String name;
    private boolean subscribable;
}
