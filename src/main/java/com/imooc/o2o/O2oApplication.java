package com.imooc.o2o;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class O2oApplication {
	@RequestMapping(value={"/", "/index","/index.html","/index.jsp"})
	public String getIndex(){
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(O2oApplication.class, args);
	}

}
