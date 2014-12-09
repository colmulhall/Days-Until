package com.colm.daysuntil;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class DisplayEvent
{
	// calculate the days between the two dates. This uses the Jodatime library
    public String daysUntil(LocalDate eventDate)
    {
    	LocalDate todaysDate = new LocalDate();
        LocalDate endDate = new LocalDate(2014, 12, 25);
        
        String daysBetween = Days.daysBetween(todaysDate, endDate).toString();
        daysBetween = daysBetween.substring(1, daysBetween.length()-1);
    	 
    	return daysBetween;
    }
}