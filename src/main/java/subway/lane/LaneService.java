package subway.lane;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

  public LaneResponse showLane(Long id) {
    Optional<Lane> optionalLane = laneRepository.findById(id);

    return optionalLane.map(this::createServiceResponse).orElse(null);
  }

  public List<LaneResponse> showLanes() {
    return laneRepository.findAll().stream()
        .map(this::createServiceResponse)
        .collect(Collectors.toList());
  }

  public void deleteLaneById(Long id) {
    laneRepository.deleteById(id);
  }

  public LaneResponse updateLane(Long id, LaneRequest request) {
    Optional<Lane> optionalLane = laneRepository.findById(id);

    if (optionalLane.isPresent()) {
      Lane lane = optionalLane.get();
      lane.setName(request.getName());
      lane.setInboundStation(request.getInbound());
      lane.setOutboundStation(request.getOutbound());
      return createServiceResponse(laneRepository.save(lane));
    }
    return null;
  }

  private LaneResponse createServiceResponse(Lane lane) {
    return new LaneResponse(lane.getId(), lane.getName());
  }
}
