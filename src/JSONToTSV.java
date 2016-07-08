import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONToTSV {
	public static String executeHiveQuery(String query) throws IOException {
		
		// TODO
		String command = "source " + System.getenv("HOME") + "/.bash_profile; " + "cd /usr/local/Cellar/hive/2.0.1; ./bin/hive -e \"" + query + "\"";
		String[] queryArray = {"/bin/bash", "-c", command};
		
		ProcessBuilder builder = new ProcessBuilder(queryArray);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
		                                    
		return "";
	}

	public static String JSONArrayToString(JSONArray array){
		if(array.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<array.size(); i++){
			sb.append(array.get(i) + ", ");
		}
		return sb.toString().substring(0, sb.length()-2);
	}

	public static void businessJsonToTsv() throws ParseException, IOException{

		String businessId, name, address, city, state, categories, neighborhoods, attributes;
		boolean open;
		int reviewCount;
		float stars;
		double longitude, latitude;

		JSONParser parser = new JSONParser();
		JSONObject obj;
		File businessIn = new File("yelp_academic_dataset_business.json");
		Scanner sc = new Scanner(businessIn);

		File outFile = new File ("yelp_academic_dataset_business.txt");
		if(outFile.exists() && !outFile.isDirectory())
			outFile.delete();
		PrintWriter pWriter = new PrintWriter (new FileWriter (outFile));

		while (sc.hasNextLine()) {

			// READ FROM FILE
			obj = (JSONObject)parser.parse(sc.nextLine().replaceAll("\\\\n", " "));

			businessId= obj.get("business_id").toString();
			name = obj.get("name").toString();
			address = obj.get("full_address").toString();
			city = obj.get("city").toString();
			state = obj.get("state").toString();
			open = Boolean.valueOf(obj.get("open").toString());
			categories = JSONArrayToString((JSONArray)obj.get("categories"));
			reviewCount = Integer.parseInt(obj.get("review_count").toString());
			stars = Float.parseFloat(obj.get("stars").toString());
			neighborhoods = JSONArrayToString((JSONArray)obj.get("neighborhoods"));
			longitude = Double.parseDouble(obj.get("longitude").toString());
			latitude = Double.parseDouble(obj.get("latitude").toString());
			attributes = obj.get("attributes").toString();

			// WRITE TO FILE
			pWriter.println(businessId + "\t" + name + "\t" + address + "\t" + city + "\t" + state + "\t" + open + "\t" + categories + "\t" + reviewCount + "\t" + stars + "\t" + neighborhoods + "\t" + longitude + "\t" + latitude + "\t" + attributes);
		}
		sc.close();
		pWriter.close();

	}
	public static void businessLoadToHive() throws ParseException, IOException{
		businessJsonToTsv();

	}
	public static void main(String[] args) throws Exception {
		businessLoadToHive();
		executeHiveQuery("USE yelp; TRUNCATE TABLE business; LOAD DATA LOCAL INPATH '/Users/vgoel1/Documents/workspace/YelpReviews/yelp_academic_dataset_business.txt' INTO TABLE business;");
	}
}
