package nextstep.subway.section.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.section.application.dto.AppendSectionRequest;

@Service
public class SectionService {

	@Transactional
	public void appendSection(Long lineId, AppendSectionRequest request) {

	}

	@Transactional
	public void deleteSection(Long lineId, Long stationId) {

	}
}
