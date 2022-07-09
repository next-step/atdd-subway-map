package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationLineModifyRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationLineSaveRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private static final String NOT_FOUND_STATION_LINE = "not found station line by ";
    private static final String NOT_FOUND_STATION = "not found station by";

    private final StationRepository stationRepository;
    private final StationLineRepository stationLineRepository;

    public StationLineService(StationRepository stationRepository, StationLineRepository stationLineRepository) {
        this.stationRepository = stationRepository;
        this.stationLineRepository = stationLineRepository;
    }

    public List<StationLineResponse> getStationLines() {
        List<StationLine> all = stationLineRepository.findAll();
        return all.stream()
                .map(this::createStationLineResponse)
                .collect(Collectors.toList());
    }

    public StationLineResponse getStationLineById(Long id) {
        return stationLineRepository.findById(id)
                .map(this::createStationLineResponse)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_STATION_LINE + id));
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineSaveRequest stationLineSaveRequest) {
        StationLine stationLine = stationLineSaveRequest.toEntity();
        StationLine save = stationLineRepository.save(stationLine);
        return createStationLineResponse(save);
    }

    @Transactional
    public void modifyStationLine(Long id, StationLineModifyRequest stationLineModifyRequest) {
        StationLine stationLine = stationLineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_STATION_LINE + id));
        stationLine.modify(stationLineModifyRequest.getName(), stationLineModifyRequest.getColor());
        stationLineRepository.save(stationLine);
    }

    @Transactional
    public void deleteStationLine(Long id) {
        stationLineRepository.deleteById(id);
    }

    private StationLineResponse createStationLineResponse(StationLine stationLine) {
        StationResponse upStation = createStationResponse(stationLine.getUpStationId());
        StationResponse downStation = createStationResponse(stationLine.getDownStationId());
        return new StationLineResponse(stationLine, upStation, downStation);
    }

    private StationResponse createStationResponse(Long id) {
        return stationRepository.findById(id)
                .map(StationResponse::new)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_STATION + id));
    }
}
