package net.brewspberry.model;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.beans.Houblon;
import net.brewspberry.business.beans.SimpleHoublon;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.HopServiceImpl.SimpleHopServiceImpl;
import net.brewspberry.exceptions.DAOException;
import net.brewspberry.util.LogManager;

public class SimpleHopProcessor implements Processor<SimpleHoublon> {

	private Logger logger = LogManager.getInstance(SimpleHopProcessor.class.getName());
	IGenericService<SimpleHoublon> simpleHopService= (new HopServiceImpl ()).new SimpleHopServiceImpl(); 

	
	
	private String ing_desc;
	private String ing_four;
	private String shbl_variete;
	private double shbl_acide_alpha;
	private String shbl_aromes;
	private Integer shbl_type;
	

	@Override
	public Boolean record(SimpleHoublon parentObject, HttpServletRequest request) {

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
			 
			 

			 if (request.getParameter("shbl_variete") != null){
				 
				 shbl_variete = (String) request.getParameter("shbl_variete");
				 
				 parentObject.setShbl_variete(shbl_variete);
			 } else {
				 logger.warning("Did not add shbl_variete");
			 }
			 

			 if (request.getParameter("shbl_acide_alpha") != null){
				 
				 shbl_acide_alpha = Double.parseDouble((String) request.getParameter("shbl_acide_alpha"));
				 
				 parentObject.setShbl_acide_alpha(shbl_acide_alpha);
			 } else {
				 logger.warning("Did not add shbl_acide_alpha");
			 }

			 if (request.getParameter("shbl_aromes") != null){
				 
				 shbl_aromes = (String) request.getParameter("shbl_aromes");
				 
				 parentObject.setShbl_aromes(shbl_aromes);
			 } else {
				 logger.warning("Did not add shbl_aromes");
			 }

			 if (request.getParameter("shbl_type") != null){
				 
				 shbl_type = Integer.parseInt((String) request.getParameter("shbl_type"));
				 
				 parentObject.setShbl_type(shbl_type);
			 } else {
				 logger.warning("Did not add shbl_type");
			 }
			 

			 
			 if (parentObject.getIng_id() == 0){
				 
				 try {
					simpleHopService.save(parentObject);
					return true;

				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;

				}
				 				 
			 }
			 else if (parentObject.getIng_id() > 0){
				 
				 simpleHopService.update(parentObject);
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
