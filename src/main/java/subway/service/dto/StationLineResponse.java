package subway.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import subway.domain.StationLine;

@Data
public class StationLineResponse {
	private Long id;
	private String name;
	private String color;
	private List<StationResponse> stations;

	public static StationLineResponse fromEntity(StationLine stationLine) {
		if (Objects.isNull(stationLine)) {
			return null;
		}

		final List<StationResponse> stations = new ArrayList<>();

		stations.add(StationResponse.fromEntity(stationLine.getUpStation()));
		stations.add(StationResponse.fromEntity(stationLine.getDownStation()));

		final StationLineResponse response = new StationLineResponse();

		response.setId(stationLine.getLineId());
		response.setName(stationLine.getName());
		response.setColor(stationLine.getColor());
		response.setStations(stations);

		return response;
	}
}
