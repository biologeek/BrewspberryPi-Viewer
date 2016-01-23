package net.brewspberry.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.ISpecificIngredientService;
import net.brewspberry.business.beans.Brassin;
import net.brewspberry.business.beans.Etape;
import net.brewspberry.business.beans.Houblon;
import net.brewspberry.business.beans.Levure;
import net.brewspberry.business.beans.Malt;
import net.brewspberry.business.service.BrassinServiceImpl;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.exceptions.DAOException;
import net.brewspberry.model.BrewProcessor;
import net.brewspberry.model.Processor;
import net.brewspberry.model.StepProcessor;

/**
 * Servlet implementation class AddOrUpdateBrew
 */
@WebServlet("/AddOrUpdateBrew")
public class AddOrUpdateBrew extends HttpServlet {
	private static final long serialVersionUID = 1L;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static final Logger logger = Logger.getLogger("AddOrUpdate");

	private Long brewid = new Long(0);

	private List<Malt> maltList = new ArrayList<Malt>();

	private List<Houblon> hopList = new ArrayList<Houblon>();
	private List<Levure> yeastList = new ArrayList<Levure>();

	private Brassin currentBrassin = new Brassin();

	IGenericService<Brassin> brassinService = (IGenericService<Brassin>) new BrassinServiceImpl();
	ISpecificIngredientService maltIngSpecService = (ISpecificIngredientService) new MaltServiceImpl();
	IGenericService<Malt> maltService = new MaltServiceImpl();
	IGenericService<Houblon> hopService = new HopServiceImpl();
	IGenericService<Levure> yeastService = new YeastServiceImpl();
	ISpecificIngredientService hopIngSpecService = (ISpecificIngredientService) new HopServiceImpl();
	ISpecificIngredientService levureIngSpecService = (ISpecificIngredientService) new YeastServiceImpl();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddOrUpdateBrew() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      Lors de l'affichage
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		logger.info("doGet");

		if (request.getParameter("bid") != null
				&& request.getParameter("bid") != "") {

			/*
			 * Mise à jour du brassin, - on récupère les données du brassin - on
			 * remplit les champs de la page - on renvoie vers la page
			 */

			brewid = Long.parseLong(request.getParameter("bid"));
			if (brewid > 0) {

				currentBrassin = brassinService.getElementById(brewid);

				if (currentBrassin != null) {

					request.setAttribute("dateDebutValue",
							sdf.format(currentBrassin.getBra_debut()));
					request.setAttribute("dateFinValue",
							sdf.format(currentBrassin.getBra_fin()));
					request.setAttribute("brassinNomValue",
							currentBrassin.getBra_nom());
					request.setAttribute("brassinQteValue",
							currentBrassin.getBra_quantiteEnLitres());
					request.setAttribute("statutValue",
							currentBrassin.getBra_statut());
					request.setAttribute(
							"JSONIngredientsValue",
							generateJSON(maltService.getAllElements(),
									hopService.getAllElements(),
									yeastService.getAllElements()));
					// Les ingrédients sont gérés par la suite dans la page web
					// via du JS

					request.setAttribute("brassinIDValue",
							currentBrassin.getBra_id());
				} else
					throw new NullPointerException();

			} else
				throw new NumberFormatException();

		}
		// Création de brassin
		// On passe les paramètres pour peupler les listes
		logger.info("Création d'un brassin");

		maltList = (new MaltServiceImpl()).getAllDistinctElements();
		hopList = (new HopServiceImpl()).getAllDistinctElements();
		yeastList = (new YeastServiceImpl()).getAllDistinctElements();

		System.out.print("bloublou");
		System.out.print(maltList);

		request.setAttribute("maltList", maltList);
		request.setAttribute("hopList", hopList);
		request.setAttribute("yeastList", yeastList);
		System.out.print(request.getAttribute("maltList"));

