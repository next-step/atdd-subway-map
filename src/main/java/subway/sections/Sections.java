package subway.sections;

import static javax.persistence.FetchType.LAZY;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
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
    if (sections.isEmpty()) {
      return null;
    }
    return sections.get(0).getUpStation();
  }

  public Station getDownStation() {
    if (sections.isEmpty()) {
      return null;
    }
    return sections.get(sections.size() - 1).getDownStation();
  }

  public Integer getSize() {
    return sections.size();
  }

  public List<Station> getStations() {
    List<Station> stations = new LinkedList<>();
    stations.add(getUpStation());
    stations.addAll(sections.stream().map(Section::getDownStation).collect(Collectors.toList()));
    return stations;
  }

  public void addSection(Section section) {
    SectionsValidator.validateAddedSection(this, section);
    sections.add(section);
  }

  public void removeSection(Station station) {
    SectionsValidator.validateRemoveSection(this, station);
    sections.remove(sections.size() - 1);
  }

  public boolean isStationContained(Station station) {
    if (sections.isEmpty()) {
      return false;
    }

    if (getDownStation().equals(station)) {
      return true;
    }
    return sections.stream().anyMatch(s -> s.getUpStation().equals(station));
  }
}
