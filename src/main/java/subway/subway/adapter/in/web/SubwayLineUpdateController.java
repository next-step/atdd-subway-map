package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.subway.adapter.in.web.mapper.SubwayLineUpdateMapper;
import subway.subway.application.in.SubwayLineUpdateUsecase;

@RestController
public class SubwayLineUpdateController {

    private final SubwayLineUpdateUsecase subwayLineUpdateUsecase;
    private final SubwayLineUpdateMapper mapper;

    public SubwayLineUpdateController(SubwayLineUpdateUsecase subwayLineUpdateUsecase, SubwayLineUpdateMapper mapper) {
        this.subwayLineUpdateUsecase = subwayLineUpdateUsecase;
        this.mapper = mapper;
    }

    @PutMapping("/subway-lines/{id}")
    public ResponseEntity<Void> updateSubwayLine(@PathVariable Long id,  @RequestBody Request request) {
        SubwayLineUpdateUsecase.Command command = mapper.mapFrom(id, request);
        subwayLineUpdateUsecase.updateSubwayLine(command);
        return ResponseEntity.ok().build();
    }

    public static class Request {
        private String name;
        private String color;

        public Request(String name, String color) {
            this.name = name;
            this.color = color;
        }

        private Request() {
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }
    }

}
