package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.sectionstation.Direction;
import subway.domain.sectionstation.SectionStation;
import subway.domain.station.Station;
import subway.dto.line.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.SectionStationRepository;
import subway.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final SectionStationRepository sectionStationRepository;

    @Transactional
    public CreateLineResponse createLine(CreateLineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        Section section = sectionRepository.save(new Section(request.getDistance(), line));

        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        sectionStationRepository.save(new SectionStation(section, upStation, Direction.UP));
        sectionStationRepository.save(new SectionStation(section, downStation, Direction.DOWN));

        return new CreateLineResponse(line, List.of(upStation, downStation));
    }

    @Transactional(readOnly = true)
    public List<ReadLinesResponse> readLines() {
        List<ReadLinesResponse> response = new ArrayList<>();

        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            response.add(new ReadLinesResponse(line));
        }
        return response;
    }

    @Transactional(readOnly = true)
    public ReadLineResponse readLine(Long stationLineId) {
        Line line = lineRepository.findById(stationLineId).orElseThrow();
        return new ReadLineResponse(line);
    }

    @Transactional
    public void updateLine(Long stationLineId, UpdateLineRequest request) {
        Line line = lineRepository.findById(stationLineId).orElseThrow();
        line.updateNameAndColor(request.convertToEntity());
    }

    @Transactional
    public void deleteLine(Long stationLineId) {
        Line line = lineRepository.findById(stationLineId).orElseThrow();
        List<Section> sections = line.getSections().getSections();
        List<SectionStation> sectionStations = new ArrayList<>();
        for (Section section : sections) {
            sectionStations.addAll(section.getSectionStations().getSectionStations());
        }

        sectionStationRepository.deleteAll(sectionStations);
        sectionRepository.deleteAll(sections);
        lineRepository.delete(line);
    }

    @Transactional
    public void extendLine(ExtendLineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Line line = lineRepository.findById(request.getLineId()).orElseThrow();

        line.checkLineExtendValid(upStation, downStation);

        Section section = sectionRepository.save(new Section(request.getDistance(), line));
        sectionStationRepository.save(new SectionStation(section, upStation, Direction.UP));
        sectionStationRepository.save(new SectionStation(section, downStation, Direction.DOWN));
    }

    @Transactional
    public void reduceLine(ReduceLineRequest request) {
        Line line = lineRepository.findById(request.getLineId()).orElseThrow();
        Station lastStation = stationRepository.findById(request.getStationId()).orElseThrow();

        line.checkLineReduceValid(lastStation);

        List<Station> stations = line.getStationsByAscendingOrder();
        Station pairStation = stations.get(stations.size() - 2);

        SectionStation upSectionStation = sectionStationRepository.findByStationAndDirection(pairStation, Direction.UP).orElseThrow();
        SectionStation downSectionStation = sectionStationRepository.findByStationAndDirection(lastStation, Direction.DOWN).orElseThrow();
        Section lastSection = upSectionStation.getSection();

        sectionStationRepository.delete(upSectionStation);
        sectionStationRepository.delete(downSectionStation);
        stationRepository.delete(lastStation);
        sectionRepository.delete(lastSection);
    }

}
