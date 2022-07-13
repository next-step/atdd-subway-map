package nextstep.subway.applicaion.dto;

public class LineResponse {
	private Long id;
	private String name;
	private String color;

	protected LineResponse() {
	}

	public LineResponse(Long id, String name, String color) {

		this.id = id;
		this.name = name;
		this.color = color;

	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

}
