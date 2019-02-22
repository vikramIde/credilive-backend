package com.hackathon.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.data.CreditResponse;

@RestController
@RequestMapping("/hackathon/creditcard")
public class CreditCardInfoCtrl {
	
	String Content;
	@Autowired
	ServletContext servletContext;

	/**************************************************************************************
	 * Under this rest end point comes all the controller for credit card information	  *
	 * 																					  *	
	 *************************************************************************************/
	
	@RequestMapping(value = "/defaulter", method = RequestMethod.GET, headers = "Accept=application/json")
	public CreditResponse executeEid()
	{
		
		CreditResponse response = new CreditResponse();
		return response;
		

	}
	

}
