function showPlayer(show) {
	if (show) {
		$("#formId").hide();
		$("#songsTableId").show();
		$("#playerGroupButtonId").show();
	} else {
		$("#playerGroupButtonId").hide();
		$("#songsTableId").hide();
		$("#formId").show();
	}
}

function riproduci() {
	if (!xor(getAlbumIdFromForm(), getPlaylistIdFromForm())) {
		console.log("Errore Input: i form di album e playlist sono entrambi vuoti o entrambi valorizzati");
		return false;
	}

	doCallWithTokenFromForm('POST', buildPlayPath(), {}, function (result) {
		buildSongsTable(result);
	});

	return false;
}

function changeTrack(next) {
	doCallWithTokenFromForm('POST', buildChangeTrackPath(next), {}, function (result) {
		updateSelectedTrack(result);
	});
}

function indietro() {
	showPlayer(false);
}

function cancellaRiproduzione() {
	doCallWithTokenFromForm('DELETE', buildStopPlayPath(), {}, function () {
		showPlayer(false);
	});
}

function buildSongsTable(jsonRiproduzione) {
	deleteSongsTable();
	
	var songsTable = $("#songsTableId");
	
	var BranoInAscolto = jsonRiproduzione["brano"];
	var tipoRaccolta = jsonRiproduzione["playlist"] == null ? "album" : "playlist";
	var listaBrani = jsonRiproduzione[tipoRaccolta]["brani"];
	
	if(BranoInAscolto != null){
	var idBranoInAscolto = BranoInAscolto.id;
	}

	songsTable.append(buildTableHead(jsonRiproduzione));

	for (var i = 0; i < listaBrani.length; i++) {
		songsTable.append(buildTableRow(listaBrani[i]));
	}
	
	showPlayer(true);
	updateSelectedTrack(jsonRiproduzione);
}

function deleteSongsTable(){
	$("#songsTableId").empty();
}

function buildTableHead(jsonRiproduzione){
	var tipoRaccolta = jsonRiproduzione["playlist"] == null ? "album" : "playlist";
	
	var tableHead = "<th> ";
	
	if (tipoRaccolta == "album") {
		tableHead += jsonRiproduzione["album"].nomeAlbum + " (" + jsonRiproduzione["album"].annoDiUscita + ")";
	} else if (tipoRaccolta == "playlist") {
		tableHead += jsonRiproduzione["playlist"].titoloPlaylist + " (" + jsonRiproduzione["playlist"]["utente"].username + ")";
	}

	tableHead += " <th>";
	return tableHead;
}

function buildTableRow(branoJson){
	id = branoJson.id
	titolo = branoJson.titoloBrano
	album = branoJson["album"].nomeAlbum;
	artista = branoJson["album"]["artista"].nome + " " + branoJson["album"]["artista"].cognome;

	var tableRow = "<tr><td id='B"+id+"'>";
	tableRow += titolo + ", " + album + ", " + artista + "</td></tr>";
	tableRow += "</td></tr>"
		
	return tableRow;
}

function updateSelectedTrack(jsonRiproduzione){
	var branoInAscolto = jsonRiproduzione["brano"];
	if(branoInAscolto == null){
		return;
	}
	
	setSelected(branoInAscolto.id); // track is now selected
	
}

function isSelected(idBrano){
	return $("#B"+idBrano).data('selected') == 'selected';
}

function setSelected(idBrano){
	clearCurrentSelected();
	
	var tagBranoRow = document.getElementById("B"+idBrano);
	tagBranoRow.setAttribute('selected', 'selected');
	tagBranoRow.innerHTML = "<b>"+tagBranoRow.innerHTML+"</b>";
}

function clearCurrentSelected(){
	var idSelected = getSelectedTag().attr("id");
	var tagBranoSelectedRow =  document.getElementById(idSelected);
	if(tagBranoSelectedRow != null){
		tagBranoSelectedRow.setAttribute('selected',"''");
		tagBranoSelectedRow.innerHTML = tagBranoSelectedRow.innerText;
	}
}

function getSelectedTag(){
	return $("[selected ='selected']");
}
