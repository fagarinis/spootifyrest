package it.spootifyrest.model.utils;

import java.util.Date;

public class DateUtils {

	
	/*
	*  Convenience method to add a specified number of minutes to a Date object
	*  From: http://stackoverflow.com/questions/9043981/how-to-add-minutes-to-my-date
	*  @param  minutes  The number of minutes to add
	*  @param  beforeTime  The time that will have minutes added to it
	*  @return  A date object with the specified number of minutes added to it 
	*/
	public static Date addMinutesToDate(int minutes, Date beforeTime){
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

	    long curTimeInMs = beforeTime.getTime();
	    Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}
}
