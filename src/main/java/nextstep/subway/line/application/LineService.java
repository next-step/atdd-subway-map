package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.CreateLineResponse;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.ModifyLineRequest;
import nextstep.subway.section.Section;
import nextstep.subway.section.SectionRepository;
import nextstep.subway.section.Sections;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;

@Service
public class LineService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final SectionRepository sectionRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository,
		SectionRepository sectionRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public CreateLineResponse createLine(CreateLineRequest request) {
		Line line = lineRepository.save(new Line(request.getName(), request.getColor()));

		Section section = sectionRepository.save(
			new Section(line.getId(), request.getUpStationId(), request.getDownStationId(), request.getDistance()));

		Station upStation = stationRepository.findById(section.getUpStationId()).orElseThrow();
		Station downStation = stationRepository.findById(section.getDownStationId()).orElseThrow();

		return new CreateLineResponse(line, upStation, downStation);
	}

	@Transactional(readOnly = true)
	public List<LineResponse> getLines() {
		return lineRepository.findAll()
			.stream()
			.map(this::lineResponse)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public LineResponse getLine(Long lineId) {
		Line line = lineRepository.findById(lineId).orElseThrow();
		return lineResponse(line);
	}

	@Transactional
	public void modifyLine(Long id, ModifyLineRequest request) {
		Line line = lineRepository.findById(id).orElseThrow();
		line.modifyLine(request.getName(), request.getColor());
	}

	@Transactional
	public void deleteLine(Long id) {
		lineRepository.deleteById(id);
	}

	private LineResponse lineResponse(Line line) {
		Sections sections = new Sections(sectionRepository.findAllByLineId(line.getId()));
		List<Station> stations = stationRepository.findAllById(sections.getStationIds());
		return new LineResponse(line, stations);
	}
}
