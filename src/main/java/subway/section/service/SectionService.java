package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.error.InvalidSectionRequestException;
import subway.common.error.NotFoundException;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;
import subway.section.domain.Section;
import subway.section.dto.CreateSectionRequest;
import subway.section.dto.CreateSectionResponse;
import subway.section.dto.SectionResponse;
import subway.section.repository.SectionRepository;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static subway.section.mapper.SectionMapper.SECTION_MAPPER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public List<SectionResponse> findSections(Long lineId) {
        List<Section> sections = sectionRepository.findAllByLine_Id(lineId);
        return sections.stream()
                .map(SECTION_MAPPER::mapToSectionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateSectionResponse createSection(Long lineId, CreateSectionRequest createSectionRequest) {
        Line line = findLine(lineId);
        Station upStation = findStation(createSectionRequest.getUpStationId());
        Station downStation = findStation(createSectionRequest.getDownStationId());

        createSectionRequest.validateUpStationId(line);
        createSectionRequest.validateDownStationId(line, downStation);

        Section section = Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(createSectionRequest.getDistance())
                .build();

        Section savedSection = sectionRepository.save(section);

        return SECTION_MAPPER.mapToAddSectionResponse(savedSection);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLine(lineId);

        if (line.hasLessThanTwoSections()) {
            throw new InvalidSectionRequestException("구간이 2개 이상일 때만 삭제할 수 있습니다.");
        }

        if (!line.getTerminalStationId().equals(stationId)) {
            throw new InvalidSectionRequestException("마지막 구간만 삭제할 수 있습니다.");
        }

        Section section = sectionRepository.findByLine_IdAndDownStation_Id(lineId, stationId)
                .orElseThrow(() -> new NotFoundException(
                        Map.of(
                                "lineId", lineId.toString(),
                                "stationId", stationId.toString()
                        )));

        sectionRepository.delete(section);
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }
}
