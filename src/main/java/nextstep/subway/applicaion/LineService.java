package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

@Service
public class LineService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public LineResponse createLine(LineRequest lineRequest) {
		Station upStation = getStation(lineRequest.getUpStationId());
		Station downStation = getStation(lineRequest.getDownStationId());
		Line line = lineRequest.toEntity(upStation, downStation);
		lineRepository.save(line);
		return LineResponse.from(line);
	}

	private Station getStation(Long stationId) {
		return stationRepository.findById(stationId)
			.orElseThrow(() -> new RuntimeException("역이 존재하지 않습니다."));
	}

	public List<LineResponse> getAllLine() {
		List<Line> lineList = lineRepository.findAll();
		return LineResponse.fromList(lineList);
	}

	public LineResponse getLine(Long lineId) {
		return LineResponse.from(getLineById(lineId));
	}

	private Line getLineById(Long lineId) {
		return lineRepository.findById(lineId).orElseThrow(() -> new RuntimeException("노선이 존재하지 않습니다."));
	}

	@Transactional
	public void deleteLine(Long lineId) {
		lineRepository.delete(getLineById(lineId));
	}
}
