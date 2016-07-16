package yelp;

import java.io.File;

import clients.FileClient;
import clients.HiveClient;
import templates.FileTemplates;
import templates.HiveTemplates;

public class YelpMaster {
	
	final public static boolean ENABLE_LOGIN = true;
	final public static boolean DISABLE_LOGIN = false;
	
	private static HiveClient hc;
	private static FileClient fc;
	private static HiveTemplates ht;
	private static FileTemplates ft;
	
	public static void checkinLoadToHive() throws Exception{
		ft.transformJsonToTsv("business_id,checkin_info", "yelp_academic_dataset_checkin.json", "yelp_academic_dataset_checkin.txt", "\t", fc);
		ht.loadFromFileToTable(new File("yelp_academic_dataset_checkin.txt").getAbsolutePath(), "checkin", true, hc);
	}
	public static void tipLoadToHive() throws Exception{
		ft.transformJsonToTsv("business_id,user_id,text,date,likes", "yelp_academic_dataset_tip.json", "yelp_academic_dataset_tip.txt", "\t", fc);
		ht.loadFromFileToTable(new File("yelp_academic_dataset_tip.txt").getAbsolutePath(), "tip", true, hc);
	}
	public static void reviewLoadToHive() throws Exception{
		ft.transformJsonToTsv("review_id, business_id,user_id,stars,text,date,votes", "yelp_academic_dataset_review.json", "yelp_academic_dataset_review.txt", "\t", fc, 0);
		ht.loadFromFileToTable(new File("yelp_academic_dataset_review.txt").getAbsolutePath(), "review", true, hc);
	}
	public static void businessLoadToHive() throws Exception{
		ft.transformJsonToTsv("business_id,name,full_address,city, state,open,categories,review_count,stars,neighborhoods,longitude,latitude,attributes", "yelp_academic_dataset_business.json", "yelp_academic_dataset_business.txt", "\t", fc);
		ht.loadFromFileToTable(new File("yelp_academic_dataset_business.txt").getAbsolutePath(), "business", true, hc);
	}
	public static void usersLoadToHive() throws Exception{
		ft.transformJsonToTsv("user_id, name, review_count, average_stars, votes, friends, elite, yelping_since, compliments, fans", "yelp_academic_dataset_user.json", "yelp_academic_dataset_user.txt", "\t", fc);
		ht.loadFromFileToTable(new File("yelp_academic_dataset_user.txt").getAbsolutePath(), "users", true, hc);
	}
	
	public static void main(String[] args) throws Exception {
		fc = new FileClient();
		hc = new HiveClient("yelp", ENABLE_LOGIN);
		
		ht = new HiveTemplates(); 
		ft = new FileTemplates(); 
		
		businessLoadToHive();
//		usersLoadToHive();
//		reviewLoadToHive();
//		tipLoadToHive();
//		checkinLoadToHive();
	}
}
