package nextstep.subway.applicaion.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<SectionResponse> sections;
}
