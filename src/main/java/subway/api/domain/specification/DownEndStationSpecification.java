package subway.api.domain.specification;

import org.springframework.stereotype.Component;

import subway.api.domain.model.entity.Line;
import subway.api.interfaces.dto.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Component
public class DownEndStationSpecification implements SubwaySpecification {
	@Override
	public boolean isNotSatisfiedBy(Line line, SectionCreateRequest request) {
		return !isSatisfiedBy(line,request);
	}

	@Override
	public boolean isSatisfiedBy(Line line, SectionCreateRequest request) {
		return line.isDownEndStation(request.getUpStationId());
	}
}