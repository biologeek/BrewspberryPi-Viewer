package net.brewspberry.model;

import javax.servlet.http.HttpServletRequest;

public interface Processor <T> {

	
	public Boolean record (T parentObject, HttpServletRequest request);
}
