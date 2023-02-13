package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.util.Finder;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.SectionRegisterRequest;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@Transactional(readOnly = true)
@Service
public class SectionService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final Finder finder;

    public SectionService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final Finder finder
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.finder = finder;
    }

    @Transactional
    public void registerSection(final Long lineId, final SectionRegisterRequest sectionRegisterRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);

        Section section = createSection(sectionRegisterRequest, line);

        line.addSection(section);
    }

    private Section createSection(final SectionRegisterRequest sectionRegisterRequest, final Line line) {
        List<Station> stations = stationRepository.findAllById(
                List.of(sectionRegisterRequest.getUpStationId(), sectionRegisterRequest.getDownStationId())
        );
        Station upStation = finder.findStationById(stations, sectionRegisterRequest.getUpStationId());
        Station downStation = finder.findStationById(stations, sectionRegisterRequest.getDownStationId());
        return new Section(sectionRegisterRequest.getDistance(), upStation, downStation, line);
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
        line.deleteBy(station);
    }
}
