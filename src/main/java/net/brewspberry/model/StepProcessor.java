package net.brewspberry.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.ISpecificIngredientService;
import net.brewspberry.business.beans.Brassin;
import net.brewspberry.business.beans.DurationBO;
import net.brewspberry.business.beans.Etape;
import net.brewspberry.business.beans.Houblon;
import net.brewspberry.business.beans.Levure;
import net.brewspberry.business.beans.Malt;
import net.brewspberry.business.service.EtapeServiceImpl;
import net.brewspberry.business.service.HopServiceImpl;
import net.brewspberry.business.service.MaltServiceImpl;
import net.brewspberry.business.service.YeastServiceImpl;
import net.brewspberry.util.LogManager;

public class StepProcessor implements Processor<Brassin> {

	IGenericService<Etape> etapeService = (IGenericService<Etape>) new EtapeServiceImpl();
	ISpecificIngredientService maltIngSpecService = (ISpecificIngredientService) new MaltServiceImpl();
	IGenericService<Malt> maltService = new MaltServiceImpl();
	IGenericService<Houblon> hopService = new HopServiceImpl();
	IGenericService<Levure> yeastService = new YeastServiceImpl();
	ISpecificIngredientService hopIngSpecService = (ISpecificIngredientService) new HopServiceImpl();
	ISpecificIngredientService levureIngSpecService = (ISpecificIngredientService) new YeastServiceImpl();
	private Logger logger = LogManager.getInstance(StepProcessor.class
			.getName());

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	@Override
	public Boolean record(Brassin currentBrassin, HttpServletRequest request) {

		/**
		 * Whether stepID is null or not this will fill the fields
		 */
		String currentStepID = (String) request.getAttribute("step_id");
		String currentStepLabel;
		String currentStepDuration;
		String currentStepTemperature;
		String currentStepComment;
		Date currentStepBeginningDate;
		Date currentStepEndDate;
		String currentStepNumber;

		Etape currentStep = new Etape();
		
		
		if (currentStepID != null) {
			/*
			 * If there's an ID field, means the step has already been created, 
			 * so setting it an ID and overwriting other fields
			 */
			try {
				currentStep.setEtp_id(Long.parseLong(currentStepID));
			} catch (Exception e) {
				logger.severe(currentStepID + " step ID is not a number !!");
				currentStep.setEtp_id(null);
			}
		}
		
		

		// Attaching brew to step
		if (currentBrassin != null && currentBrassin.getBra_id() != null){
			currentStep.setEtp_brassin(currentBrassin);
		}



		// Step beginning date
		if (request.getParameter("step_beginning") != null) {

			try {
				currentStepBeginningDate = sdf.parse(request
						.getParameter("step_beginning"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (currentStep.getEtp_debut() == null) {
				
				try {
					currentStepBeginningDate = sdf.parse(request.getParameter("step_beginnging"));
					currentStep.setEtp_debut(currentStepBeginningDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.severe(request.getParameter("step_beginnging") + " cannot be converted to Date. Considering now ()");
					currentStep.setEtp_debut(new Date());
				}


			}

		}

		// Step end date

		if (request.getParameter("step_end") != null) {

			try {

				currentStepEndDate = sdf.parse(request.getParameter("step_end"));

				currentStep.setEtp_fin(currentStepEndDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.severe(request.getParameter("step_end") + " cannot be converted to Date. Considering now ()");
				currentStep.setEtp_fin(new Date());
			}

		}

		// Step Label

		if (request.getParameter("step_label") != null) {

			currentStepLabel = request.getParameter("step_label");

			currentStep.setEtp_nom(currentStepLabel);

		}

		// Step duration

		if (request.getParameter("step_duration") != null) {

			currentStepDuration = request.getParameter("step_duration");

			DurationBO duration = new DurationBO();

			duration.setMinute(Long.parseLong(currentStepDuration));

			currentStep.setEtp_duree(duration);

		}

		// Step theoretical temperature

		if (request.getParameter("step_temperature") != null) {

			currentStepTemperature = request.getParameter("step_temperature");

			try {
			currentStep.setEtp_temperature_theorique(Double
					.parseDouble(currentStepTemperature));
			}
			catch (Exception e){
				
				logger.severe("Couldn't convert temperature "+ currentStepTemperature);
			}
		}

		// Température théorique de l'étape

		if (request.getParameter("step_comment") != null) {

			currentStepComment = request.getParameter("step_comment");

			currentStep.setEtp_remarque(currentStepComment);

		}

		// Numéro de l'étape

		if (request.getParameter("step_number") != null) {

			currentStepNumber = request.getParameter("step_number");

			currentStep.setEtp_numero(Integer.parseInt(currentStepNumber));

		}

		

		// Enregistrement de l'étape
		try {
			if (currentStep.getEtp_id() > 0) {
				currentStep = etapeService.update(currentStep);
				logger.info("Updating Step with ID " + currentStep.getEtp_id());
			} else {
				currentStep = etapeService.save(currentStep);
				logger.info("Saving Step with ID " + currentStep.getEtp_id());
			}
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			logger.severe("Got an error while saving step...");

			return false;
		}

	}
}