		request.getRequestDispatcher("add_update.jsp").forward(request,
				response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      Lors de l'envoi du formulaire
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		logger.info("doPost");
		Enumeration<String> parameterNames = request.getParameterNames();

		List<String[]> paramList = new ArrayList<String[]>();
		 
		Boolean isUpdate;

		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);

			for (int i = 0; i < paramValues.length; i++) {

				String paramValue = paramValues[i];
				String[] strArr = new String[2];
				strArr[0] = paramName.toString();
				strArr[1] = paramValue;
				System.out.println("| " + paramName + " " + paramValue);

			}
			
			

		}
		
		if (request.getParameter("typeOfAdding") != null){
			String typeOfAdding = request.getParameter("typeOfAdding");
			switch (typeOfAdding){
			
			case "brew" :
				
				Processor<Brassin> brewProc = new BrewProcessor();
				
				break;
				
			case "step" :
				
				String stepID = (String) request.getAttribute("step_id");
				String stepLabel = (String) request.getAttribute("step_label");
				String stepDuration = (String) request.getAttribute("step_duration");
				String stepTemperature = (String) request.getAttribute("step_temperature");
				String stepComment = (String) request.getAttribute("step_comment");
				String stepNumber= (String) request.getAttribute("step_number");
				
				
				Processor<Brassin> stepProc = new StepProcessor();

				//////////////////////////////////////////////:
				/// TODO
				
				if (stepID == null) {
					isUpdate = false;
				}
				else {
					isUpdate = true;
				}
				
				
			stepProc.record(currentBrassin, request);
				
				break;
			}

		}
		
			request.setAttribute("bid", currentBrassin.getBra_id());
			// TODO : rediriger vers la 2 ème page. En attendant, on affiche le
			// brassin
			request.getRequestDispatcher("brew.jsp").forward(request, response);
		
	}

	public String generateJSON(List<Malt> malts, List<Houblon> hops,
			List<Levure> yeasts) {

		String JSONmalts = "";
		String JSONhops = "";
		String JSONyeasts = "";
		JSONmalts = JSONmalts + "\"ingredients\" : [";
		if (malts != null) {
			for (Malt malt : malts) {
				JSONmalts = JSONmalts + "{\"typeIng\" :\"malt\",";

				JSONmalts = JSONmalts + "\"id\" : \"" + malt.getIng_id()
						+ "\", " + "\"desc\" : \"" + malt.getIng_desc()
						+ "\", " + "\"cereale\" : \"" + malt.getMalt_cereale()
						+ "\", " + "\"type\" : \"" + malt.getMalt_type()
						+ "\", " + "\"qte\" : \"" + malt.getIng_quantite()
						+ "\"},";
			}
		}
		if (hops != null) {

			for (Houblon hop : hops) {
				JSONhops = JSONhops + "{\"typeIng\" :\"hop\",";

				JSONhops = JSONhops + "\"id\" : \"" + hop.getIng_id() + "\", "
						+ "\"variete\" : \"" + hop.getHbl_variete() + "\", "
						+ "\"acide_alpha\" : \"" + hop.getHbl_acide_alpha()
						+ "\", " + "\"qte\" : \"" + hop.getIng_quantite()
						+ "\"},";
			}
		}
		if (yeasts != null) {
			for (Levure yeast : yeasts) {
				JSONyeasts = JSONyeasts + "{\"typeIng\" :\"yeast\",";

				JSONyeasts = JSONyeasts + "\"id\" : \"" + yeast.getIng_id()
						+ "\", " + "\"desc\" : \"" + yeast.getIng_desc()
						+ "\", " + "\"qte\" : \"" + yeast.getIng_quantite()
						+ "\"},";

			}
			JSONyeasts = JSONyeasts.substring(0, JSONyeasts.length() - 1);
			JSONyeasts = JSONyeasts + "]";
		}

		String result = "{" + JSONmalts + JSONhops + JSONyeasts + "}";
		return result;
	}

}
