import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;



public class MyHttpPost {


    private static Logger logger = Logger.getLogger(MyHttpPost.class);

    private static String start = null;
    private static String end = null;

    private static String url1 = null;
    private static String url2 = null;

    private static String url3 = null;
    private static String url4 = null;
    private static String url5 = null;
    private static String numTreads = null;

    public static void main(String[] args) throws Exception {


        loadConfig();


        getData();

    }

    private static void getData() throws ParseException {
        ArrayList<String> times = getTimeRange();
        ArrayList<String> urls = getURLs();

        ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(numTreads));


        for(String d: times){

            for(String u: urls){
                executor.submit(() -> {
                    try {
                        webSpider(d, u);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


            }
        }
    }

    private static void loadConfig() throws Exception{

        Configuration config = new PropertiesConfiguration("src/main/resources/config.properties");
        start = config.getString("start");
        end = config.getString("end");
        url1 = config.getString("url1");
        url2 = config.getString("url2");
        url3 = config.getString("url3");
        url4 = config.getString("url4");
        url5 = config.getString("url5");
        numTreads = config.getString("numTreads");

    }

    private static void webSpider(String d, String u) throws MalformedURLException {
        String inputString = null;

        URL url = new URL(u);

        try {
            // Get an HttpURLConnection subclass object instead of URLConnection
            HttpURLConnection myHttpConnection = (HttpURLConnection) url.openConnection();

            // ensure you use the GET method
            myHttpConnection.setRequestMethod("POST");
            myHttpConnection.setDoOutput(true);

            // create the query params
            StringBuffer queryParam = new StringBuffer();
            queryParam.append("time=");
            queryParam.append(d);


            // Output the results
            OutputStream output = myHttpConnection.getOutputStream();
            output.write(queryParam.toString().getBytes());
            output.flush();

            // get the response-code from the response
            myHttpConnection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    myHttpConnection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
                logger.debug("[DATE:"+d+"]"+inputString);
            }
            in.close();

        } catch (Exception e) {
        }
    }

    private static ArrayList<String> getURLs() {
        ArrayList<String> urls = new ArrayList<String>();

        urls.add(url1);
        urls.add(url2);
        urls.add(url3);
        urls.add(url4);
        urls.add(url5);

        return urls;
    }

    private static ArrayList<String> getTimeRange() throws ParseException {
        Date startTime = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date endTime = new SimpleDateFormat("yyyy-MM-dd").parse(end);

        ArrayList<String> times = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(startTime);
        while(calendar.getTime().before(endTime)) {

            times.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.HOUR, 24);
        }
        return times;
    }
}
