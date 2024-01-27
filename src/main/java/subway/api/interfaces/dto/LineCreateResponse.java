package subway.api.interfaces.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.domain.model.entity.Line;
import subway.api.domain.model.vo.StationInfo;
import subway.common.mapper.ModelMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineCreateResponse {

	private Long id;
	private String name;
	private String color;
	private List<StationInfo> stationInfo;

	public static LineCreateResponse from(Line line) {
		return ModelMapperBasedVoMapper.convert(line, LineCreateResponse.class);
	}
}
