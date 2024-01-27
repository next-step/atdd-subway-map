package subway;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SubwayLineResponse saveLine(SubwayLineCreateRequest subwayLineRequest) {
        SubwayLine line = subwayLineRepository.save(new SubwayLine(subwayLineRequest.getName(), subwayLineRequest.getColor()));

        List<Station> stations = stationRepository.findAllById(
            Arrays.asList(subwayLineRequest.getUpStationId(),
                subwayLineRequest.getDownStationId()));
        System.out.println(stations);
        stations.forEach(station -> station.setSubwayLine(line));

        return createSubwayLineResponse(line);
    }

    public List<SubwayLineResponse> findAllLines() {
        return subwayLineRepository.findAll().stream().map(this::createSubwayLineResponse).collect(
            Collectors.toList());
    }

    public SubwayLineResponse findLine(Long id) {
        final SubwayLine line = subwayLineRepository.findById(id).orElse(null);
        if(line == null) {
            return null;
        }

        return createSubwayLineResponse(line);
    }

    @Transactional
    public SubwayLineResponse updateLine(Long id, SubwayLineRequest subwayLineRequest) {
        SubwayLine line = subwayLineRepository.findById(id).orElse(null);
        if(line == null) {
            throw new EntityNotFoundException();
        }

        line.setName(subwayLineRequest.getName());

        return createSubwayLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        subwayLineRepository.deleteById(id);
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine line) {
        return new SubwayLineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            line.getStations().stream().map(StationResponse::new).collect(Collectors.toList())
        );
    }
}
