package subway.api.domain.dto.outport;

import java.util.SortedSet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.domain.model.entity.Line;
import subway.common.mapper.ObjectMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineInfo {

	private Long id;
	private String name;

	private String color;

	private SortedSet<SectionInfo> sections;

	public static LineInfo from(Line line) {
		return ObjectMapperBasedObjectMapper.convert(line, LineInfo.class);
	}
}
