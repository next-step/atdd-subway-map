package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Sections;

@Service
public class SectionService {

	private LineRepository lineRepository;
	private SectionRepository sectionRepository;

	public SectionService(LineRepository lineRepository, SectionRepository sectionRepository) {
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public SectionResponse createSelection(long lineId, SectionRequest sectionRequest) {
		Line line = lineRepository.findById(lineId)
			.orElseThrow(IllegalArgumentException::new);
		line.isRegistrable(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());
		line.updateDownStationId(sectionRequest.getDownStationId());
		return createSelectionResponse(sectionRepository.save(sectionRequest.toSelection(lineId)));
	}

	@Transactional
	public void deleteSelection(long lineId, long stationId) {
		Line line = lineRepository.findById(lineId)
			.orElseThrow(IllegalArgumentException::new);
		line.isDeletable(stationId);

		Sections selections = new Sections(sectionRepository.findByLineIdOrderById(lineId));
		selections.isDeletable(stationId);

		Section section = sectionRepository.findByDownStationId(stationId)
			.orElseThrow(IllegalArgumentException::new);
		sectionRepository.delete(section);

	}

	public List<SectionResponse> getSelectionList(long lineId) {
		return sectionRepository.findByLineIdOrderById(lineId)
			.stream()
			.map(this::createSelectionResponse)
			.collect(Collectors.toList());
	}

	public SectionResponse getSelection(long selectionId) {
		return createSelectionResponse(sectionRepository.findById(selectionId)
			.orElseThrow(IllegalArgumentException::new));
	}

	private SectionResponse createSelectionResponse(Section section) {
		return new SectionResponse(section.getId(),
			section.getUpStationId(),
			section.getDownStationId(),
			section.getDistance());
	}

}
