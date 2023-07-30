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
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(() -> new NoSuchFieldError("해당 지하철역이 없습니다."));
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(() -> new NoSuchFieldError("해당 지하철역이 없습니다."));
        line.createLineStation(upStation, downStation, request.getDistance());
        return createLineResponse(line);
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
    public LineResponse addLineStation(Long id, AddLineRequest dto) {
        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이여야한다.
        Station upStation = stationRepository.findById(dto.getUpStationId()).orElseThrow(() -> new RuntimeException("상행선역 지하철을 먼저 등록해주세요"));
        Station downStation = stationRepository.findById(dto.getDownStationId()).orElseThrow(() -> new RuntimeException("하행선역 지하철을 먼저 등록해주세요"));
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("기존에 등록되어 있는 지하철 노선이 아닙니다. 다시 확인해주세요."));
        line.isAddableLine(upStation, downStation);
        line.addLineStation(upStation, downStation, dto.getDistance());
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineDownStation(Long id, Long stationId) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 노선은 등록되어 있지 않습니다."));
        line.isDeletableLine(stationId);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = line.getUpStation();
        Station downStation = line.getDownStation();
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(createStationResponse(upStation), createStationResponse(downStation)));
    }

    private LineResponse createLineResponse(Line line, List<LineStation> lineStations) {
        Station upStation = line.getUpStation();
        Station downStation = line.getDownStation();
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(createStationResponse(upStation), createStationResponse(downStation)));
    }


}
