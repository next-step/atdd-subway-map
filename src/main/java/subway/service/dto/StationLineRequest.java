package subway.service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StationLineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private BigDecimal distance;
}
