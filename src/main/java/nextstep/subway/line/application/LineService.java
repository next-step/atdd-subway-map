package nextstep.subway.line.application;

import nextstep.subway.error.NameExistsException;
import nextstep.subway.error.NotFoundException;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public LineResponse saveLine(LineRequest request) {
        if (checkExistsName(request.getName())) {
            throw new NameExistsException(request.getName());
        }
        return LineResponse.of(addSectionsAndCreateLine(request));
    }

    private Line addSectionsAndCreateLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        Station upStation = stationService.findByStation(request.getUpStationId());
        Station downStation = stationService.findByStation(request.getDownStationId());

        Section section = Section.Builder.aSection()
                .line(persistLine)
                .upStation(upStation)
                .downStation(downStation)
                .distance(new Distance(request.getDistance()))
                .build();

        persistLine.getSections().addSection(section);
        return lineRepository.save(persistLine);
    }

    private boolean checkExistsName(String name) {
        return lineRepository.findByName(name).isPresent();
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        return LineResponse.of(line);
    }

    private boolean checkExistsName(String name) {
        return lineRepository.findByName(name).isPresent();
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLine() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void updateLineById(LineRequest lineRequest, Long lineId) {
        Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));

        persistLine.update(lineRepository.save(lineRequest.toLine()));
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long lineId) {
        Line persistLine = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(lineId));
        lineRepository.delete(persistLine);
    }

}
