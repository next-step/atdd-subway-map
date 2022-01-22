package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class LineRequest {
    private String name;
    private String color;

	private LineRequest() {
	}

	private LineRequest(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

	public Line toEntity() {
		return Line.of(name, color);
	}
}
