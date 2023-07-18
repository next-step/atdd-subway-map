package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("상행종점역을 찾을 수 없습니다. (upStationId: %d)", request.getUpStationId())));

        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("하행종점역을 찾을 수 없습니다. (downStationId: %d)", request.getDownStationId())));

        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));

        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return LineResponse.of(lineRepository.findAll());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("지하철 노선을 찾을 수 없습니다. (id: %d)", id)));

        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("지하철 노선을 찾을 수 없습니다. (id: %d)", id)));

        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("상행종점역을 찾을 수 없습니다. (upStationId: %d)", request.getUpStationId())));

        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("하행종점역을 찾을 수 없습니다. (downStationId: %d)", request.getDownStationId())));

        line.update(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public Long saveSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalStateException(String.format("지하철 노선을 찾을 수 없습니다. (id: %d)", lineId)));

        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("노선의 하행종점역을 찾을 수 없습니다. (downStationId: %d)", request.getUpStationId())));

        if (!line.getDownStationId().equals(upStation.getId())) {
            throw new IllegalStateException(String.format("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닙니다. (구간의 upStationId: %d)", request.getUpStationId()));
        }

        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalStateException(String.format("새로운 하행종점역을 찾을 수 없습니다. (downStationId: %d)", request.getDownStationId())));

        if (line.contains(downStation)) {
            throw new IllegalStateException(String.format("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다. (downStationId: %d)", downStation.getId()));
        }

        Section section = new Section(line, upStation, downStation, request.getDistance());
        line.addTerminalSection(section);

        return section.getId();
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalStateException(String.format("지하철 노선을 찾을 수 없습니다. (id: %d)", lineId)));

        line.deleteSectionByDownStationId(stationId);
    }
}
