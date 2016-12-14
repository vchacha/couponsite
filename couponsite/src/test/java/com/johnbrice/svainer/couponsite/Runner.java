package com.johnbrice.svainer.couponsite;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;

public class Runner {
	
	public static void main(String[] args) {
//		DateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z (Jerusalem Standard Time)");
//		DateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z");
//		formatter.setTimeZone(TimeZone.getTimeZone("GMT+2"));
//		String format = formatter.format(new Date());
//		System.out.println(format);
//		Wed Dec 14 2016 00:00:00 GMT+0200
		
		Date formatDate = formatDate("Wed Dec 14 2016 00:00:00");
		
		System.out.println(fromDate(formatDate));
	}
	
	private static Date formatDate(String fromString) {
		//Wed Dec 14 2016
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd yyyy");
//		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		try {
			return simpleDateFormat.parse(fromString);
		} catch (ParseException e) {
			throw new DAOException("cannot parse date: " + fromString + " from coupon table: " + "\n" + e.getMessage()); 
		}
		
	}

	private static String fromDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}
}
