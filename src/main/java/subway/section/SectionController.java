package subway.section;

import static org.springframework.http.ResponseEntity.status;

import java.net.URI;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subway.line.service.LineService;
import subway.section.domain.Section;
import subway.section.exception.InvalidSectionDownStationException;
import subway.section.exception.InvalidSectionUpStationException;
import subway.section.model.SectionCreateRequest;
import subway.section.model.SectionCreateResponse;
import subway.section.service.SectionCreateService;
import subway.support.ErrorResponse;
import subway.support.SubwayException;

@RequestMapping("/lines")
@RestController
@RequiredArgsConstructor
public class SectionController {
    private final SectionCreateService sectionCreateService;
    private final LineService lineService;

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<SectionCreateResponse> createSection(@PathVariable Long lineId, @RequestBody SectionCreateRequest request) {

        Section section = sectionCreateService.create(lineService.getLine(lineId), request);

        return ResponseEntity.created(URI.create("/lines/" + lineId + "/sections/" + section.getId()))
                             .body(new SectionCreateResponse(section));
    }

    @ExceptionHandler(value = { InvalidSectionUpStationException.class, InvalidSectionDownStationException.class, })
    public ResponseEntity<ErrorResponse> handleInvalidSectionUpstationException(SubwayException se) {
        return status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(se));
    }
}
