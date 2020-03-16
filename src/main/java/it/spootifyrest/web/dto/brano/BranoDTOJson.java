package it.spootifyrest.web.dto.brano;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.spootifyrest.model.Brano;
import it.spootifyrest.web.dto.album.AlbumDTO;

public class BranoDTOJson {
	
	private Long id;
	private String titoloBrano;

	@JsonIgnoreProperties(value = { "brani"})
	private AlbumDTO album;

	public static Brano buildBranoModelFromDTO(BranoDTOJson source, boolean includeAlbum) {
		Brano result = new Brano();
		result.setId(source.getId());
		result.setTitoloBrano(source.getTitoloBrano());
		if (includeAlbum) {
			result.setAlbum(AlbumDTO.buildAlbumModelFromDTO(source.getAlbum(), false, false));
		}

		return result;
	}

	public static BranoDTOJson buildBranoDTOFromModel(Brano source, boolean includeAlbum) {
		if(source == null) {
			return null;
		}
		
		BranoDTOJson result = new BranoDTOJson();
		result.setId(source.getId());
		result.setTitoloBrano(source.getTitoloBrano());
		if (includeAlbum) {
			result.setAlbum(AlbumDTO.buildAlbumDTOFromModel(source.getAlbum(), false, true));
		}

		return result;
	}

	public static List<BranoDTOJson> buildDTOListFromModelList(List<Brano> input, boolean includeAlbum) {
		List<BranoDTOJson> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (Brano branoItem : input) {
			BranoDTOJson branoDTOtemp = BranoDTOJson.buildBranoDTOFromModel(branoItem, includeAlbum);
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

	public static List<Brano> buildModelListFromDTOList(List<BranoDTOJson> input, boolean includeAlbum) {
		List<Brano> resultList = new ArrayList<>();
		if (input == null) {
			return null;
		}
		for (BranoDTOJson branoDTOItem : input) {
			Brano branoTemp = BranoDTOJson.buildBranoModelFromDTO(branoDTOItem, includeAlbum);
			resultList.add(branoTemp);
		}
		return resultList;
	}

	@Override
	public String toString() {
		return "BranoDTO [id=" + id + ", titoloBrano=" + titoloBrano + ", album=" + album + "]";
	}
}
