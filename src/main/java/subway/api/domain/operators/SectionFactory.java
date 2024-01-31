package subway.api.domain.operators;

import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Section;
import subway.api.domain.model.entity.Station;
import subway.api.interfaces.dto.LineCreateRequest;
import subway.api.interfaces.dto.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface SectionFactory {
	Section createSection(LineCreateRequest request, Line line);

	Section createSection(SectionCreateRequest request, Line line, Station upStation, Station downStation);

	void deleteByLine(Line line);
}
