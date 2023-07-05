package subway.section.service;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.line.domain.Line;
import subway.section.domain.Section;
import subway.section.exception.InvalidSectionDownStationException;
import subway.section.exception.InvalidSectionUpStationException;
import subway.section.model.SectionCreateRequest;
import subway.section.repository.SectionRepository;
import subway.station.service.StationService;

@Service
@Slf4j
@RequiredArgsConstructor
public class SectionCreateService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    @Transactional
    public Section create(Line line, SectionCreateRequest request) {
        if (!Objects.equals(request.getUpStationId(), line.getDownStation().getId())) {
            throw new InvalidSectionUpStationException();
        }

        if (Objects.equals(request.getDownStationId(), line.getUpStation().getId()) ||
        Objects.equals(request.getDownStationId(), line.getDownStation().getId())) {
            throw new InvalidSectionDownStationException();
        }

        Section section = Section.builder()
                                 .distance(request.getDistance())
                                 .downStation(stationService.get(request.getDownStationId()))
                                 .upStation(stationService.get(request.getUpStationId()))
                                 .build();

        return sectionRepository.save(section);
    }
}
