package subway.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"line_id", "upStationId", "downStationId"}))
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	Line line;

	private Long upStationId;

	private Long downStationId;


	private int distance;

	public Long getId() {
		return id;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public int getDistance() {
		return distance;
	}

	public Section() {
	}

	public Section(Line line, Long upStationId, Long downStationId, int distance) {
		this.line = line;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Section) {
			return Objects.equals(((Section) obj).getId(), id);
		}

		return false;
	}
}
