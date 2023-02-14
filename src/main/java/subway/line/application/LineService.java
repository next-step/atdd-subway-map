package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.section.domain.Section;
import subway.line.section.dto.SectionRequest;
import subway.line.section.dto.SectionResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationResponse;

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
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());

        Line line = lineRepository.save(new Line(
            lineRequest.getName(),
            lineRequest.getColor(),
            upStation,
            downStation,
            lineRequest.getDistance())
        );

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return createLineResponse(
            getLine(id)
        );
    }

    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = getLine(id);

        line.changeLine(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = getLine(lineId);
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());

        Section section = new Section(
            upStation,
            downStation,
            line, sectionRequest.getDistance());

        line.createSection(section);

        return createSectionResponse(section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        Station station = getStation(stationId);

        line.deleteSection(station);
    }

    private Line getLine(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.LINE_NOT_FOUND));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(
            section.getId(),
            List.of(new StationResponse(section.getUpStation().getId(), section.getUpStation().getName()),
                new StationResponse(section.getDownStation().getId(), section.getDownStation().getName()))
        );
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            line.getAllStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList())
        );
    }
}
