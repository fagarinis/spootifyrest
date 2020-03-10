package it.spootifyrest.service;

import it.spootifyrest.model.Brano;

public interface BranoService extends IBaseService<Brano> {
	
	public Brano findLastBranoByUtenteIdAndPlaylistId(Long utenteId, Long playlistId);
	
	public Brano findLastBranoByUtenteIdAndAlbumId(Long utenteId, Long albumId);
}
