package arashrasoulzadeh.codepack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class signin extends AppCompatActivity {
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 46;
    SignInButton signinbutton;
    AQuery aq;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        aq = new AQuery(this);
        signinbutton = (SignInButton) findViewById(R.id.sign_in_button);

        sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        ((TextView) findViewById(R.id.signOutLink)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(signin.this, "this", Toast.LENGTH_SHORT).show();
                sp.edit().remove("logedin").apply();
                sp.edit().remove("personName").apply();
                sp.edit().remove("personPhotoUrl").apply();
                sp.edit().remove("email").apply();
                updateUi();

            }
        });
        updateUi();
        ((TextView) findViewById(R.id.registerLink)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //url = https://accounts.google.com/SignUp?hl=en
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/SignUp?hl=en"));
                startActivity(browserIntent);
            }
        });
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void updateUi() {

        if (sp.getBoolean("logedin", false)) {

            signinbutton.setVisibility(View.GONE);
            aq.id(R.id.avatar).image(sp.getString("personPhotoUrl", ""));
            ((TextView) findViewById(R.id.logindetails)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.registerLink)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.signOutLink)).setVisibility(View.VISIBLE);
        } else {
            signinbutton.setVisibility(View.VISIBLE);

            ((TextView) findViewById(R.id.signOutLink)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.logindetails)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.registerLink)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.avatar)).setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personPhotoUrl = "";
            personPhotoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : null;
            String email = acct.getEmail();
            aq.id(R.id.avatar).image(personPhotoUrl);
            sp.edit().putBoolean("logedin", true).apply();
            sp.edit().putString("personName", personName).apply();
            sp.edit().putString("personPhotoUrl", personPhotoUrl).apply();
            sp.edit().putString("email", email).apply();
            updateUi();
            //   mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.

            //  updateUI(false);
        }
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
}
