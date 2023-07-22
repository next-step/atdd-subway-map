package subway.controller;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.ErrorResponse;
import subway.controller.dto.LineSectionCreateRequest;
import subway.controller.dto.LineSectionCreateResponse;
import subway.exception.LineNotConnectableException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.service.LineSectionService;

@RestController
public class LineSectionController {

    private final LineSectionService lineSectionService;

    public LineSectionController(LineSectionService lineSectionService) {
        this.lineSectionService = lineSectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<LineSectionCreateResponse> connectNewSection(@PathVariable Long lineId,
        @RequestBody LineSectionCreateRequest request) {
        LineSectionCreateResponse response = lineSectionService
            .connectNewSectionIntoLine(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance());

        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections")).body(response);
    }

    @ExceptionHandler(value = {
        LineNotFoundException.class,
        StationNotFoundException.class,
        LineNotFoundException.class,
        LineNotConnectableException.class})
    public ResponseEntity<ErrorResponse> error(RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
