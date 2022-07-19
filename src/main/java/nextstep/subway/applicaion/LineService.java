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

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = saveLine(lineRequest.toEntity());
        Line savedLine = findLine(line.getId());
        Section section = saveSection(savedLine, lineRequest);
        savedLine.addSection(section);
        return LineResponse.convertedByEntity(savedLine);
    }


    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::convertedByEntity)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(long lineId) {
        Line line = findLine(lineId);
        return LineResponse.convertedByEntity(line);
    }

    @Transactional
    public LineResponse updateLine(LineRequest lineRequest, long lineId) {
        Line line = findLine(lineId);
        line.changeNameAndColor(lineRequest);
        Line updatedLine = saveLine(line);
        return LineResponse.convertedByEntity(updatedLine);
    }

    @Transactional
    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }

    public List<SectionResponse> getSections(long lineId) {
        List<Section> sections = findLine(lineId).getSections();
        return sections.stream()
                .map(SectionResponse::convertedByEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SectionResponse registerSection(long lineId, SectionRequest sectionRequest) {
        Line line = findLine(lineId);
        Long upStationId = sectionRequest.getUpStationId();
        Long downEndpointStationId = line.getDownEndpoint().getId();
        line.checkRegisterEndpointId(upStationId, downEndpointStationId);
        saveSection(line, sectionRequest);
        Section section = line.getSections().get(line.getSections().size() - 1);
        return SectionResponse.convertedByEntity(section);
    }

    @Transactional
    public void removeSection(long lineId, long stationId) {
        Line line = findLine(lineId);
        line.checkRemoveEndPointId(stationId, line.getDownEndpoint().getId());
        Section.checkSectionCount(sectionRepository.count());
        line.getSections().removeIf(v->v.getDownStation().getId()==stationId);
    }

    private void saveSection(Line line, SectionRequest sectionRequest) {
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        line.addSection(section);
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
        line.addSection(section);
    }

    private Line saveLine(Line line) {
        return lineRepository.save(line);
    }

    private Line findLine(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
    }

}
