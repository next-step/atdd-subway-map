package subway.api.domain.operators;

import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Link;
import subway.api.interfaces.dto.LineCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LinkFactory {
	Link createLink(LineCreateRequest request, Line line);
}
