package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.AddLineRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

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
        lineStationRepository.save(new LineStation(1, line, downStation));
        return createLineResponse(line, upStation, downStation);
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
        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이여야한다.
        Station upStation = stationRepository.findById(dto.getUpStationId()).orElseThrow(() -> new RuntimeException("상행선역 지하철을 먼저 등록해주세요"));
        Station downStation = stationRepository.findById(dto.getDownStationId()).orElseThrow(() -> new RuntimeException("하행선역 지하철을 먼저 등록해주세요"));
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("기존에 등록되어 있는 지하철 노선이 아닙니다. 다시 확인해주세요."));
        line.isAddableLine(upStation, downStation);
        lineStationRepository.save(new LineStation(line.getLineStations().size(), line, downStation));
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineDownStation(Long id, Long stationId) {
        List<LineStation> lineStations = lineStationRepository.findAllByLineIdOrderBySequenceDesc(id);
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


    private LineResponse createLineResponse(Line line, Station upStation , Station downStation) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                List.of(new StationResponse(upStation.getId(), upStation.getName()), new StationResponse(downStation.getId(), downStation.getName())));
    }

}
