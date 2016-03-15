package net.brewspberry.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.dialect.function.StandardAnsiSqlAggregationFunctions;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.ISpecificIngredientService;
import net.brewspberry.business.beans.Brassin;
import net.brewspberry.business.beans.Houblon;
import net.brewspberry.business.beans.Levure;
import net.brewspberry.business.beans.Malt;
import net.brewspberry.business.beans.SimpleLevure;
import net.brewspberry.business.service.BrassinServiceImpl;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.util.LogManager;

public class BrewProcessor implements Processor<Brassin> {

	IGenericService<Brassin> brassinService = (IGenericService<Brassin>) new BrassinServiceImpl();
	ISpecificIngredientService maltIngSpecService = (ISpecificIngredientService) new MaltServiceImpl();
	IGenericService<Malt> maltService = new MaltServiceImpl();
	IGenericService<Houblon> hopService = new HopServiceImpl();
	IGenericService<Levure> yeastService = new YeastServiceImpl();
	ISpecificIngredientService hopIngSpecService = (ISpecificIngredientService) new HopServiceImpl();
	ISpecificIngredientService levureIngSpecService = (ISpecificIngredientService) new YeastServiceImpl();
	private Logger logger = LogManager.getInstance(BrewProcessor.class
			.getName());

	@Override
	public Boolean record(Brassin currentBrassin, HttpServletRequest request) {

		/**
		 * Whether currenBrassin is null or not this will fill the fields
		 */
		Long currentBrassinID;
		String currentDateDebut;
		String currentDateFin;
		String currentBrassinNom;
		String currentBrassinStatut;
		String currentBrassinQte = "";
		String[] currentBrassinMalts;
		String[] currentBrassinMaltsQte;
		String[] currentBrassinMaltsPrix;
		String[] currentBrassinHoublons;
		String[] currentBrassinHoublonsQte;
		String[] currentBrassinHoublonsPrix = null;
		String[] currentBrassinHoublonsType;
		String[] currentBrassinLevures;
		String[] currentBrassinLevuresQte;
		String[] currentBrassinLevuresPrix = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

		// Date de début du brassin
		if (request.getParameter("dateDebut") != null) {

			if (currentBrassin == null){
				currentBrassin = new Brassin();
			}
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
				currentBrassinMaltsPrix = request.getParameterValues("maltsPrix");

				currentBrassin.setBra_malts(maltIngSpecService
						.getIngredientFromArrayId(currentBrassinMalts, currentBrassinMaltsQte, currentBrassinMaltsPrix));

				int i = 0;
				if (currentBrassinMalts.length == currentBrassinMaltsQte.length) {

					// Pour chaque malt enregistré, on définit la quantité
					for (Malt malt : currentBrassin.getBra_malts()) {
						logger.info("Got " + currentBrassinMalts.length
								+ " malts, brew mid=" + currentBrassinMalts[i]);

					//	malt.setIng_quantite(Double
						//		.parseDouble(currentBrassinMaltsQte[i]));

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
						.getIngredientFromArrayId(currentBrassinHoublons, currentBrassinHoublonsQte, currentBrassinHoublonsPrix));

				int i = 0;
				if ((currentBrassinHoublons.length == currentBrassinHoublonsQte.length)
						&& (currentBrassinHoublons.length == currentBrassinHoublonsType.length)) {
					for (Houblon houblon : currentBrassin.getBra_houblons()) {
						// Pour chaque houblon enregistré, on définit la
						// quantité

						logger.info("Got " + currentBrassinHoublons.length
								+ " hops, brew hop id="
								+ currentBrassinHoublons[i]);
							houblon.setIng_quantite(Float
							.parseFloat(currentBrassinHoublonsQte[i]));
						houblon.setShbl_type(Integer
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

				List<Levure> levs = levureIngSpecService
				.getIngredientFromArrayId(currentBrassinLevures, currentBrassinLevuresQte, currentBrassinLevuresPrix);
				currentBrassin.setBra_levures(levs);

				int i = 0;
				if (currentBrassinLevures.length == currentBrassinLevuresQte.length) {
					for (Levure levure : currentBrassin.getBra_levures()) {
						// Pour chaque levure enregistrée, on définit la
						// quantité
						logger.info("Got " + currentBrassinLevures.length
								+ " yeasts, brew yeast id="
								+ currentBrassinLevures[i]);

					//	levure.setIng_quantite(Double
						//		.parseDouble(currentBrassinLevuresQte[i]));

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

				return false;

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
				else {
					throw new NullPointerException();
				}
			}

			// Enregistrement des levures
			for (Levure lev : currentBrassin.getBra_levures()) {

				if (lev != null)
					yeastService.save(lev);
				else
					throw new NullPointerException();
			}
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			logger.severe("Got an error while saving brew or ingredients...");

			return false;
		}

	}
}
