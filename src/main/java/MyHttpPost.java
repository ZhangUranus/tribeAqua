

import org.apache.http.client.config.RequestConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MyHttpPost {

    private static String start = "2019-12-31";
    private static String end = "2020-01-02";

    private static String url1 = "https://www.ctg.com.cn/eportal/ui?moduleId=8a2bf7cbd37c4d4f961ed1a6fbdf1ea8&&struts.portlet.mode=view&struts.portlet.action=/portlet/waterFront!getDatas.action";
    private static String url2 = "https://www.ctg.com.cn/eportal/ui?moduleId=3245f9208c304cfb99feb5a66e8a3e45&&struts.portlet.mode=view&struts.portlet.action=/portlet/waterFront!getDatas.action";

    private static String url3 = "https://www.ctg.com.cn/eportal/ui?moduleId=622108b56feb41b5a9d1aa358c52c236&&struts.portlet.mode=view&struts.portlet.action=/portlet/waterFront!getDatas.action";
    private static String url4 = "https://www.ctg.com.cn/eportal/ui?moduleId=50c13b5c83554779aad47d71c1d1d8d8&&struts.portlet.mode=view&struts.portlet.action=/portlet/waterFront!getDatas.action";
    private static String url5 = "https://www.ctg.com.cn/eportal/ui?moduleId=4f104da2afbc4bf59babd925d469491b&&struts.portlet.mode=view&struts.portlet.action=/portlet/waterPicFront!getDatas.action";


    private static final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();

    public static void main(String[] args) throws Exception, InterruptedException {


        // get the time range


        ArrayList<String> times = getTimeRange();

        ArrayList<String> urls = getURLs();


        for(String d: times){

            for(String u: urls){
                webSpider(d, u);
            }
        }

    }

    private static void webSpider(String d, String u) throws MalformedURLException {
        int responseCode = 0;
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
            responseCode = myHttpConnection.getResponseCode();


            // print out URL details

            System.out.println("----[DATE:"+d+"]-----------------------------------------------------------------");

            // open the contents of the URL as an inputStream and print to stdout
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    myHttpConnection.getInputStream()));
            while ((inputString = in.readLine()) != null) {
                System.out.println(inputString);
            }
            in.close();
            System.out.println("-------------------------------------------------------------------------------------");
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
