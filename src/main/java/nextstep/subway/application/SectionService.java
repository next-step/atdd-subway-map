package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.exception.SectionException.ONLY_LAST_STATION_OF_LINE_CAN_BE_DELETED;
import static nextstep.subway.exception.SectionException.SECTION_CANNOT_BE_DELETED_WHEN_LINE_HAS_ONLY_ONE_SECTION;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineService lineService;
    private final StationService stationService;

    public SectionResponse addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineService.findLineBy(lineId);
        // TODO merge 활용
        Station upStation = stationService.findBy(sectionRequest.getUpStationId());
        Station downStation = stationService.findBy(sectionRequest.getDownStationId());
        Section section = sectionRepository.save(new Section(line, upStation, downStation));
        return SectionResponse.of(section);
    }

    public void deleteBy(Long lineId, Long stationId) {
        Line line = lineService.findLineBy(lineId);

        validateDeleteSectionRequest(line, stationId);

        line.removeLastSection();
    }

    private void validateDeleteSectionRequest(Line line, Long stationId) {
        if (line.hasOnlyOneSection()) {
            throw new BadRequestException(SECTION_CANNOT_BE_DELETED_WHEN_LINE_HAS_ONLY_ONE_SECTION);
        }

        Station station = stationService.findBy(stationId);
        if (!line.isDownStation(station)) {
            throw new BadRequestException(ONLY_LAST_STATION_OF_LINE_CAN_BE_DELETED);
        }
    }
}
