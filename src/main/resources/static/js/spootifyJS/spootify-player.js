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
		riproduci();
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

function buildSongsTable(json) {
	var table = $("#songsTableId");
	table.empty();

	var tipoRaccolta = json["playlist"] == null ? "album" : "playlist";
	if(json["brano"] != null){
	var idBranoInAscolto = json["brano"].id;
	}

	var listaBrani = json[tipoRaccolta]["brani"];

	var tableHead = "<th> ";
	if (tipoRaccolta == "album") {
		tableHead += json["album"].nomeAlbum + " (" + json["album"].annoDiUscita + ")";
	} else if (tipoRaccolta == "playlist") {
		tableHead += json["playlist"].titoloPlaylist + " (" + json["playlist"]["utente"].username + ")";
	}

	tableHead += " <th>";
	table.append(tableHead)

	for (var i = 0; i < listaBrani.length; i++) {
		id = listaBrani[i].id
		titolo = listaBrani[i].titoloBrano
		album = listaBrani[i]["album"].nomeAlbum;
		artista = listaBrani[i]["album"]["artista"].nome + " " + listaBrani[i]["album"]["artista"].cognome;

		var tableRow = "<tr><td>";
		if (id == idBranoInAscolto) {
			tableRow += " <b>&#9658;";
		}

		tableRow += titolo + ", " + album + ", " + artista + "</td></tr>";

		if (id == idBranoInAscolto) {
			tableRow += " </b>";
		}
		tableRow += "</td></tr>"
		table.append(tableRow);
	}

	showPlayer(true);
}