package subway.application.line;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import subway.domain.line.service.LineService;
import subway.domain.station.Station;
import subway.domain.station.service.StationService;
import subway.presentation.line.dto.request.LineRequest;
import subway.presentation.line.dto.request.LineUpdateRequest;
import subway.presentation.line.dto.response.LineResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineFacade {

    private final LineService lineService;
    private final StationService stationService;

    @Transactional
    public LineResponse createLine(LineRequest request) {
        Station upStation = stationService.getStation(request.getUpStationId());
        Station downStation = stationService.getStation(request.getDownStationId());
        return lineService.createLine(request, upStation, downStation);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getAllLines() {
        return lineService.getAllLines();
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        return lineService.getLine(lineId);
    }

    @Transactional
    public void updateLine(Long lineId, LineUpdateRequest request) {
        lineService.updateLine(lineId,request);
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineService.deleteLine(lineId);
    }

}
