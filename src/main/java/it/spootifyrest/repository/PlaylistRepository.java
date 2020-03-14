package it.spootifyrest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import it.spootifyrest.model.Playlist;

public interface PlaylistRepository extends IBaseRepository<Playlist> {

	@Query("SELECT DISTINCT p from Playlist p left join fetch p.utente u WHERE u.id = ?1 ")
	List<Playlist> findAllPlaylistsByUtenteId(Long id);

}
