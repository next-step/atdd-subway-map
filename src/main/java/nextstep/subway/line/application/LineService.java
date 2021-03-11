package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exceptions.*;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.NotFoundStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new ExistingLineException();
        }
        Line line = request.toLineByGetStation(this::getStation);
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                    .stream()
                    .map(LineResponse::of)
                    .collect(Collectors.toList());
    }

    public LineResponse findLine(long id) {
        return lineRepository.findById(id)
                    .map(LineResponse::of)
                    .orElseThrow(NotFoundLineException::new);
    }

    public LineResponse updateLine(LineRequest request, long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(NotFoundLineException::new);
        line.update(request.toLine());
        return LineResponse.of(line);
    }

    public void removeLine(long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(NotFoundLineException::new);

        lineRepository.delete(line);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        getLine(lineId).addStation(
            request.getDistance(),
            getStation(request.getUpStationId()),
            getStation(request.getDownStationId())
        );
    }

    public void removeLineStation(Long lineId, Long stationId) {
        getLine(lineId).removeStation(stationId);
    }

    private Line getLine(long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(NotFoundLineException::new);
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(NotFoundStationException::new);
    }

}
