package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;

    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.getStationById(request.getUpStationId());
        Station downStation = stationService.getStationById(request.getDownStationId());
        Line line = new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
        Line persistLine = lineRepository.save(line);
        return lineToLineResponse(persistLine);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = getLineById(id);
        LineResponse lineResponse = lineToLineResponse(line);

        return lineResponse;
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = getLineById(id);
        line.update(request.toLine());
        return lineToLineResponse(line);
    }

    public Line getLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse lineToLineResponse(Line line) {
        return LineResponse.of(line);
    }

    public LineResponse createSection(Long lineId, SectionRequest request) {
        Line line = getLineById(lineId);
        Station upStation = stationService.getStationById(request.getUpStationId());
        Station downStation = stationService.getStationById(request.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
        return LineResponse.of(line);
    }

    public void deleteSection(Long lineId, Long stationId){
        Optional<Line> optLine = lineRepository.findById(lineId);
        Station station = stationService.getStationById(stationId);
        Line line = optLine.orElseThrow(() -> new NoSuchElementException());
        line.deleteSection(station);
    }
}
