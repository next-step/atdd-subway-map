package subway.infrastructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

interface LineRepository extends JpaRepository<LineJpaEntity, Long> {
}
