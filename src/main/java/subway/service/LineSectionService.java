package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineSectionCreateResponse;
import subway.domain.Line;
import subway.domain.LineSection;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.LineSectionRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineSectionService {

    private final LineRepository lineRepository;

    private final SectionRepository sectionRepository;

    private final LineSectionRepository lineSectionRepository;

    private final StationRepository stationRepository;

    public LineSectionService(LineRepository lineRepository,
        SectionRepository sectionRepository,
        LineSectionRepository lineSectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.lineSectionRepository = lineSectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineSectionCreateResponse connectNewSectionIntoLine(Long lineId,
        Long upStationId,
        Long downStationId,
        Long distance) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new LineNotFoundException("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + lineId));

        Station downStation = stationRepository.findById(downStationId)
            .orElseThrow(() -> new StationNotFoundException(
                "입력된 ID에 해당하는 역이 존재하지 않습니다: " + downStationId));

        Station upStation = stationRepository.findById(upStationId)
            .orElseThrow(() -> new StationNotFoundException(
                "입력된 ID에 해당하는 역이 존재하지 않습니다: " + upStationId));

        return LineSectionCreateResponse.responseFrom(lineSectionRepository.save(new LineSection(line, new Section(downStation, upStation, distance))));
    }
}
