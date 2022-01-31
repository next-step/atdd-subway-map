package nextstep.subway.applicaion.station;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationReadService {
    private final StationRepository stationRepository;

    public StationReadService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    // 도메인에서 바로 Controller 로 넘어가기 때문에 StationResponse 사용
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }

    // 어플리케이션 서비스에서 사용하기 때문에 Station 을 Response 로 사용
    public Station findSpecificStation(Long id){
        return stationRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

}
