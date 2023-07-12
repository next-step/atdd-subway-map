package subway.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.Section;
import subway.entity.group.SectionGroup;
import subway.repository.SectionRepository;

@Service
@Transactional
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section create(long lineId, long upStationId, long downStationId,
        int distance) {

        return save(lineId, upStationId, downStationId, distance);
    }

    private Section save(long lineId, long upStationId, long downStationId,
        int distance) {
        return sectionRepository.save(
            new Section(
                lineId,
                upStationId,
                downStationId,
                distance
            )
        );
    }

    public Section add(long lineId, long upStationId, long downStationId,
        int distance) {

        validAddSection(lineId, upStationId, downStationId);

        return save(lineId, upStationId, downStationId, distance);
    }

    private void validAddSection(long lineId, long upStationId, long downStationId) {

        SectionGroup group = SectionGroup.of(findAllByLineId(lineId));

        group.validateAdd(upStationId, downStationId);
    }

    public List<Section> findAllByLineId(Long lineId) {
        Optional<List<Section>> list = sectionRepository.findAllByStationLineId(
            lineId);

        if (list.isEmpty()) {
            throw new IllegalArgumentException(lineId + " id 값을 가지는 노선 구간이 존재하지 않습니다.");
        }

        return list.get();
    }

    public void delete(long lineId, long deleteStationId) {

        SectionGroup group = SectionGroup.of(findAllByLineId(lineId));

        group.validateDelete(deleteStationId);

        sectionRepository.delete(group.getEndDownStation());
    }
}
