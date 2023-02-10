package subway.section;

import static subway.section.Section.*;

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

	public SectionService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public SectionResponse addSection(Long lineId, SectionCreateRequest sectionRequest) {
		Line findLine = lineRepository.findById(lineId).orElseThrow(() -> new NullPointerException("Line doesn't exist"));
		Section newSection = saveSection(findLine, sectionRequest);
		return createSectionResponse(findLine.getSections().addSection(newSection));
	}

	@Transactional
	public Section saveSection(Line line, SectionCreateRequest sectionRequest) {
		Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));
		Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));

		Section section = validCreateSection(line.getSections(), upStation, downStation, sectionRequest.getDistance());
		return sectionRepository.save(section);
	}

	@Transactional
	public void deleteSectionById(Long lineId, Long downStationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new NullPointerException("Line doesn't exist"));
		Section findSection = sectionRepository.findByDownStation_Id(downStationId);
		line.getSections().removeSection(findSection);
	}

	private SectionResponse createSectionResponse(Section section) {
		return new SectionResponse(section);
	}

}
