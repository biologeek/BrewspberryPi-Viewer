package net.brewspberry.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.brewspberry.business.IGenericService;
import net.brewspberry.business.ISpecificActionerService;
import net.brewspberry.business.beans.Actioner;
import net.brewspberry.business.beans.Brassin;
import net.brewspberry.business.service.ActionerServiceImpl;
import net.brewspberry.business.service.BrassinServiceImpl;
import net.brewspberry.exceptions.DAOException;
import net.brewspberry.util.Constants;
import net.brewspberry.util.DeviceParser;
import net.brewspberry.util.LogManager;

/**
 * Servlet implementation class Actionner
 */
@WebServlet("/Actionner")
public class ActionnerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ISpecificActionerService actionerService = new ActionerServiceImpl();
	private ActionerServiceImpl genActionerService = new ActionerServiceImpl();

	private BrassinServiceImpl brassinService = new BrassinServiceImpl();
	static Logger logger = LogManager.getInstance(ActionnerServlet.class.toString());

	List<Actioner> actioners = new ArrayList<Actioner>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActionnerServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		long id = 0;

		if (request.getParameter("bid") != null) {

			try {
				id = Long.parseLong(request.getParameter("bid"));
				brassinService.getElementById(id);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (request.getParameter("type") != null) {

			String actionType = request.getParameter("type");

			switch (actionType) {

			case "listActioner":

				logger.info("Entering actioner listing.");

				/*
				 * option used to list all actioners parametered in
				 * devices.properties If actioner has ID in config file,
				 */
				actioners = DeviceParser.getInstance().getDevices(
						Constants.DEVICES_PROPERTIES);

				for (Actioner actioner : actioners) {

					logger.log(Level.FINE,
							"Nom de l'actionner : " + actioner.getAct_nom());

				}

				request.setAttribute("actioners", actioners);

				break;

			case "activate":

				logger.info("Entering actioner activation");

				/*
				 * Activation of Actioner : sets actioner_activated to True sets
				 * Actioner ID in props file
				 */

				String uuid = null;
				Actioner actioner = new Actioner();
				if (request.getParameter("uuid") != null) {

					uuid = request.getParameter("uuid");

					actioner = DeviceParser.getInstance().getDeviceByUUID(
							Constants.DEVICES_PROPERTIES, uuid);

					actioner.setAct_activated(true);

					try {
						logger.info("Saving Actioner " + actioner.getAct_uuid());
						actioner = actionerService.startAction(actioner);
						logger.info("Saved actioner ID : "
								+ actioner.getAct_id());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*
					if (actioner.getAct_id() != 0) {
						try {
							logger.info("Trying to set ID to actioner");
							DeviceParser.getInstance()
									.setIdToActioner(actioner);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else {
						logger.info("Arf shit ! actioner has id=0");
					}
					*/
					/*
					 * // Start action if type=ds18b20 if
					 * (actioner.getAct_type().equals(Constants.ACT_DS18B20)) {
					 * 
					 * try { actioner = actionerService.startAction(actioner); }
					 * catch (Exception e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); } }
					 */
					actioners = DeviceParser.getInstance().getDevices(
							Constants.DEVICES_PROPERTIES);

					request.setAttribute("actioners", actioners);

				}

				break;

			case "deactivate":

				logger.info("Entering actioner deactivation");

				String duuid = null;
				Actioner dactioner = new Actioner();
				if (request.getParameter("uuid") != null) {

					duuid = request.getParameter("uuid");

					dactioner = DeviceParser.getInstance().getDeviceByUUID(
							Constants.DEVICES_PROPERTIES, duuid);

					dactioner.setAct_activated(true);

					try {
						logger.info("Saving Actioner" + dactioner.getAct_uuid());
						dactioner = genActionerService.save(dactioner);
						logger.info("Saved actioner ID : "
								+ dactioner.getAct_id());
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						logger.info("Trying to set ID to actioner");
						DeviceParser.getInstance().setIdToActioner(dactioner);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Start action if type=ds18b20
					if (dactioner.getAct_type().equals(Constants.ACT_DS18B20)) {

						try {
							dactioner = actionerService.startAction(dactioner);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					actioners = DeviceParser.getInstance().getDevices(
							Constants.DEVICES_PROPERTIES);

					request.setAttribute("actioners", actioners);

				}

				break;
			}

		}
		request.getRequestDispatcher("actioners.jsp")
				.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
