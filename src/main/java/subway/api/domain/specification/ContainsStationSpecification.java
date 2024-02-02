package subway.api.domain.specification;

import org.springframework.stereotype.Component;

import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.model.entity.Line;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Component
public class ContainsStationSpecification implements SubwaySpecification {

	@Override
	public boolean isNotSatisfiedBy(Line line, SectionCreateCommand request) {
		return !isSatisfiedBy(line,request);
	}


	@Override
	public boolean isSatisfiedBy(Line line, SectionCreateCommand request) {
		return line.notContainsStation(request.getDownStationId());
	}


}