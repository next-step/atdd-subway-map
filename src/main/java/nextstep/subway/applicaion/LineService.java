package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.CreateLineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class LineService {
    public static final int STATION_IN_LINE_INIT_NUM = 2;

    private final LineRepository stationLineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository stationLineRepository, final StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveStationLine(CreateLineRequest request) {
        Line line = request.toEntity();

        List<Station> stations = stationRepository.findAllById(List.of(request.getUpStationId(), request.getDownStationId()));
        if(stations.size() != STATION_IN_LINE_INIT_NUM){
            throw new IllegalStateException("지하철역이 존재하지 않습니다.");
        }
        stationLineRepository.save(line);

        return LineResponse.of(line, stations);
    }
}
