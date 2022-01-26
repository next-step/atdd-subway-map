package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.line.domain.dto.LineRequest;
import nextstep.subway.line.domain.dto.LineResponse;
import nextstep.subway.line.domain.dto.LineWithStationResponse;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Line;
import nextstep.subway.line.domain.model.Section;
import nextstep.subway.line.domain.repository.LineRepository;
import nextstep.subway.line.domain.repository.SectionRepository;
import nextstep.subway.station.domain.model.Station;
import nextstep.subway.station.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository,
        StationRepository stationRepository,
        SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineWithStationResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicateColumnException(ColumnName.LINE_NAME);
        }
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Line line = Line.builder()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .upStation(upStation)
            .downStation(downStation)
            .build();

        lineRepository.save(line);
        return LineWithStationResponse.from(line);
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id)
                                .orElseThrow(EntityNotFoundException::new);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::from)
                             .collect(Collectors.toList());
    }

    public LineWithStationResponse findById(long id) {
        return lineRepository.findByIdWithStations(id)
                             .map(LineWithStationResponse::from)
                             .orElseThrow(EntityNotFoundException::new);
    }

    public void edit(long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        line.edit(lineRequest.getName(), lineRequest.getColor());
    }

    public void delete(long id) {
        lineRepository.deleteById(id);
    }

    public Long addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findByIdWithStations(lineId)
                                  .orElseThrow(EntityNotFoundException::new);
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Section createdSection = line.addSection(upStation, downStation, request.getDistance());
        sectionRepository.save(createdSection);
        return createdSection.getId();
    }

    public void deleteSection(Long lineId, Long sectionId) {
        lineRepository.findByIdWithStations(lineId)
                      .ifPresent(line -> line.deleteSection(sectionId));
    }
}
