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

    public SectionResponse saveSection(SectionRequest sectionRequest, long lineId) {
        final long downStationId = sectionRequest.getDownStationId();
        final long upStationId = sectionRequest.getUpStationId();

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

    private void validateStationInSection(long downStationId, long upStationId, Line foundLine) {
        checkUpStation(upStationId, foundLine);
        checkDownStation(downStationId, foundLine);
    }

    private void checkDownStation(long downStationId, Line foundLine) {
        if (foundLine.getUpStationId().equals(downStationId)
                || foundLine.getDownStationId().equals(downStationId)) {
            throw new IllegalArgumentException("등록할 하행종점역은 노선에 등록되지 않은 역만 가능합니다.");
        }
    }

    private void checkUpStation(long upStationId, Line foundLine) {
        if (!foundLine.getDownStationId().equals(upStationId)) {
            throw new IllegalArgumentException("등록할 상행종점역은 노선의 하행종점역이어야 합니다.");
        }
    }

    private void checkExistingStation(long downStationId, long upStationId) {
        if (!stationRepository.existsById(downStationId)) {
            throw new NotFoundException(downStationId);
        }

        if (!stationRepository.existsById(upStationId)) {
            throw  new NotFoundException(upStationId);
        }
    }
}
