package com.smalog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController extends BaseController {
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}