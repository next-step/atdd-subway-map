package subway.api.domain.specification;

import subway.api.domain.model.entity.Line;
import subway.api.interfaces.dto.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SubwaySpecification {
	boolean isNotSatisfiedBy(Line line, SectionCreateRequest request);

	boolean isSatisfiedBy(Line line, SectionCreateRequest request);
}
