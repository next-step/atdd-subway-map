package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayLineService {

    private SubwayLineRepository subwayLineRepository;
    private StationRepository stationRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository, StationRepository stationRepository) {
        this.subwayLineRepository = subwayLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SubwayLineResponse createLine(SubwayLineRequest subwayLineRequest) {
        Station upStation = stationRepository.findById(subwayLineRequest.getUpStationId())
                .orElseThrow();
        Station downStation = stationRepository.findById(subwayLineRequest.getDownStationId())
                .orElseThrow();

        List<Station> stations = Arrays.asList(upStation, downStation);
        SubwayLine subwayLine = subwayLineRepository.save(new SubwayLine(subwayLineRequest.getName()
                , subwayLineRequest.getColor(), stations));

        return createLineResponse(subwayLine);
    }

    private SubwayLineResponse createLineResponse(SubwayLine subwayLine) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getStations()
        );
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }
}