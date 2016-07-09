package Yelp;

import java.io.File;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import FileLibrary.JSONToTSV;
import HiveLibrary.HiveClient;

public class YelpMaster {

	private static HiveClient hc;
	private static JSONToTSV jt;
	
	public static void tipLoadToHive() throws ParseException, IOException{
		jt.jsonToTsv("business_id,user_id,text,date,likes", "yelp_academic_dataset_tip.json", "yelp_academic_dataset_tip.txt", "\t");
		hc.loadFromFileToTable(new File("yelp_academic_dataset_tip.txt").getAbsolutePath(), "tip", true);
	}
	public static void reviewLoadToHive() throws ParseException, IOException{
		jt.jsonToTsv("business_id,user_id,stars,text,date,votes", "yelp_academic_dataset_review.json", "yelp_academic_dataset_review.txt", "\t");
		hc.loadFromFileToTable(new File("yelp_academic_dataset_review.txt").getAbsolutePath(), "review", true);
	}
	public static void businessLoadToHive() throws ParseException, IOException{
		jt.jsonToTsv("business_id,name,full_address,city, state,open,categories,review_count,stars,neighborhoods,longitude,latitude,attributes", "yelp_academic_dataset_business.json", "yelp_academic_dataset_business.txt", "\t");
		hc.loadFromFileToTable(new File("yelp_academic_dataset_business.txt").getAbsolutePath(), "business", true);
	}
	public static void usersLoadToHive() throws ParseException, IOException{
		jt.jsonToTsv("user_id, name, review_count, average_stars, votes, friends, elite, yelping_since, compliments, fans", "yelp_academic_dataset_user.json", "yelp_academic_dataset_user.txt", "\t");
		hc.loadFromFileToTable(new File("yelp_academic_dataset_user.txt").getAbsolutePath(), "users", true);
	}
	
	public static void main(String[] args) throws Exception {
		jt = new JSONToTSV();
		hc = new HiveClient("yelp", true);
		
//		businessLoadToHive();
//		usersLoadToHive();
//		reviewLoadToHive();
//		tipLoadToHive();
	}
}
