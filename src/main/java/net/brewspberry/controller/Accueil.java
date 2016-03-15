package net.brewspberry.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.brewspberry.business.IGenericDao;
import net.brewspberry.business.IGenericService;
import net.brewspberry.business.beans.Actioner;
import net.brewspberry.business.beans.Brassin;
import net.brewspberry.business.beans.Etape;
import net.brewspberry.business.beans.TemperatureMeasurement;
import net.brewspberry.business.service.BrassinServiceImpl;
import net.brewspberry.dao.TemperatureMeasurementDaoImpl;
import net.brewspberry.util.ConfigLoader;
import net.brewspberry.util.Constants;
import net.brewspberry.util.DeviceParser;

/**
 * Servlet implementation class Accueil
 */
@WebServlet("/Accueil")
public class Accueil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	IGenericService<Brassin> brassinService = (IGenericService<Brassin>) new BrassinServiceImpl();
	
	static Logger logger = Logger.getLogger(Accueil.class.toString());

    /**
     * Default constructor. 
     */
    public Accueil() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bids = request.getParameter("bid");
		Integer bid = null;
		
		if (bids != null) {

			try{
				bid = Integer.parseInt(bids);
			}
			catch (Exception e){
				bid = 0;
			}
		}
		
		
		// Displays welcome page
		if (bid == null || bid == 0) {
			List<Brassin> brewList = brassinService.getAllElements();
			
			request.setAttribute("brewList", brewList);
			request.getRequestDispatcher("accueil_viewer.jsp").forward(request, response);
		}
		
		//Displays brew page
		else if (bid > 0){
			
			logger.info("Retrieving brew from DB for id : "+bid);
			Brassin currentBrew = brassinService.getElementById(bid);
			
			if (currentBrew != null){
				logger.info("Found 1 brew : "+currentBrew);
			}
			request.setAttribute("brassin", currentBrew);
			
			logger.info("Steps for brew : "+currentBrew.getBra_etapes());
			
			List<Actioner> availableActioners = new ArrayList<Actioner>();
			
			availableActioners = DeviceParser.getInstance().getDevices(Constants.DEVICES_PROPERTIES);
			for (Actioner act : availableActioners){
				
				logger.fine("Device : "+act.getAct_nom()+", pin="+act.getAct_raspi_pin()+", uuid="+act.getAct_uuid());
				
			}
			
			for (Etape step : currentBrew.getBra_etapes()){
				
				logger.fine("||" + step.getEtp_nom()+ " "+ step.getEtp_id()+ " "+ step.getEtp_numero());
				
			}
			
			request.setAttribute("stepCounter", Math.ceil((double) currentBrew.getBra_etapes().size()) - 1);
			request.setAttribute("steps", currentBrew.getBra_etapes());
			request.setAttribute("availableActioners", availableActioners);
			request.setAttribute("tempServlet", ConfigLoader.getConfigByKey(Constants.CONFIG_PROPERTIES, "servlets.temperature.graph"));

			request.setAttribute("actionerServiceAddress", ConfigLoader.getConfigByKey(Constants.CONFIG_PROPERTIES, "servlets.actioners.activation"));
			
			
			request.getRequestDispatcher("brew.jsp").forward(request, response);	
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
