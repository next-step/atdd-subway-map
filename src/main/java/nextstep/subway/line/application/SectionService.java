package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.SectionRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.exception.LineNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public void saveSection(long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new StationNotFoundException(sectionRequest.getUpStationId()));

        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new StationNotFoundException(sectionRequest.getDownStationId()));

        line.appendSection(new Section(upStation, downStation, sectionRequest.getDistance()));
    }

    @Transactional
    public void deleteSection(long lineId, long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));

        line.removeStation(station);
    }
}
