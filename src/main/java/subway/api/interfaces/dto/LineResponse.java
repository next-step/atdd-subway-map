package subway.api.interfaces.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class LineResponse {

	private Long id;
	private String name;
	private String color;
	private List<StationInfo> stations;

	public static LineResponse from(Line line) {
		LineResponse response = ModelMapperBasedVoMapper.convert(line, LineResponse.class);
		response.setStations(parseStations(line));
		return response;
	}

	private static List<StationInfo> parseStations(Line line) {
		return line.getLinks().stream()
			.flatMap(link -> Stream.of(StationInfo.fromUpStation(link), StationInfo.fromDownStation(link)))
			.distinct()
			.collect(Collectors.toList());
	}


}
