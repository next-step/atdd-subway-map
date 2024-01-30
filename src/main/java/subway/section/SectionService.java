package subway.section;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@Transactional(readOnly = true)
public class SectionService {
    public static final String EMPTY_LINE_MSG = "존재하지 않는 노선 입니다.";
    public static final String EMPTY_UP_STATION_MSG = "존재 하지 않는 상행종점역 입니다.";
    public static final String EMPTY_DOWN_STATION_MSG = "존재 하지 않는 하행종점역 입니다.";

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(final SectionRepository sectionRepository, final LineRepository lineRepository,
                          final StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse saveSection(final Long lineId, final SectionRequest sectionRequest) {
        final Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EMPTY_LINE_MSG));

        final Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException(EMPTY_UP_STATION_MSG));
        final Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException(EMPTY_DOWN_STATION_MSG));
        line.registerValidate(upStation, downStation);

        final Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        section.setLine(line);
        final Section savedSection = sectionRepository.save(section);
        return new SectionResponse(savedSection.getId(), savedSection.getDistance());
    }
}
