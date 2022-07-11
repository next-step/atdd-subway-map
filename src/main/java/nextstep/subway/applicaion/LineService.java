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

import java.util.ArrayList;
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
		List<Station> stations = new ArrayList<>();
		Station station1 = stationRepository.save(new Station("지하철역"));
		Station station2 = stationRepository.save(new Station("새로운지하철역"));
		stations.add(station1);
		stations.add(station2);

		Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), stations));
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
}
