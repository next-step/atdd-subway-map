package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = lineRepository.save(lineRequest.toLineEntity());
		return createLineResponse(line);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	public LineResponse findLineById(Long id) {
		return lineRepository.findById(id)
			.map(this::createLineResponse)
			.orElseThrow(EntityNotFoundException::new);
	}

	@Transactional
	public void updateLine(Long id, LineUpdateRequest updateRequest) {
		Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		line.updateName(updateRequest.getName());
		line.updateColor(updateRequest.getColor());
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	private LineResponse createLineResponse(Line line) {
		List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));
		return LineResponse.of(line, stations);
	}
}
