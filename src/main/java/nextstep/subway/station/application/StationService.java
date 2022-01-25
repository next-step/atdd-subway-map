package nextstep.subway.station.application;

import nextstep.subway.section.application.SectionService;
import nextstep.subway.station.application.dto.StationRequest;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.common.ErrorMessages.DUPLICATE_STATION_NAME;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;
    private final SectionService sectionService;

    public StationService(StationRepository stationRepository, SectionService sectionService) {
        this.stationRepository = stationRepository;
        this.sectionService = sectionService;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        checkExistsStationName(stationRequest.getName());

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        Assert.isTrue(!sectionService.isExistStation(id), "구간에 사용중인 지하철역은 삭제할 수 없습니다.");
        stationRepository.deleteById(id);
    }

    private void checkExistsStationName(String name) {
        Assert.isTrue(!stationRepository.existsByName(name), DUPLICATE_STATION_NAME.getMessage());
    }
}
