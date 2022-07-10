package nextstep.subway.applicaion.dto;

import java.util.List;

public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<StationResponse> stations;
}