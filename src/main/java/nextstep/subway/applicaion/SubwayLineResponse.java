package nextstep.subway.applicaion;

import lombok.Getter;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayLine;

import java.util.List;

@Getter
public class SubwayLineResponse {
	private Long id;
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Integer distance;
	private List<Station> stations;


	public SubwayLineResponse(SubwayLine savedLine, List<Station> stations) {
		this.id = savedLine.getId();
		this.name = savedLine.getName();
		this.color = savedLine.getColor();
		this.upStationId = savedLine.getUpStationId();
		this.downStationId = savedLine.getDownStationId();
		this.distance = savedLine.getDistance();
		this.stations = stations;
	}
}
