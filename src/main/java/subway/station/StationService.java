package subway.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.commons.ErrorCode;
import subway.commons.HttpException;
import subway.section.SectionCreateDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public StationResponse findStation(Long id) {
        Station station = stationRepository.findById(id).orElseThrow(() -> new HttpException(ErrorCode.BAD_REQUEST));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public void validateSectionCreate(SectionCreateDTO dto) {
        Station upStation = stationRepository.findById(dto.getUpStationId())
                .orElseThrow(() -> new HttpException(ErrorCode.UP_STATION_NOT_VALID));

        Station downStation = stationRepository.findById(dto.getDownStationId())
                .orElseThrow(() -> new HttpException(ErrorCode.DOWN_STATION_NOT_VALID, dto.getDownStationId().toString()));
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
