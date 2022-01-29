package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.LineException;
import nextstep.subway.exception.StationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private static String DUPLICATE_NAME_ERROR_MASSAGE = "사용중인 노선 이름입니다.";
    private static String NOT_FIND_MASSAGE = "존재하지 않습니다.";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new LineException(DUPLICATE_NAME_ERROR_MASSAGE);
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));

        line.addSection(createSection(line, request));
        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).get();
        return createLineResponse(line);
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void saveSection(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();

        line.addSection(createSection(line, lineRequest));
    }

    public void deleteSectionById(Long id, Long stationId) {
        Line line = lineRepository.findById(id).get();
        Station station = stationRepository.findById(stationId).get();
        line.deleteSectionById(station);
    }

    private Section createSection(Line line, LineRequest lineRequest) {
        Station upStation = findStationById(lineRequest.getUpStationId());
        Station downStation = findStationById(lineRequest.getDownStationId());

        return new Section(
                line,
                upStation,
                downStation,
                lineRequest.getDistance()
        );
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new StationException(NOT_FIND_MASSAGE));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations()
                        .stream()
                        .map(this::createStationResponse)
                        .collect(Collectors.toList()),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getCreatedDate(),
                station.getModifiedDate()
        );
    }
}
