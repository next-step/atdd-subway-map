package subway.section;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import subway.line.LineRepository;
import subway.station.Station;
import subway.line.Line;
import subway.station.StationRepository;

@Service
public class SectionService {
	private LineRepository lineRepository;
	private SectionRepository sectionRepository;
	private StationRepository stationRepository;

	private final int UP = 0;
	private final int DOWN = 1;

	public SectionService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public SectionResponse addSection(Long lineId, SectionCreateRequest sectionRequest) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new NullPointerException("Line doesn't exist"));

		List<Station> stations = findStation(sectionRequest);
		line.validAddSection(stations.get(UP), stations.get(DOWN));

		Section newSection = saveSection(sectionRequest);
		line.addSection(newSection);
		return createSectionResponse(newSection);
	}

	@Transactional
	public Section saveSection(SectionCreateRequest sectionRequest) {
		List<Station> stations = findStation(sectionRequest);
		Section newSection = sectionRepository.save(new Section(stations.get(UP), stations.get(DOWN), sectionRequest.getDistance()));
		return newSection;
	}

	@Transactional
	public void deleteSectionById(Long lineId, Long downStationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new NullPointerException("Line doesn't exist"));
		Section findSection = sectionRepository.findByDownStation_Id(downStationId);
		line.removeSection(findSection);
	}

	private SectionResponse createSectionResponse(Section section) {
		return new SectionResponse(section);
	}

	private List<Station> findStation(SectionCreateRequest sectionRequest) {
		Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));
		Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));

		return List.of(upStation, downStation);
	}
}
