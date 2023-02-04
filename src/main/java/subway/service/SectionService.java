package subway.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {
    SectionRepository sectionRepository;
    StationRepository stationRepository;
    public static String ERROR_NO_FOUND_SECTION = "[SYS_ERROR] do not found section by id";

    @Transactional
    public SectionResponse saveSection(SectionRequest sectionRequest) throws Exception {
        Section section = sectionRepository.save(new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance()));

        return createSectionResponse(section);
    }

    private SectionResponse createSectionResponse(Section section) throws Exception{
        Station upStation = stationRepository.findById(Long.valueOf(section.getUpStationId())).orElseThrow(() -> new Exception(ERROR_NO_FOUND_SECTION));
        Station downStation = stationRepository.findById(Long.valueOf(section.getDownStationId())).orElseThrow(() -> new Exception(ERROR_NO_FOUND_SECTION));

        return new SectionResponse(section.getId(), upStation, downStation, section.getDistance());
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }
}
