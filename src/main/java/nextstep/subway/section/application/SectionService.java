package nextstep.subway.section.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.section.Section;
import nextstep.subway.section.SectionRepository;
import nextstep.subway.section.Sections;
import nextstep.subway.section.application.dto.AppendSectionRequest;

@Service
public class SectionService {

	private final SectionRepository sectionRepository;

	public SectionService(SectionRepository sectionRepository) {
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public void appendSection(Long lineId, AppendSectionRequest request) {
		Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));
		sections.validateAppendUpStationId(request.getUpStationId());
		sections.validateAppendDownStationId(request.getDownStationId());

		Section appendSection = new Section(lineId, request.getUpStationId(), request.getDownStationId(),
			request.getDistance());
		sectionRepository.save(appendSection);
	}

	@Transactional
	public void deleteSection(Long lineId, Long stationId) {
		Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));
		sections.validateMinimumSize();
		sections.validateDeleteStationId(stationId);
	}
}
