package pack;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zack
 */
public class RESTSend {
    
    public final String USER_AGENT = "Mozilla/5.0";
    private String URL_Target;
    private String URL_Param;
    public JSONObject json;
    
    public RESTSend(String _URL_Target, String _URL_Param) {
        URL_Target = _URL_Target;
        URL_Param = _URL_Param.replace(" ", "%20");;
        json = null;
    }
    
    public void sendPost() throws Exception {
        // KIRIM
        // init connection
        URL url = new URL(URL_Target);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        // POST Method
        con.setDoOutput(true);
        con.setRequestMethod("POST");        
        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
        dos.writeBytes(URL_Param);
        dos.flush();
        dos.close();
        
        con.setReadTimeout(15000);
        con.connect();
        
        // TERIMA RESPON
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String stemp;
        while ((stemp = br.readLine()) != null) {
            sb.append(stemp);
        }
        JSONParser parser = new JSONParser();
        json = (JSONObject) parser.parse(sb.toString());
    }
    
    public void sendGet() throws ParseException {
        try {
            // KIRIM
            // init Connection
            String urlRequest = URL_Target + URL_Param;
            URL url = new URL(urlRequest);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            // GET Method gaada...
            
            // TERIMA RESPON
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String stemp;
            while ((stemp = br.readLine()) != null) {
                sb.append(stemp);
            }
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(sb.toString());
            
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public JSONObject getJSON() {
        return json;
    }
    
}
