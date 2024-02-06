package subway.line.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.presentation.request.CreateLineRequest;
import subway.line.presentation.request.UpdateLineRequest;
import subway.line.presentation.response.CreateLineResponse;
import subway.line.presentation.response.ShowAllLinesResponse;
import subway.line.presentation.response.ShowLineResponse;
import subway.line.presentation.response.UpdateLineResponse;
import subway.section.domain.Section;
import subway.section.domain.SectionRepository;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public CreateLineResponse saveLine(CreateLineRequest createLineRequest) {
        Station upStation = stationRepository.getById(createLineRequest.getUpStationId());
        Station downStation = stationRepository.getById(createLineRequest.getDownStationId());
        Section section = sectionRepository.save(new Section(upStation, downStation, createLineRequest.getDistance()));

        Line line = lineRepository.save(
                Line.from(
                        createLineRequest.getName(), createLineRequest.getColor(), createLineRequest.getDistance()
                )
        );

        line.addSection(section);

        return CreateLineResponse.from(line);
    }

    public ShowAllLinesResponse findAllLines() {
        return ShowAllLinesResponse.of(lineRepository.findAll().stream()
                .map(LineDto::from)
                .collect(Collectors.toList()));
    }

    public ShowLineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 지하철 노선입니다."));

        return ShowLineResponse.from(line);
    }

    @Transactional
    public UpdateLineResponse updateLine(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 지하철 노선입니다."));

        line.updateLine(updateLineRequest.getColor(), updateLineRequest.getDistance());

        return UpdateLineResponse.from(line);
    }

    @Transactional
    public void deleteLine(final Long id) {
        lineRepository.deleteById(id);
    }

}
