package net.brewspberry.model;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.beans.SimpleHoublon;
import net.brewspberry.business.beans.SimpleLevure;
import net.brewspberry.business.beans.SimpleMalt;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.business.service.HopServiceImpl.SimpleHopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl.SimpleMaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl.SimpleYeastServiceImpl;
import net.brewspberry.exceptions.DAOException;
import net.brewspberry.util.LogManager;

public class SimpleMaltProcessor implements Processor<SimpleMalt> {

	private String ing_desc;
	private String ing_four;
	private String smal_cereale;
	private String smal_type;
	private String smal_couleur;
	

	IGenericService<SimpleMalt> simpleMaltService= (new MaltServiceImpl ()).new SimpleMaltServiceImpl(); 
	IGenericService<SimpleHoublon> simpleHopService= (new HopServiceImpl ()).new SimpleHopServiceImpl(); 
	IGenericService<SimpleLevure> simpleYeastService= (new YeastServiceImpl ()).new SimpleYeastServiceImpl(); 
	
	
	
	Logger logger = LogManager.getInstance(SimpleMaltProcessor.class.getName());
	
	@Override
	public Boolean record(SimpleMalt parentObject, HttpServletRequest request) {
		
		 if (request != null){
			 
			 if (request.getAttribute("ing_description") != null){
				 
				 ing_desc = (String) request.getAttribute("ing_description");
				 
				 parentObject.setIng_desc(ing_desc);
			 } else {
				 logger.warning("Did not add ing_desc");
			 }
			 

			 if (request.getAttribute("ing_fournisseur") != null){
				 
				 ing_four = (String) request.getAttribute("ing_fournisseur");
				 
				 parentObject.setIng_desc(ing_four);
			 } else {
				 logger.warning("Did not add ing_four");
			 }
			 
			 

			 if (request.getAttribute("smal_cereale") != null){
				 
				 smal_cereale = (String) request.getAttribute("smal_cereale");
				 
				 parentObject.setIng_desc(smal_cereale);
			 } else {
				 logger.warning("Did not add smal_cereale");
			 }
			 

			 if (request.getAttribute("smal_type") != null){
				 
				 smal_type = (String) request.getAttribute("smal_type");
				 
				 parentObject.setIng_desc(smal_type);
			 } else {
				 logger.warning("Did not add smal_type");
			 }
			 
			 if (request.getAttribute("smal_couleur") != null){
				 
				 smal_couleur = (String) request.getAttribute("smal_couleur");
				 
				 parentObject.setIng_desc(smal_couleur);
			 }
			 

			 
			 if (parentObject.getIng_id() == 0){
				 
				 try {
					simpleMaltService.save(parentObject);
					return true;

				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;

				}
				 				 
			 }
			 else if (parentObject.getIng_id() > 0){
				 
				 simpleMaltService.update(parentObject);
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
