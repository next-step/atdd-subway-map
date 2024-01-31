package subway.api.domain.service;

import subway.api.domain.dto.outport.SectionInfo;
import subway.api.interfaces.dto.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SectionService {
	SectionInfo addSection(Long lineId, SectionCreateRequest createRequest);

	void deleteSection(Long lineId, Long stationId);
}
