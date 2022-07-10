package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.stationLine.CreateStationLineRequest;
import nextstep.subway.applicaion.dto.stationLine.CreateStationLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class StationLineService {
    public static final int SAVE_LINESTATION_NUM = 2;
    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;

    public StationLineService(final StationLineRepository stationLineRepository, final StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    public CreateStationLineResponse saveStationLine(CreateStationLineRequest request) {
        StationLine stationLine = request.toEntity();

        List<Station> stations = stationRepository.findAllById(List.of(request.getUpStationId(), request.getDownStationId()));
        if(stations.size() != SAVE_LINESTATION_NUM){
            throw new IllegalStateException("지하철역이 존재하지 않습니다.");
        }
        stationLineRepository.save(stationLine);

        return CreateStationLineResponse.of(stationLine, stations);
    }
}
