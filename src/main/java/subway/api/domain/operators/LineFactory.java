package subway.api.domain.operators;

import subway.api.domain.dto.inport.LineCreateCommand;
import subway.api.domain.model.entity.Line;
import subway.api.interfaces.dto.request.LineCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LineFactory {
	Line createLine(LineCreateCommand request);

	void deleteLine(Line line);
}
