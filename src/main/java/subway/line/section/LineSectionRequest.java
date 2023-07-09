package subway.line.section;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import subway.line.LineRequest;
import subway.line.SubwayLine;
import subway.station.Station;

@NoArgsConstructor
@Getter
public class LineSectionRequest {

  @NotBlank
  @NumberFormat
  private String downStationId;

  @NotBlank
  @NumberFormat
  private String upStationId;

  @NotNull
  private Long distance;

  public LineSectionRequest(LineRequest lineRequest) {
    this.distance = lineRequest.getDistance();
    this.upStationId = String.valueOf(lineRequest.getUpStationId());
    this.downStationId = String.valueOf(lineRequest.getDownStationId());
  }

  public LineSection toEntity(SubwayLine line, Station upStation, Station downStation) {
    return new LineSection(line, upStation, downStation);
  }

  public Long getDownStationId() {
    return Long.parseLong(downStationId);
  }

  public Long getUpStationId() {
    return Long.parseLong(upStationId);
  }
}
