package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
	private StationRepository stationRepository;
	private LineRepository lineRepository;

	public LineService(StationRepository stationRepository, LineRepository lineRepository) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Station upStation = stationRepository.findById(lineRequest.getUpStationId())
				.orElseThrow(RuntimeException::new);
		Station downStation = stationRepository.findById(lineRequest.getDownStationId())
				.orElseThrow(RuntimeException::new);

		Line line = lineRepository.save(
				new Line(
						lineRequest.getName(),
						lineRequest.getColor(),
						upStation,
						downStation
				)
		);
		return LineResponse.of(line);
	}

	public List<LineResponse> findAllLines() {
		return LineResponse.listOf(lineRepository.findAll());
	}

	public LineResponse findLineById(Long lineId) {
		return LineResponse.of(lineRepository.findById(lineId)
				.orElseThrow(RuntimeException::new));
	}

	@Transactional
	public void updateLine(Long lineId, LineUpdateRequest lineUpdateRequest) {
		Line line = lineRepository.findById(lineId)
				.orElseThrow(RuntimeException::new);

		line.update(lineUpdateRequest);
	}

	@Transactional
	public void deleteLineById(Long lineId) {
		lineRepository.deleteById(lineId);
	}
}
