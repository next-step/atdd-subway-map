package subway.api.infrastructure.operators;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subway.api.domain.model.entity.Line;
import subway.api.domain.operators.LineFactory;
import subway.api.infrastructure.persistence.LineRepository;
import subway.api.interfaces.dto.LineCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Component
@RequiredArgsConstructor
public class SimpleLineFactory implements LineFactory {

	private final LineRepository lineRepository;

	@Override
	public Line createLine(LineCreateRequest request) {
		return lineRepository.save(Line.from(request));
	}

	@Override
	public void deleteLine(Line line) {
		lineRepository.delete(line);
	}
}
