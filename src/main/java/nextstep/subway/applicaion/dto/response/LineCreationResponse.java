package nextstep.subway.applicaion.dto.response;

import nextstep.subway.domain.Entity.Section;

import java.time.LocalDateTime;
import java.util.List;

public class LineCreationResponse {

	private Long id;
	private String name;
	private String color;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private List<Section> stations;

	public LineCreationResponse(
					Long id,
					String name,
					String color,
					LocalDateTime createdDate,
					LocalDateTime modifiedDate,
					List<Section> stations
	) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.stations = stations;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
	}
}
