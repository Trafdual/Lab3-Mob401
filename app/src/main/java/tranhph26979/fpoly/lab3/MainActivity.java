package tranhph26979.fpoly.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    TextView tv1;
    EditText edt1, edt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        tv1 = findViewById(R.id.tv);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new GETAsyn().execute();
                new POSTAsync().execute();
            }
        });
    }

    class POSTAsync extends AsyncTask<Void, Void, Void> {
        String pathPost = "https://trafdual.000webhostapp.com/post.php";
        String ketquapost = "";

        //lay du lieu tu server
        @Override
        protected Void doInBackground(Void... voids) {

            try {
//1.chuyen path thanh url
                URL url = new URL(pathPost);
//2.Ma hoa tham so
                String param = "canh=" + URLEncoder.encode(edt1.getText().toString(), "utf-8");
//3. Mo ket noi
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//4. Thiet lap cac thuoc tinh cho urlConnection
                urlConnection.setDoOutput(true);//co lay du lieu tra ve
                urlConnection.setRequestMethod("POST");//su dung phuong thuc post
                urlConnection.setFixedLengthStreamingMode(param.getBytes().length);//do dai cua tham so
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//5. truyen tham so
                PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
                printWriter.print(param);
                printWriter.close();
//6. doc du lieu
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));//bo dem
                StringBuilder stringBuilder = new StringBuilder();//bo chua du lieu
                String line = "";//doc theo dong
                while ((line = br.readLine()) != null)//neu van con du lieu thi van doc
                {
                    stringBuilder.append(line);//dua du lieu vao bo chua
                }
                ketquapost = stringBuilder.toString();//tra ve ket qua
                urlConnection.disconnect();//dong ket noi
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //tra du lieu ve client @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            tv1.setText(ketquapost);
        }
    }

    class GETAsyn extends AsyncTask<Void, Void, Void> {
        String pathGet = "https://trafdual.000webhostapp.com/lab3.php";
        String ketquaget = "";

        @Override
        protected Void doInBackground(Void... voids) {
            pathGet += "?toan=" + edt1.getText().toString() + "&van=" + edt2.getText().toString();
            try {
//chuyen path thanh url
                URL url = new URL(pathGet);
//tao bo dem du lieu
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
//tao bo chua du lieu
                StringBuilder stringBuilder = new StringBuilder();
//bat dau doc dulieu
                String line = "";
                while ((line = br.readLine()) != null) {//neu van con du lieu
                    stringBuilder.append(line);//dua du lieu vao bo chua
                }
                ketquaget = stringBuilder.toString();//tra ve ket qua
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            tv1.setText(ketquaget);
        }
    }
}