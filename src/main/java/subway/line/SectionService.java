package subway.line;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import subway.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {

    private final StationService stationService;
    private final LineService lineService;

    public void addSection(final Long lineId, final SectionAddRequest request) {

        final var line = lineService.getById(lineId);

        final var upStation = stationService.getById(request.getUpStationId());
        final var downStation = stationService.getById(request.getDownStationId());

        if(!line.isLastStation(upStation)) {
            throw new NotLastDownStationException();
        }

        if(line.anyMatchStation(downStation)) {
            throw new DuplicateSectionStationException();
        }

        line.addLineStation(createSection(request, line, upStation, downStation));
    }

    private static Section createSection(
            final SectionAddRequest request,
            final Line line,
            final Station upStationId,
            final Station downStationId
    ) {

        return Section.builder()
                .line(line)
                .upStation(upStationId)
                .downStation(downStationId)
                .distance(request.getDistance())
                .build();
    }
}
