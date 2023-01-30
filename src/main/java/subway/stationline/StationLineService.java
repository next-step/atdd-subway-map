package subway.stationline;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private final StationLineRepository stationLineRepository;

    public StationLineService(StationLineRepository stationLineRepository) {
        this.stationLineRepository = stationLineRepository;
    }




}
