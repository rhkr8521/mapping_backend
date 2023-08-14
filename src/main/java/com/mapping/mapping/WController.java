package com.mapping.mapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class WController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

}