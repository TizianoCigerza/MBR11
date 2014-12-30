package mbrapp.tiziano.mbr;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
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

import login_control.LoginFunctions;
import login_control.LoginScreen;

/**
 * Created by Tiziano on 21/11/2014.
 */
public class KmFunctions {

    public HttpParams httpParams = new BasicHttpParams();
    public final HttpClient httpclient = new DefaultHttpClient(httpParams);
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

    public void formatPost(String nomeTexto, String placaTexto, String destinoTexto, String kmTexto, TextView textView) throws Exception {
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
        value.append(nomeTexto);


        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(value.toString().getBytes(), 0, value.length());

            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (Exception e) {
            System.out.println("Md5 exception: "+e.getMessage());
        }


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("placa", placaTexto));
        nameValuePairs.add(new BasicNameValuePair("destino", destinoTexto));
        nameValuePairs.add(new BasicNameValuePair("quilometragem", kmTexto));


        String request = "http://10.71.71.166/mbr11/auth/public_execute/" + horaAtual + "/" + nomeTexto + "/" + md5 + "/gravar_quilometragem"; //10.71.71.3 \ 186.251.17.18:8080
        //String request = "http://186.251.17.18.8080/mbr11/auth/public_execute/" + horaAtual + "/" + nomeTexto + "/" + md5 + "/gravar_quilometragem";
        final HttpPost httppost = new HttpPost(request);

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
                            } catch (UnsupportedEncodingException e) {
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


    public static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }



    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                String str = KmFunctions.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                Editable editable = ediTxt.getText();
                isUpdating = true;
                ediTxt.setText(mascara);
                afterTextChanged(editable);
                ediTxt.setSelection(mascara.length());
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {
                if(s.length()<3){
                    ediTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                }else if(s.length()>=3){
                    ediTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
                }

            }

            };
        }

}






