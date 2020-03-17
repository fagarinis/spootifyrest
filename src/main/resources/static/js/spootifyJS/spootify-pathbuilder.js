function buildPlayPath() {
	var [raccoltaId, isAlbum] = getRaccolta();
	var tipoRaccolta = isAlbum ? "album" : "playlist";

	return "/player/" + tipoRaccolta + "/" + raccoltaId;
}

function buildChangeTrackPath(next) {
	var [raccoltaId, isAlbum] = getRaccolta();
	var path = "";
	if (isAlbum) {
		path += "albums/";
	} else {
		path += "playlists/";
	}
	path += raccoltaId;
	if (next) {
		path += "/play";
	} else {
		path += "/playPrevious";
	}

	return path;
}

function buildStopPlayPath() {
	var [raccoltaId, isAlbum] = getRaccolta();
	var path = "";
	if (isAlbum) {
		path += "albums/";
	} else {
		path += "playlists/";
	}
	path += raccoltaId + "/stop";

	return path;
}