package souza.hm.com.randomuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
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
        final Spinner spLeg = (Spinner) findViewById(R.id.spinnerLEG);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                https://randomuser.me/api/?gender=male&nat=br
//                https://randomuser.me/api/?gender=male&nat=br&lego
                GeneratePerson(String.valueOf(spgender.getSelectedItem()),String.valueOf(spNat.getSelectedItem()),String.valueOf(spLeg.getSelectedItem()));

            }
        });

    }
    //render view to bitmap android

    //
    void GeneratePerson(String genderpick, String natpick,String lepick)
    {
//
        String requestUrl;
        if (lepick=="Human")
        {
             requestUrl = "https://randomuser.me/api/?gender="+genderpick+"&nat="+natpick;
        }else
            {
                requestUrl = "https://randomuser.me/api/?gender="+genderpick+"&nat="+natpick+"&lego";
            }


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
                final TextView nomeUser = (TextView) findViewById(R.id.user_name);
                final TextView state = (TextView) findViewById(R.id.user_state);
                final TextView nickname = (TextView) findViewById(R.id.nick_name);
                final TextView password = (TextView) findViewById(R.id.password);
                final ImageView userAvatar = (ImageView) findViewById((R.id.user_avatar));

                final String jsonData = response.body().string();
                Log.i("getProfileInfo", jsonData);
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CardView userCard = (CardView) findViewById(R.id.card_user_info);
                            try {
                                JSONObject rootObj = new JSONObject(jsonData);
                                JSONArray results = (JSONArray) rootObj.get("results");
                                JSONObject resultObject = (JSONObject) results.get(0);

                                JSONObject ge_name = (JSONObject) resultObject.get("name");
                                JSONObject ge_location = (JSONObject) resultObject.get("location");
                                JSONObject ge_login = (JSONObject) resultObject.get("login");
                                JSONObject ge_img = (JSONObject) resultObject.get("picture");

                                if (rootObj != null) {

                                    nomeUser.setText(ge_name.get("first").toString()+" "+ge_name.get("last").toString());
                                    state.setText(ge_location.get("city").toString()+"-"+ge_location.get("state").toString());
                                    nickname.setText("Username: "+ge_login.get("username").toString());
                                    password.setText("Password: "+ge_login.get("password").toString());

                                    Glide.with(MainActivity.this).load(ge_img.get("medium"))
                                            .asBitmap()
                                            .fitCenter()
                                            .into(userAvatar);

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
