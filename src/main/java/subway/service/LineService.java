package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.color.Color;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.sectionstation.Direction;
import subway.domain.sectionstation.SectionStation;
import subway.domain.station.Station;
import subway.dto.domain.AddSectionVo;
import subway.dto.domain.CreateSectionVo;
import subway.dto.domain.DeleteSectionVo;
import subway.dto.line.*;
import subway.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final SectionStationRepository sectionStationRepository;
    private final ColorRepository colorRepository;

    @Transactional
    public CreateLineResponse createLine(CreateLineRequest request) {
        Color color = colorRepository.findByName(request.getColor()).orElseThrow();
        Line line = lineRepository.save(new Line(request.getName(), color));

        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Section section = new Section(request.getDistance(), line);
        line.createSection(new CreateSectionVo(section, new SectionStation(section, upStation, Direction.UP), new SectionStation(section, downStation, Direction.DOWN)));

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
        Color color = colorRepository.findByName(request.getColor()).orElseThrow();
        line.updateNameAndColor(request.getName(), color);
    }

    @Transactional
    public void deleteLine(Long stationLineId) {
        Line line = lineRepository.findById(stationLineId).orElseThrow();
        line.deleteAllSection();
        lineRepository.delete(line);
    }

    @Transactional
    public void extendLine(ExtendLineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Line line = lineRepository.findById(request.getLineId()).orElseThrow();

        line.checkLineExtendValid(upStation, downStation);

        Section section = new Section(request.getDistance(), line);
        SectionStation upSectionStation = new SectionStation(section, upStation, Direction.UP);
        SectionStation downSectionStation = new SectionStation(section, downStation, Direction.DOWN);
        line.addSection(new AddSectionVo(section, upSectionStation, downSectionStation));
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

        line.deleteSection(new DeleteSectionVo(lastSection, upSectionStation, downSectionStation));
        stationRepository.delete(lastStation);
    }

}
