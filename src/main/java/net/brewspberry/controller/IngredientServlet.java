package net.brewspberry.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.beans.AbstractIngredient;
import net.brewspberry.business.beans.Houblon;
import net.brewspberry.business.beans.Levure;
import net.brewspberry.business.beans.Malt;
import net.brewspberry.business.beans.SimpleHoublon;
import net.brewspberry.business.beans.SimpleLevure;
import net.brewspberry.business.beans.SimpleMalt;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.model.Processor;
import net.brewspberry.model.SimpleHopProcessor;
import net.brewspberry.model.SimpleMaltProcessor;
import net.brewspberry.model.SimpleYeastProcessor;
import net.brewspberry.util.Constants;
import net.brewspberry.util.LogManager;

@WebServlet("/ingredientServlet")
public class IngredientServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IGenericService<SimpleMalt> simpleMaltService= (new MaltServiceImpl ()).new SimpleMaltServiceImpl(); 
	IGenericService<SimpleHoublon> simpleHopService= (new HopServiceImpl ()).new SimpleHopServiceImpl(); 
	IGenericService<SimpleLevure> simpleYeastService= (new YeastServiceImpl ()).new SimpleYeastServiceImpl(); 
	
	
	private static final Logger logger = LogManager.getInstance(IngredientServlet.class.getName());
	
	
	
	public IngredientServlet() {
		super();
		// TODO Auto-generated constructor stub
	}


	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		
		long requestID = 0;
		
		String ingredientType = "";
		
		if (request.getParameter("iid") != null && request.getParameter("type") != null){
			/*
			 * if request is not null, it's an update ! 
			 * Must specify type of ingredient
			 * 
			 */
			try {
				requestID = Long.parseLong(request.getParameter("iid"));
				
				
				
				switch ((String) request.getParameter("type")){
				
				case "malt" :
					
					
					ingredientType = Constants.INGREDIENT_TYPES.get("malt");
					SimpleMalt malt = simpleMaltService.getElementById(requestID);
					
					
					
					break;
					
				case "hop" :

					ingredientType = Constants.INGREDIENT_TYPES.get("hop");
					SimpleHoublon hop = simpleHopService.getElementById(requestID);
					
					break;
					
				case "yeast" :

					ingredientType = Constants.INGREDIENT_TYPES.get("yeast");
					SimpleLevure levure = simpleYeastService.getElementById(requestID);
					
					break;
				
					
				}
				
			} catch (Exception e){
				
				e.printStackTrace();
			}
			
			
		}
		
		request.setAttribute("cerealType", Constants.CEREALS);
		request.setAttribute("ingredientType", ingredientType);
		
		// Sets ingredients menu list
		request.setAttribute("ingredientTypes", Constants.INGREDIENT_TYPES);
		
		request.getRequestDispatcher("add_update_ingredient.jsp").forward(request, response);
	}


	/**
	 * doPost is called for processing form data for creating or updating ingredient 
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		
		/*
		 * Processes ingredient creation or update form 
		 */
		
		AbstractIngredient ingredient = null;
		String ingredient_id = null;
		if (request.getAttribute("ingredient_type") != null) {

			String ing_type = (String) request.getAttribute("ingredient_type");
			long ing_id = 0;
			Processor proc;
			
			SimpleMalt malt = new SimpleMalt();
			SimpleLevure levure = new SimpleLevure();
			SimpleHoublon houblon = new SimpleHoublon();

			switch (ing_type) {

			case "malt":
				proc = new SimpleMaltProcessor();
				
				// if returns true, it's an update. Ingredient already exists
				if (isUpdate(request.getAttribute("ingredient_id"))){
					
					ingredient_id = (String) request.getAttribute("ingredient_id");
					try {
					
						ing_id = Long.parseLong(ingredient_id);
						malt = (SimpleMalt) simpleMaltService.getElementById(ing_id);
						
					} catch (Exception e){
						
						logger.severe("Could not convert "+ingredient_id+" to long.");
					
					}
				}

				proc.record((SimpleMalt) malt, request);
				
				break;

			case "hop":
				proc = new SimpleHopProcessor();
				
				// if returns true, it's an update. Ingredient already exists
				if (isUpdate(request.getAttribute("ingredient_id"))){
					
					ingredient_id = (String) request.getAttribute("ingredient_id");
					try {
					
						ing_id = Long.parseLong(ingredient_id);
						houblon = (SimpleHoublon) simpleHopService.getElementById(ing_id);
						
					} catch (Exception e){
						
						logger.severe("Could not convert "+ingredient_id+" to long.");
					
					}
				}

				proc.record((SimpleHoublon) ingredient, request);

				break;

			case "yeast":
				proc = new SimpleYeastProcessor();
				
				// if returns true, it's an update. Ingredient already exists
				if (isUpdate(request.getAttribute("ingredient_id"))){
					
					ingredient_id = (String) request.getAttribute("ingredient_id");
					try {
					
						ing_id = Long.parseLong(ingredient_id);
						levure = (SimpleLevure) simpleYeastService.getElementById(ing_id);
						
					} catch (Exception e){
						
						logger.severe("Could not convert "+ingredient_id+" to long.");
					
					}
				}

				proc.record((SimpleLevure) ingredient, request);

				break;
			}

		}

	}
	
	
	/**
	 * If attribute is a positive number, returns true, else false
	 */
	private Boolean isUpdate (Object attribute){
		
		
		if (attribute != null) {

			try {
				
				if (Long.parseLong((String) attribute) > 0)
					return true;
				else 
					return false;
				
			} catch (Exception e){
				return false;
			}
		}
		else {
			return false;
		}
	}
}
