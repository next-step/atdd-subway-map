package subway.section;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, StationService stationService, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public List<SectionResponse> findAllSection(Long lineId) {
        Line line = lineService.findLineById(lineId);
        return findAllSection(line);
    }

    public List<SectionResponse> findAllSection(Line line) {
        return sectionRepository.findAllByLine(line).stream()
                .map(this::createSectionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.findLineById(lineId);
        validateSavable(line, sectionRequest);

        line.updateDownStation(sectionRequest.getDownStationId());
        return createSectionResponse(sectionRepository.save(sectionRequest.toEntity(line)));
    }

    private void validateSavable(Line line, SectionRequest sectionRequest) {
        List<SectionResponse> sections = findAllSection(line);
        if (sections.size() > 0) {
            validateEndStationOfLine(line, sectionRequest.getUpStationId());
            validateDuplicateSection(line, sectionRequest.getDownStationId());
        }
    }

    private void validateDuplicateSection(Line line, Long downStationsId) {
        List<SectionResponse> sections = findAllSection(line);
        if (isContainsSection(sections, downStationsId)) {
            throw new SectionException("노선에 중복된 구간은 추가할 수 없습니다.");
        }
    }

    @Transactional
    public void deleteLineOfSection(Long lineId, Long stationId) {
        Line line = lineService.findLineById(lineId);
        validateDeletable(line, stationId);
        sectionRepository.deleteByLineAndDownStationId(line, stationId);
    }

    @Transactional
    public void deleteAllLineOfSection(Long lineId) {
        Line line = lineService.findLineById(lineId);
        sectionRepository.deleteByLine(line);
    }



    private void validateDeletable(Line line, Long stationId) {
        List<SectionResponse> sections = findAllSection(line);
        validateEndStationOfLine(line, stationId);
        validateMinimumSection(sections);
        validateContainsSection(sections, stationId);
    }

    private void validateEndStationOfLine(Line line, Long stationId) {
        if (!line.getDownStationId().equals(stationId)) {
            throw new SectionException("노선의 하행 종점역 이외의 구간은 추가 및 삭제할 수 없습니다.");
        }
    }

    private void validateMinimumSection(List<SectionResponse> sections) {
        if (sections.size() == 1) {
            throw new SectionException("노선에 구간이 한개만 존재하여 구간을 삭제할 수 없습니다.");
        }
    }

    private void validateContainsSection(List<SectionResponse> sections, Long stationId) {
        if (!isContainsSection(sections, stationId)) {
            throw new SectionException("노선에 포함되지 않은 구간을 삭제할 수 없습니다.");
        }
    }

    private boolean isContainsSection(List<SectionResponse> sections, Long stationId) {
        List<StationResponse> stations = sections.stream()
                .flatMap(item -> item.getStations().stream())
                .collect(Collectors.toList());
        return stations.stream()
                .anyMatch(stationResponse -> stationResponse.getId().equals(stationId));
    }

    private SectionResponse createSectionResponse(Section section) {
        List<StationResponse> stations = section.getStationIds()
                .stream()
                .map(stationService::findStationById)
                .collect(Collectors.toList());
        return SectionResponse.toResponse(section, stations);
    }
}
