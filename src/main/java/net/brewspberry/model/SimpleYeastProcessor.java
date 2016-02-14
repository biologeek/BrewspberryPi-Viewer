package net.brewspberry.model;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.beans.SimpleLevure;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.exceptions.DAOException;
import net.brewspberry.model.Processor;
import net.brewspberry.util.LogManager;

public class SimpleYeastProcessor implements Processor<SimpleLevure> {

	private Logger logger = LogManager.getInstance(SimpleHopProcessor.class.getName());
	IGenericService<SimpleLevure> simpleYeastService= (new YeastServiceImpl()).new SimpleYeastServiceImpl(); 

	

	private String ing_desc;
	private String ing_four;
	private String slev_espece;
    private String slev_floculation;
    private String slev_aromes;
    
    
	@Override
	public Boolean record(SimpleLevure parentObject, HttpServletRequest request) {

		 if (request != null){
			 
			 if (request.getParameter("ing_description") != null){
				 
				 ing_desc = (String) request.getParameter("ing_description");
				 
				 parentObject.setIng_desc(ing_desc);
			 } else {
				 logger.warning("Did not add ing_desc");
			 }
			 

			 if (request.getParameter("ing_fournisseur") != null){
				 
				 ing_four = (String) request.getParameter("ing_fournisseur");
				 
				 parentObject.setIng_fournisseur(ing_four);
			 } else {
				 logger.warning("Did not add ing_four");
			 }
			 
			 

			 if (request.getParameter("slev_espece") != null){
				 
				 slev_espece = (String) request.getParameter("slev_espece");
				 
				 parentObject.setSlev_espece(slev_espece);
			 } else {
				 logger.warning("Did not add slev_espece");
			 }
			 

			 if (request.getParameter("slev_floculation") != null){
				 
				 slev_floculation = (String) request.getParameter("slev_floculation");
				 
				 parentObject.setSlev_floculation(slev_floculation);
			 } else {
				 logger.warning("Did not add slev_floculation");
			 }

			 if (request.getParameter("slev_aromes") != null){
				 
				 slev_aromes = (String) request.getParameter("slev_aromes");
				 
				 parentObject.setSlev_aromes(slev_aromes);
			 } else {
				 logger.warning("Did not add slev_aromes");
			 }

			 
			 if (parentObject.getIng_id() == 0){
				 
				 // Saving yeast
				 try {
					simpleYeastService.save(parentObject);
					return true;

				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;

				}
				 				 
			 }
			 else if (parentObject.getIng_id() > 0){
				 
				 // Updating yeast
				 
				 simpleYeastService.update(parentObject);
				return true;

			 }
			 else {
				 try {
					throw new Exception("Ingredient ID is not correct : "+parentObject.getIng_id());
				} catch (Exception e) {

					logger.severe("Ingredient ID is not correct : "+parentObject.getIng_id());
					
				}
					return false;

			 }
			 
		 }
		
		return false;
	}

}
