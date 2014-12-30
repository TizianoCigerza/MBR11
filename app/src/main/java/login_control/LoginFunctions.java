package login_control;

import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Tiziano on 20/11/2014.
 */
public class LoginFunctions {
    public HttpParams httpParams = new BasicHttpParams();
    public final HttpClient httpclient = new DefaultHttpClient(httpParams);

    public void formatPost(String login, String senha, TextView textView) throws Exception {
        String horaAtual;
        LoginScreen m = new LoginScreen();
        LoginFunctions f = new LoginFunctions();
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar data = Calendar.getInstance();
        sdf.applyPattern("yyyyMMddHHmmss");
        horaAtual = sdf.format(data.getTime());
        String md5 = null;
        StringBuilder value = new StringBuilder();

        value.append(horaAtual); // concatena a string do md5 (timestamp + address(ip) + usuario
        value.append(f.getLocalIpAddress());
        value.append(login);


        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(value.toString().getBytes(), 0, value.length());

            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (Exception e) {
            System.out.println("Md5 exception: " + e.getMessage());
        }

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("login", login));
        nameValuePairs.add(new BasicNameValuePair("senha", senha));

        //-------------------------------------------------------------------------
        String request="URL A SER PASSADA";

        final HttpPost httppost = new HttpPost(request);//WAITING URL
        //-------------------------------------------------------------------------
        try {

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            post(httppost, httpclient, m, textView);

        } catch (UnsupportedEncodingException e) {

            System.out.println("Post parameters exception: " + e.getMessage());

        }
    }

    public static void post(final HttpPost httppost, final HttpClient httpclient, final LoginScreen activity, final TextView textView){
        new Thread(){
            public void run(){
                try {
                    HttpResponse httpResponse = httpclient.execute(httppost);
                    final String entityFinal = EntityUtils.toString(httpResponse.getEntity());

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                byte texto[]=entityFinal.getBytes("ISO-8859-1");
                                String byteText = new String(texto, "UTF-8");
                                textView.setText(byteText);
                                sleep(1000);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }catch(IOException e){
                    System.out.println("Post exception: "+ e.getMessage());
                }
            }
        }.start();
    }






    public String getLocalIpAddress() {
        try {
            String ipv4;
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {
                        return ipv4;
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
