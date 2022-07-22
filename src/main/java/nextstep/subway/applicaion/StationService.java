package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        return new StationResponse(stationRepository.save(new Station(stationRequest.getName())));
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream().map(StationResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        if (sectionRepository.existsByUpStation_Id(id) || sectionRepository.existsByDownStation_Id(id)) {
            throw new IllegalArgumentException("station.used.section");
        }
        stationRepository.deleteById(id);
    }

}
