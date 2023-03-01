package subway.sections;

import static javax.persistence.FetchType.LAZY;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.StringUtils;
import subway.line.LineException.DuplicatedStationAddToSectionFailException;
import subway.line.LineException.InvalidUpstationAppendInSection;
import subway.line.LineException.MiddleSectionRemoveFailException;
import subway.line.LineException.ZeroSectionException;
import subway.section.Section;
import subway.station.Station;

@Embeddable
public class Sections {

  @OneToMany(fetch = LAZY)
  private List<Section> sections;

  public Sections() {
    this.sections = new LinkedList<>();
  }

  public Station getUpStation() {
    return sections.get(0).getUpStation();
  }

  public Station getDownStation() {
    return sections.get(sections.size() - 1).getDownStation();
  }

  public List<Station> getStations() {
    List<Station> stations = new LinkedList<>();
    stations.add(getUpStation());
    stations.addAll(sections.stream().map(Section::getDownStation).collect(Collectors.toList()));
    return stations;
  }

  public void addSection(Section section) {
    if (sections.size() != 0 && !Objects.equals(getDownStation().getId(), section.getUpStation().getId())) {
      throw new InvalidUpstationAppendInSection();
    }
    if (sections.stream().anyMatch(s -> Objects.equals(section.getDownStation().getId(), s.getUpStation().getId()))) {
      throw new DuplicatedStationAddToSectionFailException();
    };

    sections.add(section);
  }

  public void removeSection(Station station) {
    if (sections.size() <= 1) {
      throw new ZeroSectionException();
    }
    System.out.println("no error occur!");
    if (!Objects.equals(getDownStation().getId(), station.getId())) {
      throw new MiddleSectionRemoveFailException();
    }

    sections.remove(sections.size() - 1);
  }
}
