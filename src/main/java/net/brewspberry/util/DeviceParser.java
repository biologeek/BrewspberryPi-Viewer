package net.brewspberry.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import net.brewspberry.business.beans.Actioner;
import net.brewspberry.business.service.ActionerServiceImpl;

public class DeviceParser extends ConfigLoader {

	static DeviceParser instance = null;

	public static Logger logger = Logger.getLogger(DeviceParser.class
			.toString());

	ActionerServiceImpl actionerService = new ActionerServiceImpl();

	public DeviceParser() {

		super();
	}

	public static DeviceParser getInstance() {

		if (instance == null) {

			logger.info("Creating new DeviceParser instance");
			instance = new DeviceParser();

		}

		return instance;
	}

	/**
	 * From Properties file path
	 * 
	 * @param path
	 *            : Properties file path
	 * @return
	 */
	public List<Actioner> getDevices(String path) {

		List<Actioner> result = new ArrayList<Actioner>();

		File propsFile = new File(path);

		logger.info("Entering getDevices. Conf : " + path);

		if (propsFile.exists() && !propsFile.isDirectory()) {

			Properties props = null;
			try {
				props = super.openFile(path);

				String[] pins = props.getProperty("device.pins").split(";");
				String[] names = props.getProperty("device.names").split(";");
				String[] uuids = props.getProperty("device.uuids").split(";");
				String[] types = props.getProperty("device.types").split(";");
				String[] ids = props.getProperty("device.ids").split(";");

				if (pins.length == names.length && names.length == uuids.length
						&& uuids.length == types.length) {

					for (int i = 0; i < pins.length; i++) {

						Actioner actioner = new Actioner();

						if (actioner.getAct_id() == 0) {

							actioner.setAct_nom(names[i]);
							actioner.setAct_type(types[i]);
							actioner.setAct_uuid(uuids[i]);
							actioner.setAct_raspi_pin(pins[i]);
							actioner.setAct_used(false);

						} else {
							actioner = actionerService.getElementById(Long
									.parseLong(ids[i]));
						}

						result.add(actioner);

					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return result;
	}

	public Actioner getDeviceByUUID(String path, String uuid) {

		Actioner result = null;

		List<Actioner> actioners = getDevices(path);

		logger.info("getDeviceByUUID : " + actioners.size()
				+ " devices found !");

		for (Actioner actioner : actioners) {

			logger.info(actioner.getAct_uuid() + " - " + uuid);
			if (actioner.getAct_uuid().equals(uuid))
				result = actioner;

		}
		logger.info("getDeviceByUUID - Result : " + result);

		return result;
	}

	public void setIdToActioner(Actioner actioner) throws Exception {

		List<Actioner> devices = getDevices(Constants.DEVICES_PROPERTIES);
		Boolean found = false;

		if (devices.size() > 0) {

			// Modifying devices with new ID
			for (Actioner device : devices) {

				logger.info("Device : " + device.getAct_uuid() + " "
						+ device.getAct_id());
				logger.info("Actioner : " + actioner.getAct_uuid() + " "
						+ actioner.getAct_id());
				if (device.getAct_uuid() == actioner.getAct_uuid()) {

					if (found) {

						throw new Exception("Actioner already found ERROR");

					} else {
						if (actioner.getAct_id() != 0) {
							found = true;
							device.setAct_id(actioner.getAct_id());
						}
					}

				}
			}

			// Setting new Properties file
			setDevices(devices);
		}
	}

	private void setDevices(List<Actioner> devices) {
		Properties props = null;

		try {
			props = super.openFile(Constants.DEVICES_PROPERTIES);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String dev_id = "";
		String dev_pin = "";
		String dev_name = "";
		String dev_uuid = "";
		String dev_type = "";

		for (Actioner device : devices) {

			dev_id = dev_id + device.getAct_id() + ";";
			dev_pin = dev_pin + device.getAct_raspi_pin() + ";";
			dev_name = dev_name + device.getAct_nom() + ";";
			dev_uuid = dev_uuid + device.getAct_uuid() + ";";
			dev_type = dev_type + device.getAct_type() + ";";
		}

		dev_id = dev_id.substring(0, dev_id.length() - 1);
		dev_pin = dev_pin.substring(0, dev_pin.length() - 1);
		dev_name = dev_name.substring(0, dev_name.length() - 1);
		dev_uuid = dev_uuid.substring(0, dev_uuid.length() - 1);
		dev_type = dev_type.substring(0, dev_type.length() - 1);

		if (props != null) {
			logger.info("Devices written : device.ids" + dev_id);

			props.setProperty("device.ids", dev_id);
			props.setProperty("device.pins", dev_pin);
			props.setProperty("device.names", dev_name);
			props.setProperty("device.uuids", dev_uuid);
			props.setProperty("device.types", dev_type);
		}

		// Saving properties file
		try {
			props.store(new FileOutputStream(new File(
					Constants.DEVICES_PROPERTIES)), "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
