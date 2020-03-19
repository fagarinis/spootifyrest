package it.spootifyrest.web.dto.brano;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import it.spootifyrest.model.Brano;
import it.spootifyrest.web.dto.album.AlbumDTO;

public class BranoDTO{

	private Long id;
	@NotBlank
	private String titoloBrano;

	@JsonIgnoreProperties(value = { "brani", "artista" })
	@NotNull
	private AlbumDTO album;

	public static Brano buildBranoModelFromDTO(BranoDTO source, boolean includeAlbum) {
		Brano result = new Brano();
		result.setId(source.getId());
		result.setTitoloBrano(source.getTitoloBrano());
		if (includeAlbum) {
			result.setAlbum(AlbumDTO.buildAlbumModelFromDTO(source.getAlbum(), false, false));
		}

		return result;
	}

	public static BranoDTO buildBranoDTOFromModel(Brano source, boolean includeAlbum) {
		if(source == null) {
			return null;
		}
		
		BranoDTO result = new BranoDTO();
		result.setId(source.getId());
		result.setTitoloBrano(source.getTitoloBrano());
		if (includeAlbum) {
			result.setAlbum(AlbumDTO.buildAlbumDTOFromModel(source.getAlbum(), false, false));
		}

		return result;
	}

	public static List<BranoDTO> buildDTOListFromModelList(List<Brano> input, boolean includeAlbum) {
		List<BranoDTO> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (Brano branoItem : input) {
			BranoDTO branoDTOtemp = BranoDTO.buildBranoDTOFromModel(branoItem, includeAlbum);
			resultList.add(branoDTOtemp);
		}
		return resultList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitoloBrano() {
		return titoloBrano;
	}

	public void setTitoloBrano(String titoloBrano) {
		this.titoloBrano = titoloBrano;
	}

	public AlbumDTO getAlbum() {
		return album;
	}

	public void setAlbum(AlbumDTO album) {
		this.album = album;
	}

	public static List<Brano> buildModelListFromDTOList(List<BranoDTO> input, boolean includeAlbum) {
		List<Brano> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (BranoDTO branoDTOItem : input) {
			Brano branoTemp = BranoDTO.buildBranoModelFromDTO(branoDTOItem, includeAlbum);
			resultList.add(branoTemp);
		}
		return resultList;
	}

	@Override
	public String toString() {
		return "BranoDTO [id=" + id + ", titoloBrano=" + titoloBrano + ", album=" + album + "]";
	}

}
