package subway;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LinesService {

    private LinesRepository linesRepository;
    private StationRepository stationRepository;

    public LinesService(LinesRepository linesRepository, StationRepository stationRepository) {
        this.linesRepository = linesRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LinesResponse saveLines(LinesCreateRequest linesCreateRequest) {
        final Station upStation = stationRepository
            .findById(linesCreateRequest.getUpStationId())
            .orElse(null);
        final Station downStation = stationRepository
            .findById(linesCreateRequest.getDownStationId())
            .orElse(null);

        final Lines lines = linesRepository.save(
            new Lines(linesCreateRequest.getName(), linesCreateRequest.getColor(), upStation,
                downStation, linesCreateRequest.getDistance()));

        return new LinesResponse(lines);
    }

    public List<LinesResponse> getLinesList() {
        return linesRepository.findAll().stream().map(LinesResponse::new).collect(Collectors.toList());
    }

    public LinesResponse getLines(Long id) {
        final Lines lines = linesRepository.findById(id).orElse(null);
        if(lines == null) {
            return null;
        }

        return new LinesResponse(lines);
    }

    public void updateLines(Long id, LinesUpdateRequest linesUpdateRequest) {
        final Lines lines = linesRepository.findById(id).orElse(null);
        if(lines== null) {
            throw new EntityNotFoundException();
        }

        lines.setName(linesUpdateRequest.getName());
        lines.setColor(linesUpdateRequest.getColor());
    }
}
