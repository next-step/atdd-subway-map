package nextstep.subway.applicaion.dto;

public class StationResponse {

	private Long id;
	private String name;
	private String color;

	public StationResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
