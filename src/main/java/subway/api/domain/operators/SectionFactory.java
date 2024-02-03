package subway.api.domain.operators;

import subway.api.domain.dto.inport.LineCreateCommand;
import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Section;
import subway.api.domain.model.entity.Station;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface SectionFactory {

	Section createSection(LineCreateCommand createCommand);

	Section createSection(SectionCreateCommand command, Station upStation, Station downStation);

	void deleteByLine(Line line);
}
