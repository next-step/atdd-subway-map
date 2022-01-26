package nextstep.subway.applicaion.dto.response;

import java.time.LocalDateTime;

public class LineCreationResponse {

	private Long id;
	private String name;
	private String color;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public LineCreationResponse(
					Long id,
					String name,
					String color,
					LocalDateTime createdDate,
					LocalDateTime modifiedDate
	) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
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
