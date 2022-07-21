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
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.StationNotRegisteredException;
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
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(),
                stations.get(0), stations.get(1), distance));
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
                .ifPresent(line -> line.changeBy(request.getName(), request.getColor()));
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
        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();
        int distance = sectionRequest.getDistance();

        Line line = lineFrom(id);
        List<Station> stations = stationRepository.findByIdInOrderByIdAsc(List.of(upStationId, downStationId));
        Station upStation = stations.get(0);
        Station downStation = stations.get(1);

        Line sectionAddedLine = line.addSection(upStation, downStation, distance);

        return new LineResponse(sectionAddedLine.getId(), sectionAddedLine.getName(),
                sectionAddedLine.getColor(), stationResponses(sectionAddedLine));

    }

    private Line lineFrom(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("노선을 찾을 수 없습니다. : " + id));
    }


    private List<StationResponse> stationResponses(Line line) {
        return line.allStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(toList());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("노선을 찾을 수 없습니다. : " + lineId));
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotRegisteredException("역이 등록되어 있지 않습니다. : " + stationId));
        line.deleteSection(station);
    }
}
