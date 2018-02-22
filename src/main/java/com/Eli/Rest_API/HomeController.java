package com.Eli.Rest_API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		//logger.info("Patient Data is :", locale);
		try{
			Class.forName("org.postgresql.Driver");
//			DriverManager.registerDriver(new org.postgresql.Driver());

			Connection con = DriverManager.getConnection("jdbc:postgresql://10.18.0.55:5432/cloud_postgre","postgres","MDoffice2002");
			PreparedStatement stmt = con.prepareStatement("select first_name,last_name,middle_initial,soc_sec_number from patients Limit 10");
			ResultSet rs = stmt.executeQuery();
			    
			JSONArray json = new JSONArray();
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()) {
			  int numColumns = rsmd.getColumnCount();
			  JSONObject obj = new JSONObject();
			  for (int i=1; i<=numColumns; i++) {
			    String column_name = rsmd.getColumnName(i);
			    obj.put(column_name, rs.getObject(column_name));
			  }
			  json.put(obj);
			}
			
			model.addAttribute("DatabaseData", json );
			
		} catch(Exception ex){
			logger.info(ex.getMessage().toString(), locale);
		}
		
		return "home";
	}
	
}
