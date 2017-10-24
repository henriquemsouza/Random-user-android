package souza.hm.com.randomuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.search_button);
        final Spinner spgender = (Spinner) findViewById(R.id.spinnerGender);
        final Spinner spNat = (Spinner) findViewById(R.id.spinnerNAT);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(getApplicationContext(),String.valueOf(spgender.getSelectedItem()), Toast.LENGTH_SHORT).show();
                //https://randomuser.me/api/?gender=male&nat=br
                GeneratePerson(String.valueOf(spgender.getSelectedItem()),String.valueOf(spNat.getSelectedItem()));
            }
        });

    }
    //

    //
    void GeneratePerson(String genderpick, String natpick)
    {
//
        String requestUrl = "https://randomuser.me/api/?gender="+genderpick+"&nat="+natpick;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(requestUrl)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("getProfileInfo", "FAIL");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final TextView nomeUser = (TextView) findViewById(R.id.tesID);
                // final TextView nomeUser = (TextView) findViewById(R.id.user_name);
                final TextView state = (TextView) findViewById(R.id.user_state);
                final TextView nickname = (TextView) findViewById(R.id.nick_name);
                final TextView password = (TextView) findViewById(R.id.password);
                // final TextView Uestado = (TextView) findViewById(R.id.estadoID);

                final String jsonData = response.body().string();
               // Log.i("getProfileInfo", jsonData);
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CardView userCard = (CardView) findViewById(R.id.card_user_info);
                            try {
                                JSONObject rootObj = new JSONObject(jsonData);
                                JSONObject subObj = rootObj.getJSONObject("name");
                                //JSONObject subObj = rootObj.get("results");

                                if (rootObj != null) {

                                 //   nomeUser.setText(rootObj.getString("name"));
                                    nomeUser.setText(rootObj.get("name").toString());


                                    userCard.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                userCard.setVisibility(View.GONE);
                                Log.e("bad",e.getMessage());
                            }
                        }
                    });
                }
            }
        });
        //
    }

    //
}
