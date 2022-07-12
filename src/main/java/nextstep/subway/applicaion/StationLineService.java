package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private StationLineRepository stationLineRepository;
    private StationRepository stationRepository;

    public StationLineService(StationLineRepository stationLineRepository, StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest stationLineRequest) {
        StationLine stationLine = stationLineRepository.save(stationLineRequest.toEntity());
        return createStationLineResponse(stationLine);
    }

    public StationLineResponse findStationLine(Long id) {
        StationLine stationLine = stationLineRepository.findById(id)
                .orElseThrow();
        return createStationLineResponse(stationLine);
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll().stream()
                .map(this::createStationLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationLineById(Long id) {
        stationLineRepository.deleteById(id);
    }

    @Transactional
    public void updateStationLine(Long id, StationLineRequest stationLineRequest) {
        StationLine existedStationLine = stationLineRepository.findById(id)
                .orElseThrow();
        stationLineRequest.updateExistedStationLine(existedStationLine);
    }

    private StationLineResponse createStationLineResponse(StationLine stationLine) {
        List<Station> stations = findRelatedStations(stationLine);

        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stations
        );
    }

    private List<Station> findRelatedStations(StationLine stationLine) {
        List<Station> stations = new ArrayList<>();
        Station upStation = stationRepository.findById(stationLine.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(stationLine.getDownStationId()).orElseThrow();
        stations.add(upStation);
        stations.add(downStation);
        return stations;
    }
}
