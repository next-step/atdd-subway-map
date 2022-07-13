package nextstep.subway.applicaion;

import static nextstep.subway.common.exception.errorcode.EntityErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;

@Service
public class LineService {

	private final LineRepository lineRepository;

	public LineService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	@Transactional
	public LineResponse createLines(LineRequest lineRequest) {
		Line line = lineRepository.save(lineRequest.toLine());
		return createLineResponse(line);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
			.stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	public LineResponse findLine(Long stationId) {

		return createLineResponse(lineRepository.findById(stationId)
			.orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND)));
	}

	@Transactional
	public void updateLine(Long stationId, LineRequest lineRequest) {
		Line line = lineRepository.findById(stationId)
			.orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
		line.updateStationLineInformation(lineRequest.getName(), lineRequest.getColor());
	}

	@Transactional
	public void deleteLine(Long stationId) {
		lineRepository.delete(lineRepository.findById(stationId)
			.orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND)));
	}

	private LineResponse createLineResponse(Line line) {
		return new LineResponse(line.getId(),
			line.getName(),
			line.getColor());
	}

}
