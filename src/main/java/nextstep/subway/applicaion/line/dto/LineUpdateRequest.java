package nextstep.subway.applicaion.line.dto;

public class LineUpdateRequest {
	private String name;
	private String color;

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public LineUpdateRequest(String name, String color) {
		this.name = name;
		this.color = color;
	}
}
