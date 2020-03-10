package it.spootifyrest.model.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private final static SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private final static SimpleDateFormat fullCETDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");

	/*
	 * Convenience method to add a specified number of minutes to a Date object
	 * From:
	 * http://stackoverflow.com/questions/9043981/how-to-add-minutes-to-my-date
	 * 
	 * @param minutes The number of minutes to add
	 * 
	 * @param beforeTime The time that will have minutes added to it
	 * 
	 * @return A date object with the specified number of minutes added to it
	 */
	public static Date addMinutesToDate(int minutes, Date beforeTime) {
		final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

		long curTimeInMs = beforeTime.getTime();
		Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
		return afterAddingMins;
	}

	/**
	 * @return una stringa dd-MM-yyyy HH:mm:ss z
	 */
	public static String convertDateToFullCETDateString(Date date) {
		return fullCETDateFormat.format(date);
	}

	/**
	 * 
	 * @param fullCETString una stringa dd-MM-yyyy HH:mm:ss z
	 * @return oggetto Date
	 */
	public static Date ConvertFullCETStringToDate(String fullCETString) {
		try {
			return fullCETDateFormat.parse(fullCETString);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @return una stringa yyyy-MM-dd
	 */
	public static String convertDateToSqlDateString(Date date) {
		return sqlDateFormat.format(date);
	}
}
