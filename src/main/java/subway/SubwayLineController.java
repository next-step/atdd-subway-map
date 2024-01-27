package subway;

import java.net.URI;
import java.util.List;
import javax.websocket.server.PathParam;
import org.hibernate.hql.internal.ast.tree.ResolvableNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubwayLineController {
    private SubwayLineService subwayLineService;

    public SubwayLineController(SubwayLineService subwayLineService) {
        this.subwayLineService = subwayLineService;
    }

    @PostMapping("/subwayLines")
    public ResponseEntity<SubwayLineResponse> createSubwayLine(@RequestBody SubwayLineRequest subwayLineRequest) {
        SubwayLineResponse line = subwayLineService.saveLine(subwayLineRequest);

        return ResponseEntity.created(URI.create("/subwayLines/" + line.getId())).body(line);
    }

    @GetMapping("/subwayLines")
    public ResponseEntity<List<SubwayLineResponse>> getSubwayLineList() {
        return ResponseEntity.ok().body(subwayLineService.findAllLines());
    }

    @GetMapping("/subwayLines/{id}")
    public ResponseEntity<SubwayLineResponse> getSubwayLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(subwayLineService.findLine(id));
    }

    @PutMapping("/subwayLines/{id}")
    public ResponseEntity<SubwayLineResponse> putSubwayLine(@PathVariable Long id, @RequestBody SubwayLineRequest subwayLineRequest) {
        return ResponseEntity.ok().body(subwayLineService.updateLine(id, subwayLineRequest));
    }

    @DeleteMapping("/subwayLines/{id}")
    public ResponseEntity<Void> deleteSubwayLine(@PathVariable Long id) {
         subwayLineService.deleteLineById(id);
         return ResponseEntity.noContent().build();
    }
}
