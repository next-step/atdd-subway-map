package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class LineRequest {
	private String name;
	private String color;

	public Line toLine() {
		return new Line(this.name, this.color);
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

}
