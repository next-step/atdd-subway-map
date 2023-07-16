package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.AddLineRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private LineStationRepository  lineStationRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, LineStationRepository lineStationRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.lineStationRepository = lineStationRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), request.getDistance()));
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(() -> new NoSuchFieldError("해당 지하철역이 없습니다."));
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(() -> new NoSuchFieldError("해당 지하철역이 없습니다."));
        lineStationRepository.save(new LineStation(0, line, upStation));
        lineStationRepository.save(new LineStation(request.getDistance() - 1, line, downStation));
        return createLineResponse(line, upStation, downStation);
    }

    private LineResponse createLineResponse(Line line, Station upStation , Station downStation) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                List.of(new StationResponse(upStation.getId(), upStation.getName()), new StationResponse(downStation.getId(), downStation.getName())));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 아이디의 지하철 노선이 없습니다."));
        return createLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 아이디의 지하철 노선이 없습니다."));
        line.updateLine(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addLine(Long id, AddLineRequest dto) {
        Station upStation = stationRepository.findById(dto.getUpStationId()).orElseThrow(() -> new RuntimeException("상행선역 지하철을 먼저 등록해주세요"));
        List<LineStation> lines = lineStationRepository.findAllByLineIdOrderBySequenceDesc(id);
        Station prevDownStation = lines.stream().findFirst().orElseThrow(() -> new RuntimeException("해당하는 지하철 노선 id가 없습니다.")).getStation();
        dto.compareUpStationId(prevDownStation.getId());
        LineStation lineStation = lines.stream()
                .max(Comparator.comparing(LineStation::getSequence))
                .orElseThrow(() -> new RuntimeException("해당하는 지하철 노선의 max sequence 값이 없습니다."));
        LineStation save = lineStationRepository.save(new LineStation(lineStation.getSequence() + dto.getDistance() - 1,
                lineStation.getLine(),
                upStation));
        return createLineResponse(save.getLine());
    }

    public void deleteLineDownStation(Long id, Long stationId) {
        List<LineStation> lineStations = lineStationRepository.findAllByLineIdOrderBySequenceDesc(id);
        isAbleDelete(lineStations, stationId);
        lineStationRepository.deleteByLineIdAndStationId(id, stationId);
    }

    private StationResponse createStationResponse(LineStation lineStation) {
        return new StationResponse(lineStation.getStation().getId(), lineStation.getStation().getName());
    }

    private LineResponse createLineResponse(Line line) {
        LineStation up = lineStationRepository.findFirstByLineIdOrderBySequence(line.getId());
        LineStation down = lineStationRepository.findFirstByLineIdOrderBySequenceDesc(line.getId());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(createStationResponse(up), createStationResponse(down)));
    }

    private void isAbleDelete(List<LineStation> lineStations, Long requestId) {
        if(lineStations.size() == 2) {
            throw new RuntimeException("구간이 1개인 경우 역을 삭제할 수 없습니다.");
        }

        LineStation lineStation = lineStations.stream().max(Comparator.comparing(LineStation::getSequence)).get();
        if(!requestId.equals(lineStation.getStation().getId())) {
            throw new RuntimeException("지하철 노선의 하행 종점역만 제거할 수 있습니다.");
        }
    }
}
