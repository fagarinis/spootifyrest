package it.spootifyrest.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class Filter {

	@Autowired
	HttpServletRequest httpServletRequest;

	public void filter() {

		// così prendo il token dalla request di postman //TODO
//		httpServletRequest.getHeader("token");
		//fare i controlli
	}

}
