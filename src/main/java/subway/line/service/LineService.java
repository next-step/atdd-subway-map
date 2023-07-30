package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.error.InvalidSectionRequestException;
import subway.common.error.NotFoundException;
import subway.line.domain.Line;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.ModifyLineResponse;
import subway.line.repository.LineRepository;
import subway.section.domain.Section;
import subway.section.dto.AddSectionRequest;
import subway.section.dto.AddSectionResponse;
import subway.section.dto.SectionResponse;
import subway.section.repository.SectionRepository;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static subway.common.Validation.*;
import static subway.line.mapper.LineMapper.LINE_MAPPER;
import static subway.section.mapper.SectionMapper.SECTION_MAPPER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse createLine(CreateLineRequest createLineRequest) {
        Line line = LINE_MAPPER.mapToLine(createLineRequest);
        Station upStation = findStationById(createLineRequest.getUpStationId());
        Station downStation = findStationById(createLineRequest.getDownStationId());
        Section section = new Section(line, upStation, downStation, createLineRequest.getDistance());
        line.addSection(section);
        Line savedLine = lineRepository.save(line);
        return LINE_MAPPER.toLineResponse(savedLine);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LINE_MAPPER::toLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        return LINE_MAPPER.toLineResponse(line);
    }

    @Transactional
    public ModifyLineResponse modifyLine(Long id, ModifyLineRequest modifyLineRequest) {
        Line line = findLineById(id);
        setIfNotNull(modifyLineRequest.getName(), line::setName);
        setIfNotNull(modifyLineRequest.getColor(), line::setColor);
        return LINE_MAPPER.toModifyLineResponse(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        lineRepository.deleteById(id);
    }

    @Transactional
    public AddSectionResponse addSection(Long lineId, AddSectionRequest addSectionRequest) {
        Line line = findLineById(lineId);
        Station upStation = findStationById(addSectionRequest.getUpStationId());
        Station downStation = findStationById(addSectionRequest.getDownStationId());

        Section section = Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(addSectionRequest.getDistance())
                .build();

        line.addSection(section);
        return SECTION_MAPPER.mapToCreateSectionResponse(section);
    }

    public List<SectionResponse> findSections(Long lineId) {
        List<Section> sections = sectionRepository.findAllByLine_Id(lineId);
        return sections.stream()
                .map(SECTION_MAPPER::mapToSectionResponse)
                .collect(Collectors.toList());
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);

        if (line.hasLessThanTwoSections()) {
            throw new InvalidSectionRequestException("구간이 2개 이상일 때만 삭제할 수 있습니다.");
        }

        if (!line.isTerminalStationId(stationId)) {
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

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
