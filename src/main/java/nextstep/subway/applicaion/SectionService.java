package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
public class SectionService {

	private final static int NUMBER_ZERO = 0;

	private final SectionRepository sectionRepository;
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public SectionService(SectionRepository sectionRepository,
												LineRepository lineRepository,
												StationRepository stationRepository
	) {
		this.sectionRepository = sectionRepository;
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public SectionResponse createSection(final Long id, final SectionRequest sectionRequest) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		Station upStation =
						stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(RuntimeException::new);
		Station downStation =
						stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(RuntimeException::new);
		verifyStationsRelation(upStation);
		newUpStationMustBeDownStation(upStation);
		Section section = sectionRepository.save(Section.of(line, upStation, downStation, sectionRequest.getDistance()));

		return SectionResponse.of(section.getId(), upStation.getId(), downStation.getId(), section.getDistance());
	}

	public void deleteSection(final Long lineId, final Long stationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
		Station downStation = stationRepository.findById(stationId).orElseThrow(RuntimeException::new);
		Section section = sectionRepository.findByLineAndDownStation(line, downStation).orElseThrow(RuntimeException::new);
		sectionRepository.delete(section);
	}

	private void verifyStationsRelation(final Station upStation) {
		Section sectionByUpStation = sectionRepository.findSectionByUpStation(upStation);
		if (!ObjectUtils.isEmpty(sectionByUpStation)) {
			throw new DuplicationException("새로운 구간의 상행역은 이미 등록되어있습니다.");
		}
	}

	private void newUpStationMustBeDownStation(final Station upStation) {
		if (NUMBER_ZERO != sectionRepository.count()) {
			Section sectionByDownStation = sectionRepository.findSectionByDownStation(upStation);
			if (ObjectUtils.isEmpty(sectionByDownStation)) {
				throw new RuntimeException("새로운 상행역은 무조건 하행역으로 등록되어있어야합니다.");
			}
		}
	}

	private void deleteSectionMustBeDownStation(final Long stationId) {

	}
}
