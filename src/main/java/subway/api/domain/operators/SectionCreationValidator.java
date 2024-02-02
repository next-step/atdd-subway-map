package subway.api.domain.operators;

import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.model.entity.Line;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SectionCreationValidator {
	void validate(Line line, SectionCreateCommand request);
}
