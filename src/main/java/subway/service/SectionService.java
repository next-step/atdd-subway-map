package subway.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.repository.SectionRepository;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {
    SectionRepository sectionRepository;
    StationService stationService;

    @Transactional
    public SectionResponse saveSection(SectionRequest sectionRequest) throws Exception {
        Section section = sectionRepository.save(new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance()));

        return createSectionResponse(section);
    }

    private SectionResponse createSectionResponse(Section section) throws Exception{
        Station upStation = stationService.findById(Long.valueOf(section.getUpStationId()));
        Station downStation = stationService.findById(Long.valueOf(section.getDownStationId()));

        return new SectionResponse(section.getId(), upStation, downStation, section.getDistance());
    }
}
