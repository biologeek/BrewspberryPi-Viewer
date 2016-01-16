package net.brewspberry.controller;

import java.awt.image.RescaleOp;
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
import net.brewspberry.util.Formats;
import net.brewspberry.business.beans.Brassin;
import net.brewspberry.business.beans.Houblon;
import net.brewspberry.business.beans.Ingredient;
import net.brewspberry.business.beans.Levure;
import net.brewspberry.business.beans.Malt;
import net.brewspberry.business.service.BrassinServiceImpl;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.exceptions.DAOException;

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
		Long currentBrassinID;
		String currentDateDebut;
		String currentDateFin;
		String currentBrassinNom;
		String currentBrassinStatut;
		String currentBrassinQte = "";
		String[] currentBrassinMalts;
		String[] currentBrassinMaltsQte;
		String[] currentBrassinHoublons;
		String[] currentBrassinHoublonsQte;
		String[] currentBrassinHoublonsType;
		String[] currentBrassinLevures;
		String[] currentBrassinLevuresQte;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		logger.info("doPost");
		Enumeration<String> parameterNames = request.getParameterNames();

		List<String[]> paramList = new ArrayList<String[]>();

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

		// Date de début du brassin
		if (request.getParameter("dateDebut") != null) {

			currentBrassin = new Brassin();
			try {
				currentDateDebut = request.getParameter("dateDebut");
				System.out.println(currentDateDebut);
				System.out.println(sdf.toString());
				System.out.println(currentBrassin.toString());

				currentBrassin.setBra_debut(sdf.parse(currentDateDebut));

			} catch (ParseException e) {

				e.printStackTrace();
			}

		}
		// Date de fin du brassin

		if (request.getParameter("dateFin") != null) {

			try {

				currentDateFin = request.getParameter("dateFin");

				currentBrassin.setBra_fin(sdf.parse(currentDateFin));

			} catch (ParseException e) {

				e.printStackTrace();
			}

		}

		// "Nom" du brassin
		if (request.getParameter("brassinNom") != null) {

			try {

				currentBrassinNom = request.getParameter("brassinNom");

				currentBrassin.setBra_nom(currentBrassinNom);

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		// Quantité finale du brassin
		if (request.getParameter("brassinQte") != null) {

			try {

				currentBrassinQte = request.getParameter("brassinQte");

				System.out.println("Qte : " + currentBrassinQte);
				currentBrassin.setBra_quantiteEnLitres(Double
						.parseDouble(currentBrassinQte));

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		// Statut du brassin
		if (request.getParameter("statutBrassin") != null) {

			try {

				currentBrassinStatut = request.getParameter("statutBrassin");

				if (currentBrassinStatut == "")
					currentBrassin.setBra_statut(10);
				else
					currentBrassin.setBra_statut(Integer
							.parseInt(currentBrassinStatut));

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		// Malts et quantités
		if ((request.getParameterValues("malt") != null)
				&& (request.getParameterValues("maltQte") != null)) {

			try {

				currentBrassinMalts = request.getParameterValues("malt");
				currentBrassinMaltsQte = request.getParameterValues("maltQte");

				currentBrassin.setBra_malts(maltIngSpecService
						.getIngredientFromArrayId(currentBrassinMalts));

				int i = 0;
				if (currentBrassinMalts.length == currentBrassinMaltsQte.length) {

					// Pour chaque malt enregistré, on définit la quantité
					for (Malt malt : currentBrassin.getBra_malts()) {
						logger.info("Got " + currentBrassinMalts.length
								+ " malts, brew mid=" + currentBrassinMalts[i]);

						malt.setIng_quantite(Double
								.parseDouble(currentBrassinMaltsQte[i]));

					}
				} else {
					throw new Exception(currentBrassinMaltsQte.length
							+ " quantités et " + currentBrassinMalts.length
							+ " malts recus !");
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		// Récupération des houblons

		if ((request.getParameterValues("houblon") != null)
				&& (request.getParameterValues("houblonQte") != null)
				&& (request.getParameterValues("houblonType") != null)) {

			try {

				currentBrassinHoublons = request.getParameterValues("houblon");
				currentBrassinHoublonsQte = request
						.getParameterValues("houblonQte");
				currentBrassinHoublonsType = request
						.getParameterValues("houblonType");

				currentBrassin.setBra_houblons(hopIngSpecService
						.getIngredientFromArrayId(currentBrassinHoublons));

				int i = 0;
				if ((currentBrassinHoublons.length == currentBrassinHoublonsQte.length)
						&& (currentBrassinHoublons.length == currentBrassinHoublonsType.length)) {
					for (Houblon houblon : currentBrassin.getBra_houblons()) {
						// Pour chaque houblon enregistré, on définit la
						// quantité

						logger.info("Got " + currentBrassinHoublons.length
								+ " hops, brew hop id="
								+ currentBrassinHoublons[i]);
						houblon.setIng_quantite(Double
								.parseDouble(currentBrassinHoublonsQte[i]));
						houblon.setHbl_type(Integer
								.parseInt(currentBrassinHoublonsType[i]));
					}

				} else {
					throw new Exception(currentBrassinHoublonsQte.length
							+ " quantités et " + currentBrassinHoublons.length
							+ " houblons recus !");
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		// Récupération des levures

		if ((request.getParameterValues("levure") != null)
				&& (request.getParameterValues("levureQte") != null)) {

			try {

				currentBrassinLevures = request.getParameterValues("levure");
				currentBrassinLevuresQte = request
						.getParameterValues("levureQte");

				currentBrassin.setBra_levures(levureIngSpecService
						.getIngredientFromArrayId(currentBrassinLevures));

				int i = 0;
				if (currentBrassinLevures.length == currentBrassinLevuresQte.length) {
					for (Levure levure : currentBrassin.getBra_levures()) {
						// Pour chaque levure enregistrée, on définit la
						// quantité
						logger.info("Got " + currentBrassinLevures.length
								+ " yeasts, brew yeast id="
								+ currentBrassinLevures[i]);

						levure.setIng_quantite(Double
								.parseDouble(currentBrassinLevuresQte[i]));

					}
				} else {
					throw new Exception(currentBrassinLevuresQte.length
							+ " quantités et " + currentBrassinLevures.length
							+ " levures recus !");
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		// If not null => updates brew
		if (request.getParameter("brassinID") != null
				&& request.getParameter("brassinID") != "") {

			try {
				Long.parseLong(request.getParameter("brassinID"));

				currentBrassin.setBra_id(Long.parseLong(request
						.getParameter("brassinID")));
			} catch (Exception e) {

				logger.severe(request.getParameter("brassinID")
						+ " is not a number !!");

			}

		}

		currentBrassin.setBra_date_maj(new Date());
		logger.info(currentBrassin.toString());

		// Enregistrement du brassin
		try {
			if (currentBrassin.getBra_id() > 0) {
				currentBrassin = brassinService.update(currentBrassin);
				logger.info("Updating Brassin with ID "
						+ currentBrassin.getBra_id());
			} else {
				currentBrassin = brassinService.save(currentBrassin);
				logger.info("Saving Brassin with ID "
						+ currentBrassin.getBra_id());
			}
			// Enregistrement des malts
			for (Malt malt : currentBrassin.getBra_malts()) {

				if (malt != null)
					maltService.save(malt);
				else
					throw new NullPointerException();
			}

			// Enregistrement des houblons
			for (Houblon hop : currentBrassin.getBra_houblons()) {

				if (hop != null)
					hopService.save(hop);
				else
					throw new NullPointerException();
			}

			// Enregistrement des levures
			for (Levure lev : currentBrassin.getBra_levures()) {

				if (lev != null)
					yeastService.save(lev);
				else
					throw new NullPointerException();
			}

			request.setAttribute("bid", currentBrassin.getBra_id());
			// TODO : rediriger vers la 2 ème page. En attendant, on affiche le
			// brassin
			request.getRequestDispatcher("brew.jsp").forward(request, response);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
