package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.*;
import nextstep.subway.exception.SubwayException;
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
		Section section =
				saveSection(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());

		Line line = lineRepository.save(
				new Line(lineRequest.getName(), lineRequest.getColor(), section)
		);
		return LineResponse.of(line);
	}

	public List<LineResponse> findAllLines() {
		return LineResponse.listOf(lineRepository.findAll());
	}

	public LineResponse findLineById(Long lineId) {
		return LineResponse.of(lineRepository.findById(lineId)
				.orElseThrow(SubwayException::new));
	}

	@Transactional
	public void updateLine(Long lineId, LineUpdateRequest lineUpdateRequest) {
		Line line = lineRepository.findById(lineId)
				.orElseThrow(SubwayException::new);

		line.update(lineUpdateRequest);
	}

	@Transactional
	public void deleteLineById(Long lineId) {
		lineRepository.deleteById(lineId);
	}


	@Transactional
	public void addSection(Long lineId, SectionRequest sectionRequest) {
		Line line = lineRepository.findById(lineId)
				.orElseThrow(SubwayException::new);

		Section section =
				saveSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());

		line.addSection(section);
	}


	private Section saveSection(Long upStationId, Long downStationId, Integer distance) {
		Station upStation = stationRepository.findById(upStationId)
				.orElseThrow(SubwayException::new);

		Station downStation = stationRepository.findById(downStationId)
				.orElseThrow(SubwayException::new);

		return sectionRepository.save(
				new Section(upStation, downStation, distance)
		);
	}

	@Transactional
	public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {
		Station station = stationRepository.findById(sectionDeleteRequest.getStationId())
				.orElseThrow(SubwayException::new);
		Line line = lineRepository.findById(lineId)
				.orElseThrow(SubwayException::new);
		Section section = line.deleteSectionOf(station);
		sectionRepository.delete(section);
	}
}
