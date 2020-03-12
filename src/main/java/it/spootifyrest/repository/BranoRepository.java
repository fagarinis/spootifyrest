package it.spootifyrest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import it.spootifyrest.model.Brano;

public interface BranoRepository extends IBaseRepository<Brano>{

	@Query("Select distinct b from Brano b left join fetch b.album a where b.id = ?1")
	Optional<Brano> findByIdEager(Long id);

	@Query("SELECT DISTINCT b FROM Brano b LEFT JOIN FETCH b.playlist p WHERE p.id = ?1")
	List<Brano> findAllByPlaylistId(Long idPlaylist);

}
