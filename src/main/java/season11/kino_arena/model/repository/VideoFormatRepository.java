package season11.kino_arena.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import season11.kino_arena.model.pojo.VideoFormat;

@Repository
public interface VideoFormatRepository extends JpaRepository<VideoFormat, Long> {
}
