package subway.line.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.business.constant.LineConstants;
import subway.line.repository.LineRepository;
import subway.line.repository.entity.Line;
import subway.line.web.dto.LineRequest;
import subway.line.web.dto.LineResponse;
import subway.line.web.dto.SectionRequest;
import subway.line.repository.entity.Section;
import subway.line.repository.entity.Sections;
import subway.station.business.StationService;
import subway.station.repository.entity.Station;
import subway.station.web.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;

    private final LineRepository lineRepository;

    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());

        Sections sections = new Sections(List.of(new Section(upStation, downStation, request.getDistance())));
        Line newLine = lineRepository.save(new Line(request.getName(), request.getColor(), sections));

        return toLineResponse(newLine);
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll().stream().map(this::toLineResponse).collect(Collectors.toList());
    }

    public LineResponse getLineResponse(Long lineId) {
        return toLineResponse(getLine(lineId));
    }

    public Line getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException(LineConstants.LINE_NOT_EXIST));
    }

    @Transactional
    public void modify(Long id, String name, String color) {
        Line entity = lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException(LineConstants.LINE_NOT_EXIST));
        lineRepository.save(entity.modify(name, color));
    }

    @Transactional
    public void remove(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest request) {
        Line line = getLine(lineId);
        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());

        line.addSection(new Section(upStation, downStation, request.getDistance()));
        lineRepository.save(line);
    }

    private LineResponse toLineResponse(Line line) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station station : line.getAllStations()) {
            stationResponses.add(stationService.toStationResponse(station));
        }

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }

}
