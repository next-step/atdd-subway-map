package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLine(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow();
        return createLineResponse(subwayLine);
    }

    @Transactional
    public void updateSubwayLine(Long id, SubwayLineUpdateRequest subwayLineUpdateRequest) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow();

        subwayLine.updateName(subwayLineUpdateRequest.getName());
        subwayLine.updateColor(subwayLineUpdateRequest.getColor());
    }


    @Transactional
    public void delete(Long id) { subwayLineRepository.deleteById(id); }

    private SubwayLineResponse createLineResponse(SubwayLine subwayLine) {
        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                subwayLine.getStations()
        );
    }
}