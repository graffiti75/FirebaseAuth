package br.android.cericatto.firebaseauth.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.android.cericatto.firebaseauth.R;
import br.android.cericatto.firebaseauth.databinding.ActivityMainBinding;
import br.android.cericatto.firebaseauth.view.utils.ActivityUtils;

/**
 * MainActivity.java.
 *
 * @author Rodrigo Cericatto
 * @since Jan 28, 2017
 */
public class MainActivity extends AppCompatActivity {

    //--------------------------------------------------
    // Constants
    //--------------------------------------------------

    public static final String TAG = "Auth";
    private static final int LOGIN = 1000;

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    /**
     * Context.
     */

    private MainActivity mActivity = MainActivity.this;
    private ActivityMainBinding mBinding;

    /**
     * Firebase.
     */

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * Others.
     */

    private ProgressDialog mProgressDialog;

    //--------------------------------------------------
    // Activity Life Cycle
    //--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_main);

        showBackArrow(false, getString(R.string.app_name));
        setLayout();
        checkUserStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "[MainActivity]onActivityResult().");
        if (requestCode == LOGIN && resultCode == RESULT_OK) {
            logout();
        }
    }

    @Override
    public void onResume() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //--------------------------------------------------
    // Other Methods
    //--------------------------------------------------

    private void showBackArrow(Boolean homeEnabled, String string) {
        Toolbar toolbar = (Toolbar)findViewById(R.id.id_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(homeEnabled);
            getSupportActionBar().setTitle(string);
        }
    }

    private void setLayout() {
        Log.d(TAG, "[MainActivity]setLayout().");

        mBinding.idActivityMainSignInButton.setOnClickListener(view -> signIn());
        mBinding.idActivityMainCreateAccountButton.setOnClickListener(view -> createAccount());
    }

    private boolean validateForm() {
        Log.d(TAG, "[MainActivity]validateForm().");
        boolean valid = true;

        String email = mBinding.idActivityMainEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mBinding.idActivityMainEmailEditText.setError(getString(R.string.activity_main__mandatory_field));
            valid = false;
        } else {
            mBinding.idActivityMainEmailEditText.setError(null);
        }

        String password = mBinding.idActivityMainPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mBinding.idActivityMainPasswordEditText.setError(getString(R.string.activity_main__mandatory_field));
            valid = false;
        } else {
            mBinding.idActivityMainPasswordEditText.setError(null);
        }
        return valid;
    }

    //--------------------------------------------------
    // Firebase Methods
    //--------------------------------------------------

    private void checkUserStatus() {
        Log.d(TAG, "[MainActivity]checkUserStatus().");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "[MainActivity]checkUserStatus().onAuthStateChanged() -> Signed in: " + user.getUid() + ".");
                Log.d(TAG, "[MainActivity]checkUserStatus().onAuthStateChanged() -> Signed in: " + user.getDisplayName() + ".");
                ActivityUtils.startActivityForResult(mActivity, ChatActivity.class, LOGIN);
            } else {
                Log.d(TAG, "[MainActivity]checkUserStatus().onAuthStateChanged() -> Signed out.");
            }
        };
    }

    private void createAccount() {
        String email = mBinding.idActivityMainEmailEditText.getText().toString();
        String password = mBinding.idActivityMainPasswordEditText.getText().toString();

        Log.d(TAG, "[MainActivity]createAccount() for " + email + ".");
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mActivity, task -> {
            Log.d(TAG, "[MainActivity]createAccount().createUserWithEmailAndPassword() -> On complete: " + task.isSuccessful() + ".");
            if (!task.isSuccessful()) {
                Toast.makeText(mActivity, R.string.activity_main__auth_failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, R.string.activity_main__auth_success, Toast.LENGTH_SHORT).show();
            }
            hideProgressDialog();
        });
    }

    private void signIn() {
        String email = mBinding.idActivityMainEmailEditText.getText().toString();
        String password = mBinding.idActivityMainPasswordEditText.getText().toString();

        Log.d(TAG, "[MainActivity]signIn().");
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(mActivity, task -> {
            Log.d(TAG, "[MainActivity]signIn().signInWithEmailAndPassword() -> On complete: " + task.isSuccessful() + ".");
            if (!task.isSuccessful()) {
                Log.d(TAG, "[MainActivity]signIn().signInWithEmailAndPassword() -> Failed: " + task.getException() + ".");
                Toast.makeText(mActivity, R.string.activity_main__auth_failed, Toast.LENGTH_SHORT).show();
            } else {
                ActivityUtils.startActivityForResult(mActivity, ChatActivity.class, LOGIN);
            }
            hideProgressDialog();
        });
    }

    private void logout() {
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        authentication.signOut();
    }

    //--------------------------------------------------
    // Dialog Methods
    //--------------------------------------------------

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setMessage(getString(R.string.activity_main__loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}