package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class StationLineService {

    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;
    private final StationLineMapper stationLineMapper;

    public StationLineService(StationLineRepository stationLineRepository,
                              StationRepository stationRepository,
                              StationLineMapper stationLineMapper) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
        this.stationLineMapper = stationLineMapper;
    }

    public StationLineResponse createStationLine(StationLineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("지하철역이 존재하지 않습니다."));
        StationLine stationLine = stationLineMapper.of(request, upStation, downStation);
        StationLine savedStationLine = stationLineRepository.save(stationLine);
        return stationLineMapper.of(savedStationLine);
    }

    public List<StationLineResponse> getStationLines() {
        List<StationLine> stationLines = stationLineRepository.findAll();
        return stationLines.stream()
                .map(stationLineMapper::of)
                .collect(Collectors.toList());
    }

    public StationLineResponse getStationLine(Long lineId) {
        StationLine stationLine = stationLineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("지하철노선이 존재하지 않습니다."));
        return stationLineMapper.of(stationLine);
    }

    public void updateStationLine(Long lineId, StationLineRequest request) {
        StationLine stationLine = stationLineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("지하철노선이 존재하지 않습니다."));
        stationLine.changeNameAndColor(request.getName(),request.getColor());
    }

    public void deleteStationLine(Long lineId) {
        stationRepository.deleteById(lineId);
    }
}