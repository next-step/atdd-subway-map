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
        Station upStation = stationRepository.findById(subwayLineRequest.getUpStationId()).orElse(null);
        Station downStation = stationRepository.findById(subwayLineRequest.getDownStationId()).orElse(null);

        SubwayLine line = subwayLineRepository.save(
            new SubwayLine(
                subwayLineRequest.getName(),
                subwayLineRequest.getColor(),
                upStation,
                downStation,
                subwayLineRequest.getDistance()
            ));

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
        final List<StationResponse> stationList = Arrays.stream(
            new Station[]{line.getUpStation(), line.getDownStation()}).map(StationResponse::new).collect(
            Collectors.toList());

        return new SubwayLineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            stationList
        );
    }
}
