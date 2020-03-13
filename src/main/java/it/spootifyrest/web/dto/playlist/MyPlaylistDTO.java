package it.spootifyrest.web.dto.playlist;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import it.spootifyrest.model.Playlist;

public class MyPlaylistDTO {

	private Long id;
	@NotBlank
	private String titoloPlaylist;

	public static Playlist buildPlaylistModelFromDTO(MyPlaylistDTO source) {
		Playlist result = new Playlist();
		result.setId(source.getId());
		result.setTitoloPlaylist(source.getTitoloPlaylist());

		return result;
	}

	public static MyPlaylistDTO buildPlaylistDTOFromModel(Playlist source) {
		MyPlaylistDTO result = new MyPlaylistDTO();
		result.setId(source.getId());
		result.setTitoloPlaylist(source.getTitoloPlaylist());

		return result;
	}

	public static List<MyPlaylistDTO> buildDTOListFromModelList(List<Playlist> input, boolean includeBrani,
			boolean includeUtente) {
		List<MyPlaylistDTO> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (Playlist playlistItem : input) {
			MyPlaylistDTO playlistDTOtemp = MyPlaylistDTO.buildPlaylistDTOFromModel(playlistItem);
			resultList.add(playlistDTOtemp);
		}
		return resultList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitoloPlaylist() {
		return titoloPlaylist;
	}

	public void setTitoloPlaylist(String titoloPlaylist) {
		this.titoloPlaylist = titoloPlaylist;
	}

}