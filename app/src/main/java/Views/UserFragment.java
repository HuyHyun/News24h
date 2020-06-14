package Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.a24hnews.BookmarksActivity;
import com.example.a24hnews.HistoryActivity;
import com.example.a24hnews.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import org.json.JSONException;
import org.json.JSONObject;

import Util.LogUtil;


public class UserFragment extends Fragment {


    private Button btnGopY;
    private Button btnBookmarks, btnHistory, btnDangXuat;
    private Switch swNightMode;
    private CallbackManager callbackManager;
    private TextView textViewName;
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private String name;
    private Boolean checkLogin;
    private SharedPreferences sharedPreferences;
    public static SharedPreferences shareNight;
    public static Boolean checkNight = false;


    public UserFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(requireActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        final View view = inflater.inflate(R.layout.fragment_user, container, false);





        sharedPreferences = this.requireActivity().getSharedPreferences("dataLogin", Context.MODE_PRIVATE);
        shareNight = this.requireActivity().getSharedPreferences("dataNight",Context.MODE_PRIVATE);

        checkNight = shareNight.getBoolean("night",false);



        init(view);
        btnDangXuat.setVisibility(View.INVISIBLE);
        textViewName.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions("public_profile");
        loginButton.setFragment(this);
        setLogin_Button();
        setLogout_Button();

        String checkName = sharedPreferences.getString("fullname", "fail");
        String checkId = sharedPreferences.getString("id", "fail");
        checkLogin = sharedPreferences.getBoolean("checklogin", false);
        if (!checkId.equals("fail")) {

            loginButton.setVisibility(View.INVISIBLE);
            textViewName.setVisibility(View.VISIBLE);
            btnDangXuat.setVisibility(View.VISIBLE);
            textViewName.setText(checkName);
            profilePictureView.setProfileId(checkId);

        }

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), HistoryActivity.class));
            }
        });
        btnBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), BookmarksActivity.class));
            }
        });

        swNightMode.setChecked(checkNight);




        swNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = shareNight.edit();
                if (isChecked) {
                    checkNight = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Intent intent = new Intent(getActivity(), getContext().getClass());
                    e.putBoolean("night", checkNight);
                    e.apply();
                    startActivity(intent);
                    requireActivity().finish();

                } else {
                    checkNight = false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Intent intent = new Intent(getActivity(), getContext().getClass());
                    e.putBoolean("night", checkNight);
                    e.apply();
                    startActivity(intent);
                    requireActivity().finish();

                }
            }
        });


        btnGopY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "boyhsky@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[News 24H] Góp ý của người dùng");
                requireActivity().startActivity(Intent.createChooser(emailIntent, null));
            }
        });








        return view;
    }


    private void setLogout_Button() {
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    btnDangXuat.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    textViewName.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    profilePictureView.setProfileId(null);
                    editor.remove("fullname");
                    editor.remove("id");
                    editor.apply();
                    checkLogin = false;
                    name = "";
            }
        });
    }


    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LogUtil.d("Dangnhap","thanhcong");
                loginButton.setVisibility(View.INVISIBLE);
                textViewName.setVisibility(View.VISIBLE);
                btnDangXuat.setVisibility(View.VISIBLE);
                result();
            }

            @Override
            public void onCancel() {
                LogUtil.d("Dangnhap","that bai");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "No Internet!!!", Toast.LENGTH_SHORT).show();
                LogUtil.d("Dangnhap","khong ket noi");
            }
        });
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    name = object.getString("name");
                    profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                    textViewName.setText(name);
                    checkLogin = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fullname", name);
                    editor.putString("id", profilePictureView.getProfileId());
                    editor.putBoolean("checklogin", checkLogin);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    private void init(View view){
        profilePictureView =  view.findViewById(R.id.friendProfilePicture);
        loginButton =  view.findViewById(R.id.login_button);
        swNightMode =  view.findViewById(R.id.bandem);
        btnHistory =  view.findViewById(R.id.lichsu);
        btnBookmarks =  view.findViewById(R.id.tindaluu);
        textViewName =  view.findViewById(R.id.nameFacebook);
        btnDangXuat =  view.findViewById(R.id.btnDangxuat);
        btnGopY =  view.findViewById(R.id.btngopy);

    }
}
