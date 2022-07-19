package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.LineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.line.LineUpdateRequest;
import nextstep.subway.applicaion.dto.line.SectionRequest;
import nextstep.subway.applicaion.dto.station.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.AlreadyExistStationException;
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.SectionStationMismatchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Long upStationId = lineRequest.getUpStationId();
        Long downStationId = lineRequest.getDownStationId();
        int distance = lineRequest.getDistance();
        List<Station> stations = stationRepository.findByIdInOrderByIdAsc(List.of(upStationId, downStationId));
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), stations.get(0), stations.get(1), distance));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(toList());
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id)
                .map(this::createLineResponse)
                .orElseGet(LineResponse::new);
    }

    @Transactional
    public void updateLineById(Long id, LineUpdateRequest request) {
        lineRepository.findById(id)
                .ifPresent(line -> lineRepository.save(line.changeBy(request.getName(), request.getColor())));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses(line));
    }

    @Transactional
    public LineResponse addSections(Long id, SectionRequest sectionRequest) {
        String downStationId = sectionRequest.getDownStationId();
        String upStationId = sectionRequest.getUpStationId();
        int distance = sectionRequest.getDistance();

        Line line = lineFrom(id);
        validateSection(downStationId, upStationId, line);
        List<Long> stationsIds = List.of(Long.parseLong(upStationId), Long.parseLong(downStationId));
        List<Station> stations = stationRepository.findByIdInOrderByIdAsc(stationsIds);
        Line sectionAddedLine = line.addSection(stations, distance);

        return new LineResponse(sectionAddedLine.getId(), sectionAddedLine.getName(),
                sectionAddedLine.getColor(), stationResponses(sectionAddedLine));

    }

    private Line lineFrom(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("노선을 찾을 수 없습니다. : " + id));
    }


    private void validateSection(String downStationId, String upStationId, Line line) {
        Station station = line.lastStation();
        if (!station.equalsId(Long.parseLong(upStationId))) {
            throw new SectionStationMismatchException("노선의 하행 마지막역과 추가되는 구간의 상행역이 달라 추가될 수 없습니다. 하행 마지막 역 : "
                    + station.getId() + ", 구간 상행역 : " + upStationId);
        }

        if (line.hasStation(Long.parseLong(downStationId))) {
            throw new AlreadyExistStationException("이미 존재하는 역입니다.");
        }
    }

    private List<StationResponse> stationResponses(Line line) {
        return line.allStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(toList());
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("노선을 찾을 수 없습니다. : " + id));
        line.deleteSection(stationId);
    }
}
