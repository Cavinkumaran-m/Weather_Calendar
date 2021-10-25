package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Web_Scrapper {
	
	String final_page = new String();
	String[] tech_data = new String[4];
	
   public Web_Scrapper() {
	   
       try{
    	   String ip = ip_reader();
    	   String api_key = "A5tUxWRSLePNOjOTU8ED5e5KshCgIIwb";
    	   String document = Jsoup.connect("http://dataservice.accuweather.com/locations/v1/cities/ipaddress?apikey=" + api_key + "&q=" + ip + "&language=en-us&details=true").ignoreContentType(true).execute().body();
           try{
        	   FileWriter fw = new FileWriter("temp.json");    
        	   fw.write(document);    
               fw.close();   
           }
           catch (Exception e) {
        	   System.out.println(e);
           }
           
           String[] json_data = json_parser();
           
           String link = "https://www.accuweather.com/en/" + json_data[0] + "/" + json_data[2] + "/" + json_data[1] + "/weather-forecast/" + json_data[3];
           Document document2 = Jsoup.connect(link).get();
           Elements links = document2.select("a.header-link");
           for (Element link_ : links) {
        	   if(link_.text().equals("Monthly")) {
        		   final_page = "https://www.accuweather.com" + link_.attr("href");
   				}
           }          
       }
       catch (IOException ioe) {
    	   System.out.println("Check Your Internet Connection");
    	   System.out.println("Got Problem With the I.P Tracking site"
    	   		+ "Actual Error is:\n" + ioe);
       }
   }
   
   public String ip_reader() {
	   
	   String systemipaddress = "";
	   try
       {
		   
           URL url_name = new URL("http://bot.whatismyipaddress.com");
           BufferedReader sc =
           new BufferedReader(new InputStreamReader(url_name.openStream()));  
           systemipaddress = sc.readLine().trim();
       }
       catch (Exception e)
       {
           systemipaddress = "Cannot Execute Properly";
           System.out.println("Public Ip Can't be retrieved");
       }
	   return (systemipaddress);
   }
   
   public String[] json_parser() {
	   
	   Path path = Paths.get("temp.json");
	   StringBuilder sb = new StringBuilder();
	   try (Stream<String> stream = Files.lines(path)) {
		   stream.forEach(s -> sb.append(s).append("\n"));
	   } catch (IOException ex) {
		   System.out.println(ex);
	   }

	   String contents = sb.toString();
	   Object json_ = JSONValue.parse(contents);
	   JSONObject json_decoded = (JSONObject)json_;
	   JSONObject country = (JSONObject)json_decoded.get("Country");
	   String country_id = (String)country.get("ID");
	   String location_key = (String)json_decoded.get("Key");
	   String city_n = (String)json_decoded.get("EnglishName");
	   String city_name = city_n.replaceAll(" ", "-").toLowerCase();
	   String postal_code = (String)json_decoded.get("PrimaryPostalCode");
	   if(postal_code == "") {
		   postal_code = location_key;
	   }
	   
	   tech_data[0] = country_id;
	   tech_data[1] = postal_code;
	   tech_data[2] = city_name;
	   tech_data[3] = location_key;
	   
	   File f= new File("temp.json");
	   f.delete();
	   return tech_data;
	   }
   
   public boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
   
   public String get_location() {
	   return tech_data[2];
   }
   
   public String get_url() {
	   return final_page;
   }
   public ArrayList<String[]> get_weather_data() {
	   
	   ArrayList<String[]> weather_data = new ArrayList<String[]>();
	   try {
		   String f_page = this.final_page;
		   Document doc = Jsoup.connect(f_page).get();
		   Elements cal = doc.select("div.monthly-calendar");
		   
			
		   String temp = cal.text();
		   String[] data = temp.split(" "); 
		   String[] data2 = new String[3];
		   int i = 0;
			
		   for(String a: data) {
				
			   if(isNumeric(a)) {
					i = 0;
					data2[i] = a;
					i++;
			   }
			   else {
				   if(a.contains("N/A")) {
					   data2[i] = a;
					   data2[i+1] = a;
					   weather_data.add(data2);
					   data2 = new String[3];
				   }
				   else {
					   data2[i] = a;
					   if(i<2) {
						   i++;
					   }
					   else {
						   weather_data.add(data2);
						   data2 = new String[3];
					   }
					}	
				}
			}
	   }
	   catch(Exception e){
		   System.out.println(e);
	   }
	   return weather_data;
   }
}