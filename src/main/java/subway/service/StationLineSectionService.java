package subway.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.StationLineSection;
import subway.entity.group.StationLineSectionGroup;
import subway.repository.StationLineSectionRepository;

@Service
@Transactional
public class StationLineSectionService {

    private final StationLineSectionRepository stationLineSectionRepository;

    public StationLineSectionService(StationLineSectionRepository stationLineSectionRepository) {
        this.stationLineSectionRepository = stationLineSectionRepository;
    }

    public StationLineSection create(long lineId, long upStationId, long downStationId,
        int distance) {

        return save(lineId, upStationId, downStationId, distance);
    }

    private StationLineSection save(long lineId, long upStationId, long downStationId,
        int distance) {
        return stationLineSectionRepository.save(
            new StationLineSection(
                lineId,
                upStationId,
                downStationId,
                distance
            )
        );
    }

    public StationLineSection add(long lineId, long upStationId, long downStationId,
        int distance) {

        validAddSection(lineId, upStationId, downStationId);

        return save(lineId, upStationId, downStationId, distance);
    }

    private void validAddSection(long lineId, long upStationId, long downStationId) {

        StationLineSectionGroup group = StationLineSectionGroup.of(findAllByLineId(lineId));

        group.validateAdd(upStationId, downStationId);
    }

    public List<StationLineSection> findAllByLineId(Long lineId) {
        Optional<List<StationLineSection>> list = stationLineSectionRepository.findAllByStationLineId(
            lineId);

        if (list.isEmpty()) {
            throw new IllegalArgumentException(lineId + " id 값을 가지는 노선 구간이 존재하지 않습니다.");
        }

        return list.get();
    }

    public void delete(long lineId, long deleteStationId) {

        StationLineSectionGroup group = StationLineSectionGroup.of(findAllByLineId(lineId));

        group.validateDelete(deleteStationId);

        stationLineSectionRepository.delete(group.getEndDownStation());
    }
}
