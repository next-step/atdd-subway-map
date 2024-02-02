package subway.api.domain.specification;

import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.model.entity.Line;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SubwaySpecification {
	boolean isNotSatisfiedBy(Line line, SectionCreateCommand request);

	boolean isSatisfiedBy(Line line, SectionCreateCommand request);
}
