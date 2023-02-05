package subway.section;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import subway.station.Station;
import subway.station.StationRepository;
import subway.line.Line;
import subway.line.LineRepository;

@Service
public class SectionService {
	private LineRepository lineRepository;
	private SectionRepository sectionRepository;
	private StationRepository stationRepository;

	public SectionService(LineRepository lineRepository, SectionRepository sectionRepository,
		StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public SectionResponse addSection(Long lineId, SectionCreateRequest sectionRequest) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new NullPointerException("Line doesn't exist"));

		if (line.getLastSection().getDownStation().getId() != sectionRequest.getUpStationId()) {
			throw new NoSuchElementException("등록하려는 새로운 구간의 상행역이 노선의 하행 종점역과 일치하지 않습니다.");
		}
		if (line.getAllStation().contains(stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist")))) {
			throw new IllegalArgumentException("등록하려는 새로운 구간의 하행 종점역이 이미 노선에 등록되어 있습니다.");
		}

		Section newSection = saveSection(sectionRequest);
		line.addSection(newSection);
		return createSectionResponse(newSection);
	}

	@Transactional
	public Section saveSection(SectionCreateRequest sectionRequest) {
		Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));
		Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));

		Section newSection = sectionRepository.save(new Section(upStation, downStation, sectionRequest.getDistance()));
		return newSection;
	}

	@Transactional
	public void deleteSectionById(Long lineId, Long downStationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new NullPointerException("Line doesn't exist"));
		if (line.getLastSection().getDownStation().getId() != downStationId) {
			throw new NoSuchElementException("삭제하려는 구간의 하행역이 노선의 하행 종점역과 일치하지 않습니다.");
		}
		if (line.isLastSection()) {
			throw new IllegalArgumentException("삭제하려는 구간이 노선의 마지막 구간입니다.");
		}
		line.removeLastSection();
	}

	public SectionResponse createSectionResponse(Section section) {
		return new SectionResponse(section);
	}
}
