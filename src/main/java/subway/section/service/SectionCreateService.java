package subway.section.service;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.line.domain.Line;
import subway.section.domain.Section;
import subway.section.exception.InvalidSectionCreateException;
import subway.section.model.SectionCreateRequest;
import subway.section.repository.SectionRepository;
import subway.station.service.StationService;
import subway.support.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class SectionCreateService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    @Transactional
    public Section create(Line line, SectionCreateRequest request) {
        if (!Objects.equals(request.getUpStationId(), line.getDownStation().getId())) {
            throw new InvalidSectionCreateException(ErrorCode.SECTION_CREATE_FAIL_BY_UPSTATION);
        }

        if (Objects.equals(request.getDownStationId(), line.getUpStation().getId()) ||
        Objects.equals(request.getDownStationId(), line.getDownStation().getId())) {
            throw new InvalidSectionCreateException(ErrorCode.SECTION_CREATE_FAIL_BY_DOWNSTATION);
        }

        Section section = Section.builder()
                                 .line(line)
                                 .downStation(stationService.get(request.getDownStationId()))
                                 .upStation(stationService.get(request.getUpStationId()))
                                 .distance(request.getDistance())
                                 .build();

        return sectionRepository.save(section);
    }
}
