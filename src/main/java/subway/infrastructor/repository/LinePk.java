package subway.infrastructor.repository;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LinePk implements Serializable {

    private Long id;

    public LinePk() {

    }

    public LinePk(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinePk linePk = (LinePk) o;
        return Objects.equals(id, linePk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}