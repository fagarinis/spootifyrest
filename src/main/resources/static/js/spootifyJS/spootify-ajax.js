function ajaxSetupTokenInHeaderFromForm() {
	// questo per settare le chiamate ajax successive
	$.ajaxSetup({
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json',
			"token": getTokenFromForm()
		}
	});
}
//chiamata parametrizzata
function doCall(typeRequest, urlPath, parametri, callbackOnSuccess) {
	$.ajax({
		url: urlPath,
		type: typeRequest,
		data: parametri,
		success: callbackOnSuccess
	});
}

function doCallWithTokenFromForm(typeRequest, urlPath, parametri, callbackOnSuccess) {
	ajaxSetupTokenInHeaderFromForm();
	doCall(typeRequest, urlPath, parametri, callbackOnSuccess);
}