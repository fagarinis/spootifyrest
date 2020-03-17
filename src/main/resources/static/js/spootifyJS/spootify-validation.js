function deleteAlbumField() {
	$("#inputAlbumId").val("");
}

function deletePlaylistField() {
	$("#inputPlaylistId").val("");
}

function getTokenFromForm() {
	return $("#inputTokenId").val();
}

function getAlbumIdFromForm() {
	return $("#inputAlbumId").val();
}

function getPlaylistIdFromForm() {
	return $("#inputPlaylistId").val();
}

function xor(A, B) {
	var a = !isBlank(A);
	var b = !isBlank(B);

	return (a || b) && !(a && b);
}

function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}

function getRaccolta() {
	albumId = getAlbumIdFromForm();
	playlistId = getPlaylistIdFromForm();

	if (!isBlank(albumId)) {
		return [albumId, true];
	} else if (!isBlank(playlistId)) {
		return [playlistId, false];
	}
}