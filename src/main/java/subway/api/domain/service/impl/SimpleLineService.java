package subway.api.domain.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.api.domain.dto.inport.LineCreateCommand;
import subway.api.domain.dto.inport.LineUpdateCommand;
import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Section;
import subway.api.domain.operators.LineFactory;
import subway.api.domain.operators.LineResolver;
import subway.api.domain.operators.SectionFactory;
import subway.api.domain.service.LineService;
import subway.api.interfaces.dto.request.LineCreateRequest;
import subway.api.interfaces.dto.response.LineResponse;
import subway.api.interfaces.dto.request.LineUpdateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimpleLineService implements LineService {

	private final LineFactory lineFactory;
	private final SectionFactory sectionFactory;
	private final LineResolver lineResolver;

	@Override
	@Transactional
	public LineResponse saveLine(LineCreateCommand request) {
		Line line = lineFactory.createLine(request);
		Section section = sectionFactory.createSection(request, line);

		line.updateLink(section);

		return LineResponse.from(line);
	}

	@Override
	public List<LineResponse> findAllLines() {
		return lineResolver.fetchAll().stream().map(LineResponse::from).collect(Collectors.toList());
	}

	@Override
	public LineResponse findLineById(Long id) {
		return LineResponse.from(fetchLineOrThrow(id));
	}

	@Override
	@Transactional
	public LineResponse updateLineById(Long id, LineUpdateCommand updateRequest) {
		Line line = fetchLineOrThrow(id);

		line
			.updateName(updateRequest.getName())
			.updateColor(updateRequest.getColor());

		return LineResponse.from(line);
	}

	@Override
	@Transactional
	public void deleteLineById(Long id) {
		Line line = fetchLineOrThrow(id);

		sectionFactory.deleteByLine(line);
		lineFactory.deleteLine(line);
	}

	private Line fetchLineOrThrow(Long id) {
		return lineResolver.fetchOptional(id).orElseThrow(EntityNotFoundException::new);
	}

}
