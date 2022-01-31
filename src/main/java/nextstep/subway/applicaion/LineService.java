package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.SubwayException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public LineResponse saveLine(LineRequest request) {
		if (lineRepository.existsByName(request.getName())) {
			throw new SubwayException.DuplicatedNameException();
		}
		Station upStation = stationRepository.findById(request.getUpStationId())
			.orElseThrow(SubwayException.NotFoundException::new);
		Station downStation = stationRepository.findById(request.getDownStationId())
			.orElseThrow(SubwayException.NotFoundException::new);

		Line line = lineRepository.save(request.toLine(upStation, downStation));
		return LineResponse.of(line);
	}

	public List<LineResponse> findAllLines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}

	public LineResponse findById(Long id) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
		return LineResponse.of(line);
	}

	public void updateLine(Long id, LineRequest lineRequest) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
		line.update(lineRequest.getName(), lineRequest.getColor());
	}

	public void deleteLine(Long id) {
		lineRepository.deleteById(id);
	}
}
