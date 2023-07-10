package subway.service;

import java.util.List;
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

    public StationLineSection create(Long stationLineId, long upStationId, long downStationId,
        int distance) {

        return stationLineSectionRepository.save(
            new StationLineSection(
                stationLineId,
                upStationId,
                downStationId,
                distance
            )
        );
    }

    public List<StationLineSection> findAllByLineId(Long lineId) {
        return stationLineSectionRepository.findAllByStationLineId(lineId);
    }

    public void validAddSection(long lineId, long upStationId, long downStationId) {

        StationLineSectionGroup group = StationLineSectionGroup.of(findAllByLineId(lineId));

        if (!group.isEqualDownEndStation(upStationId)) {
            throw new IllegalArgumentException("추가하고자 하는 구간의 상행역이, 노선의 하행종점역이 아닙니다.");
        }

        if (group.isExistDownEndStation(downStationId)) {
            throw new IllegalArgumentException("추가하고자 하는 구간의 하행역이 이미 구간에 존재합니다.");
        }
    }
}
