package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.domain.LineRepository;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.applicaion.station.StationService;
import nextstep.subway.applicaion.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class LineService {

	private LineRepository lineRepository;
	private StationService stationService;

	public LineService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Station upStation = stationService.getStationById(lineRequest.getUpStationId());
		Station downStation = stationService.getStationById(lineRequest.getDownStationId());

		if (upStation == null || downStation == null) {
			throw new NoSuchElementException("승차 또는 하차역이 존재하지 않습니다.");
		}

		Line line = lineRepository.save(lineRequest.toLine());

		return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
	}
}
