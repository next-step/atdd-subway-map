package subway.subway.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.subway.application.in.SubwayLineCloseUsecase;
import subway.subway.domain.SubwayLine;

@RequestMapping
public class SubwayLineCloseController {

    private final SubwayLineCloseUsecase subwayLineCloseUsecase;

    public SubwayLineCloseController(SubwayLineCloseUsecase subwayLineCloseUsecase) {
        this.subwayLineCloseUsecase = subwayLineCloseUsecase;
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> closeSubwayLine(@PathVariable Long id) {
        SubwayLineCloseUsecase.Command command = new SubwayLineCloseUsecase.Command(new SubwayLine.Id(id));
        subwayLineCloseUsecase.closeSubwayLine(command);
        return ResponseEntity.noContent().build();
    }

}
