package subway.api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.api.domain.model.entity.Link;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LinkRepository extends JpaRepository<Link,Long> {
}
