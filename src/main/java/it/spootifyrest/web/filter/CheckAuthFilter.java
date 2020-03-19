package it.spootifyrest.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import it.spootifyrest.model.Utente;
import it.spootifyrest.service.SessioneService;
import it.spootifyrest.service.UtenteService;

//urlPattern non funziona, non filtra le request con solo quel path
@WebFilter(filterName = "CheckAuthFilter", urlPatterns = { "/*" })
@Component
@Order(1)
public class CheckAuthFilter implements Filter {

	private static final String HOME_PATH = "/";
	private static final String[] EXCLUDED_URLS = { "index.html", "/auth/login", "/auth/logout", "/register", "/css/",
			"/js/", "favicon.ico" };

	// l'admin ha la creazione, modifica e cancellazione di album, artisti, brani. +
	// gestione utenti
	private static final String[] PROTECTED_URLS_ADMIN = { "/admin/" };

//	Creare, rinominare e cancellare una playlist e gestire i brani al suo interno (aggiungi/rimuovi).
//	Avviare una riproduzione a partire da una playlist o un album, potendo andare avanti, indietro o
//	bloccare la riproduzione.
	private static final String[] PROTECTED_URLS_CUSTOMER = { "/player", "/playlists/", "/myPlaylists/" };

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private SessioneService sessioneService;

	/**
	 * @return il token dalla request con chiave "token"
	 */
	private String getTokenFromRequest() {
		return httpServletRequest.getHeader("token");
	}

	private Utente getUtenteInSessione() {
		return utenteService.caricaUtenteAttivoConSessioneValidaDaToken(getTokenFromRequest());
	}

	public CheckAuthFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// prendo il path della request che sta passando in questo momento
		String pathAttuale = httpServletRequest.getServletPath();
		//TODO eliminare print 
		System.out.println(pathAttuale);

		if (!isPathInWhiteList(pathAttuale)) {
			if(!sessioneService.tokenValido(getTokenFromRequest())) {
				httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token non valido");
				return;
			}
			Utente utenteInSessione = getUtenteInSessione();
			// Print di testing
//			System.out.println("filtro in azione su path " + pathAttuale);
//			System.out.println("token: " + getTokenFromRequest());
//			System.out.println("utente in sessione: " + utenteInSessione);

			if (utenteInSessione == null || utenteInSessione.getSessione() == null) {
				//questa è la sintassi corretta per mandare errori in console nel browser
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sessione utente scaduta");
				return;
			}
			
			if (utenteInSessione.getRuoli().size() == 0) {
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "L'utente non appartiene a nessuna categoria di ruolo");
				return;
			}

			if (!utenteInSessione.isAdmin() && isPathForOnlyAdmin(pathAttuale)) {
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utente non autorizzato all'area admin");
				return;
			}

			if (!utenteInSessione.isCustomer() && !utenteInSessione.isAdmin() && isPathForOnlyCustomer(pathAttuale)) {
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Utente non autorizzato all'area customer");
				return;
			}

			// refresha la sessione dell'utente per ogni chiamata che fa
			utenteInSessione.getSessione().refresh();
			sessioneService.aggiorna(utenteInSessione.getSessione());
		}

		chain.doFilter(request, response);
	}

	private boolean isPathInWhiteList(String requestPath) {
		// bisogna controllare che se il path risulta proprio "/" oppure se
		// siamo in presenza un url 'libero'
		if (requestPath.equals(HOME_PATH))
			return true;

		for (String urlPatternItem : EXCLUDED_URLS) {
			if (requestPath.contains(urlPatternItem)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPathForOnlyAdmin(String requestPath) {
		for (String urlPatternItem : PROTECTED_URLS_ADMIN) {
			if (requestPath.contains(urlPatternItem)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPathForOnlyCustomer(String requestPath) {
		for (String urlPatternItem : PROTECTED_URLS_CUSTOMER) {
			if (requestPath.contains(urlPatternItem)) {
				return true;
			}
		}
		return false;
	}

	public void destroy() {
	}

}
