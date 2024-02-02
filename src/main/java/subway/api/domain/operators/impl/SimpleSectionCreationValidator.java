package subway.api.domain.operators.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.model.entity.Line;
import subway.api.domain.operators.SectionCreationValidator;
import subway.api.domain.specification.SubwaySpecification;
import subway.common.exception.SectionCreationNotValidException;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Component
@RequiredArgsConstructor
public class SimpleSectionCreationValidator implements SectionCreationValidator {
	private final List<SubwaySpecification> specifications;

	@Override
	public void validate(Line line, SectionCreateCommand request) {
		specifications.stream()
			.filter(spec -> spec.isNotSatisfiedBy(line, request))
			.findFirst()
			.ifPresent(spec -> {
				throw new SectionCreationNotValidException();
			});
	}
}
