package it.spootifyrest.web.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.spootifyrest.service.BranoService;

@RestController
@RequestMapping(value = "/songs")
public class BranoController {

	@Autowired
	BranoService branoService;
}
