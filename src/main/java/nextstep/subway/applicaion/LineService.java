package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.exception.ExceptionMessages;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = saveLine(lineRequest.toEntity());
        Line savedLine = findLine(line.getId());
        Section section = saveSection(savedLine, lineRequest);
        return LineResponse.convertedByEntity(savedLine, section.getUpStation(), section.getDownStation());
    }


    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(v -> LineResponse.convertedByEntity(v, getUpEndpoint(v), getDownEndpoint(v)))
                .collect(Collectors.toList());
    }

    public LineResponse getLine(long lineId) {
        Line line = findLine(lineId);
        return LineResponse.convertedByEntity(line, getUpEndpoint(line), getDownEndpoint(line));
    }

    @Transactional
    public LineResponse updateLine(LineRequest lineRequest, long lineId) {
        Line line = findLine(lineId);
        line.changeNameAndColor(lineRequest);
        Line updatedLine = saveLine(line);
        return LineResponse.convertedByEntity(updatedLine, getUpEndpoint(updatedLine), getDownEndpoint(updatedLine));
    }

    @Transactional
    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }

    public List<SectionResponse> getSections(long lineId) {
        List<Section> sections = sectionRepository.findSectionsByLineId(lineId);
        return sections.stream()
                .map(SectionResponse::convertedByEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SectionResponse registerSection(long lineId, SectionRequest sectionRequest) {
        Line line = findLine(lineId);
        Long upStationId = sectionRequest.getUpStationId();
        Long downEndpointStationId = getDownEndpoint(line).getId();
        line.checkRegisterEndpointId(upStationId, downEndpointStationId);
        Section section = saveSection(line, sectionRequest);
        return SectionResponse.convertedByEntity(section);
    }

    @Transactional
    public void removeSection(long lineId, long stationId) {
        Line line = findLine(lineId);
        line.checkRemoveEndPointId(stationId, getDownEndpoint(line).getId());
        Section.checkSectionCount(sectionRepository.count());
        sectionRepository.deleteSectionByDownStationIdAndLineId(stationId, lineId);
    }

    private Section saveSection(Line line, SectionRequest sectionRequest) {
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        return sectionRepository.save(section);
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoStationExceptionMessage(stationId)));
    }

    private Section saveSection(Line line, LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());
        Section section = new Section(line, upStation, downStation,
                lineRequest.getDistance());
        return sectionRepository.save(section);
    }

    private List<Section> getSections(Line line) {
        return sectionRepository.findSectionsByLineId(line.getId());
    }

    private Station getUpEndpoint(Line line) {
        List<Section> sections = getSections(line);
        List<Long> upStationIds = getUpStationIds(sections);
        List<Long> downStationIds = getDownStationIds(sections);
        upStationIds.removeAll(downStationIds);
        Long upStationId = upStationIds.get(0);
        return sections.stream()
                .map(Section::getUpStation)
                .filter(upStation -> upStation.getId().equals(upStationId))
                .collect(Collectors.toList())
                .get(0);
    }

    private Station getDownEndpoint(Line line) {
        List<Section> sections = getSections(line);
        List<Long> upStationIds = getUpStationIds(sections);
        List<Long> downStationIds = getDownStationIds(sections);
        downStationIds.removeAll(upStationIds);
        Long downStationId = downStationIds.get(0);
        return sections.stream()
                .map(Section::getDownStation)
                .filter(downStation -> downStation.getId().equals(downStationId))
                .collect(Collectors.toList())
                .get(0);
    }

    private List<Long> getDownStationIds(List<Section> sections) {
        return sections.stream()
                .map(v -> v.getDownStation().getId())
                .collect(Collectors.toList());
    }

    private List<Long> getUpStationIds(List<Section> sections) {
        return sections.stream()
                .map(v -> v.getUpStation().getId())
                .collect(Collectors.toList());
    }

    private Line saveLine(Line line) {
        return lineRepository.save(line);
    }

    private Line findLine(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
    }

}
