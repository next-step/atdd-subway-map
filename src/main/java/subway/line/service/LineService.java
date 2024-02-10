package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundLineException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.presentation.request.AddSectionRequest;
import subway.line.presentation.request.CreateLineRequest;
import subway.line.presentation.request.UpdateLineRequest;
import subway.line.presentation.response.*;
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
        Section section = sectionRepository.save(Section.of(upStation, downStation, createLineRequest.getDistance()));

        Line line = lineRepository.save(
                Line.from(
                        createLineRequest.getName(), createLineRequest.getColor(), createLineRequest.getDistance()
                )
        );

        line.registerSection(section);

        return CreateLineResponse.from(line);
    }

    public ShowAllLinesResponse findAllLines() {
        return ShowAllLinesResponse.of(lineRepository.findAll().stream()
                .map(LineDto::from)
                .collect(Collectors.toList()));
    }

    public ShowLineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException());

        return ShowLineResponse.from(line);
    }

    @Transactional
    public UpdateLineResponse updateLine(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException());

        line.updateLine(updateLineRequest.getColor(), updateLineRequest.getDistance());

        return UpdateLineResponse.from(line);
    }

    @Transactional
    public void deleteLine(final Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public AddSectionResponse addSection(Long id, AddSectionRequest addSectionRequest) {
        Station upStation = stationRepository.getById(addSectionRequest.getUpStationId());
        Station downStation = stationRepository.getById(addSectionRequest.getDownStationId());
        Section section = sectionRepository.save(Section.of(upStation, downStation, addSectionRequest.getDistance()));

        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException());

        line.addSection(section);

        return AddSectionResponse.from(line);
    }

}
