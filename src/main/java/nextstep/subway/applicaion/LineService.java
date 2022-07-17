package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
	private final StationRepository stationRepository;
	private final LineRepository lineRepository;
	private final SectionRepository sectionRepository;

	public LineService(StationRepository stationRepository, LineRepository lineRepository, SectionRepository sectionRepository) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Section section = saveSection(lineRequest);

		Line line = lineRepository.save(
				new Line(lineRequest.getName(), lineRequest.getColor(), section)
		);
		return LineResponse.of(line);
	}

	private Section saveSection(LineRequest lineRequest) {
		Station upStation = stationRepository.findById(lineRequest.getUpStationId())
				.orElseThrow(RuntimeException::new);

		Station downStation = stationRepository.findById(lineRequest.getDownStationId())
				.orElseThrow(RuntimeException::new);

		return sectionRepository.save(
				new Section(upStation, downStation, lineRequest.getDistance())
		);
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
