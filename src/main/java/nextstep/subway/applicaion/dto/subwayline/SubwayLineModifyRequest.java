package nextstep.subway.applicaion.dto.subwayline;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SubwayLineModifyRequest {
	private String name;
	private String color;
}
