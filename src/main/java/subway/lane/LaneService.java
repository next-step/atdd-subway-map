package subway.lane;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LaneService {

  private LaneRepository laneRepository;

  public LaneService(LaneRepository repository) {
    this.laneRepository = repository;
  }

  @Transactional
  public LaneResponse saveLane(LaneRequest request) {
    Lane lane = laneRepository.save(new Lane(request.getName(), request.getInbound(), request.getOutbound()));
    return createServiceResponse(lane);
  }


  private LaneResponse createServiceResponse(Lane lane) {
    return new LaneResponse(lane.getId(), lane.getName());
  }
}
