package subway.controller;

import javax.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.facade.StationLineSectionFacade;
import subway.service.request.SectionRequest;

@RestController
public class StationLineSectionController {

    private final StationLineSectionFacade facade;

    public StationLineSectionController(StationLineSectionFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/lines/{id}/sections")
    public ResponseEntity<Object> createStationLineSection(
        @PathVariable long id,
        @RequestBody SectionRequest request) {

        facade.addStationLineSection(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/lines/{id}/sections")
    public ResponseEntity<Object> deleteStationLineSection(
        @PathVariable(name = "id") long lineId,
        @PathParam("stationId") long sectionId) {

        facade.deleteSection(lineId, sectionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
