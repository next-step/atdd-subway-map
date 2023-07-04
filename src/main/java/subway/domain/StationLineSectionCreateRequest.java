package subway.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StationLineSectionCreateRequest {
	private Long upStationId;
	private Long downStationId;
	private BigDecimal distance;
}
