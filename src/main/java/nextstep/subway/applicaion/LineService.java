package nextstep.subway.applicaion;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.SubwayException;

@Service
@Transactional(readOnly = true)
public class LineService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = lineRepository.save(Line.builder()
			.name(lineRequest.getName())
			.color(lineRequest.getColor())
			.upStationId(lineRequest.getUpStationId())
			.downStationId(lineRequest.getDownStationId())
			.distance(lineRequest.getDistance())
			.build()
		);
		return createLineResponse(line);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	private LineResponse createLineResponse(Line line) {
		Optional<Station> upStation = stationRepository.findById(line.getUpStationId());
		Optional<Station> downStation = stationRepository.findById(line.getDownStationId());

		return LineResponse.builder()
			.id(line.getId())
			.name(line.getName())
			.color(line.getColor())
			.upStation(upStation.orElseThrow(() -> new SubwayException("no station")))
			.downStation(downStation.orElseThrow(() -> new SubwayException("no station")))
			.build();
	}
}
