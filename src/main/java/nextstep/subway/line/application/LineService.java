package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse save(LineRequest request) {
        Station upStation = stationService.find(request.getUpStationId());
        Station downStation = stationService.find(request.getDownStationId());
        Line persistLine = lineRepository.save(new Line(request.getName(),
                                                        request.getColor(),
                                                        upStation, downStation, request.getDistance()));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                    .map(LineResponse::of)
                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse find(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchLineException::new);
        return LineResponse.of(line);
    }

    public void update(Long id, LineRequest request) {
        lineRepository.findById(id).orElseThrow(NoSuchLineException::new);
        lineRepository.save(request.toLine());
    }

    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    public void deleteStationFromLine(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(NoSuchLineException::new);
        line.deleteStation(stationId);
    }

    public void addSectionToLine(Long id, SectionRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchLineException::new);
        Station upStation = stationService.find(request.getUpStationId());
        Station downStation = stationService.find(request.getDownStationId());
        line.addSectionToLine(upStation, downStation, request.getDistance());
    }
}
