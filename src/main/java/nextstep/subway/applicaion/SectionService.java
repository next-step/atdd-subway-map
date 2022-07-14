package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.line.Section;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse saveSection(Long id, SectionRequest sectionRequest) {
        Line line = getLine(id);

        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();

        validateStation(upStationId);
        validateStation(downStationId);

        Section section = new Section(id, upStationId, downStationId, sectionRequest.getDistance());

        line.getSections().addSection(section);

        return new SectionResponse(id, section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }

    @Transactional
    public void deleteSection(Long id, Long downStationId) {
        Line line = getLine(id);
        line.getSections().deleteSection(downStationId);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException("존재하지 않는 노선에는 구간을 추가할 수 없습니다.")
        );
    }

    private void validateStation(Long stationId) {
        if (!stationRepository.findById(stationId).isPresent()) {
            throw new DataNotFoundException("Station 데이터가 없습니다.");
        }
    }
}
