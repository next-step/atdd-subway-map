package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository,
                          StationRepository stationRepository,
                          LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public SectionResponse saveSection(SectionRequest sectionRequest, Long lineId) {
        final Long downStationId = sectionRequest.getDownStationId();
        final Long upStationId = sectionRequest.getUpStationId();

        checkExistingStation(downStationId, upStationId);

        final Line foundLine = lineRepository.getById(lineId);
        validateStationInSection(downStationId, upStationId, foundLine);

        final Section section = sectionRepository.save(
                new Section(
                        lineId,
                        downStationId,
                        upStationId,
                        sectionRequest.getDistance()));

        return new SectionResponse(section.getId(),
                section.getLineId(),
                section.getDownStationId(),
                section.getUpStationId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate()
        );
    }

    private void validateStationInSection(Long downStationId, Long upStationId, Line foundLine) {
        checkUpStation(upStationId, foundLine);
        checkDownStation(downStationId, foundLine);
    }

    private void checkDownStation(Long downStationId, Line foundLine) {
        if (downStationId.equals(foundLine.getUpStationId())
                || downStationId.equals(foundLine.getDownStationId())) {
            throw new IllegalArgumentException("새로운 하행종점역은 노선에 등록되지 않은 역만 가능합니다.");
        }
    }

    private void checkUpStation(Long upStationId, Line foundLine) {
        if (!upStationId.equals(foundLine.getDownStationId())) {
            throw new IllegalArgumentException("등록할 상행종점역은 노선의 하행종점역이어야 합니다.");
        }
    }

    private void checkExistingStation(Long downStationId, Long upStationId) {
        if (!stationRepository.existsById(downStationId)) {
            throw new NotFoundException(downStationId);
        }

        if (!stationRepository.existsById(upStationId)) {
            throw  new NotFoundException(upStationId);
        }
    }
}
