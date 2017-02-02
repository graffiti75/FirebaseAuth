package br.android.cericatto.firebaseauth.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.android.cericatto.firebaseauth.R;
import br.android.cericatto.firebaseauth.databinding.ActivityChatBinding;
import br.android.cericatto.firebaseauth.model.Message;
import br.android.cericatto.firebaseauth.view.adapter.MessageAdapter;

/**
 * ChatActivity.java.
 *
 * @author Rodrigo Cericatto
 * @since Jan 28, 2017
 */
public class ChatActivity extends AppCompatActivity {

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    /**
     * Context.
     */

    private ChatActivity mActivity = ChatActivity.this;
    private ActivityChatBinding mBinding;

    /**
     * Firebase.
     */

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;

    /**
     * Adapter.
     */

    private MessageAdapter mAdapter;
    private List<Message> mMessages;

    //--------------------------------------------------
    // Activity Life Cycle
    //--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_chat);

        showBackArrow(false, getString(R.string.activity_chat__title));
        setAdapter();
        setFirebaseInstances();
        setDatabaseReference();
        sendMessage();
    }

    //--------------------------------------------------
    // Menu Methods
    //--------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Integer id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.id_menu_logout:
                setResult(RESULT_OK);
                onBackPressed();
                break;
        }
        return false;
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

    private void setAdapter() {
        mMessages = new ArrayList<>();
        mAdapter = new MessageAdapter(getLayoutInflater(), mMessages);
        mBinding.idActivityChatListView.setAdapter(mAdapter);
    }

    private void sendMessage() {
        Log.d(MainActivity.TAG, "[ChatActivity]sendMessage().");
        mBinding.idActivityChatSendButton.setOnClickListener(v -> {
            // Gets the proper message.
            Message message = new Message();
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            message.setText(mBinding.idActivityChatMessageEditText.getText().toString());
            if (mFirebaseUser.getDisplayName() == null) {
                message.setUser(mFirebaseUser.getEmail());
            } else {
                message.setUser(mFirebaseUser.getDisplayName());
            }

            // Update into the Firebase Server.
            Log.d(MainActivity.TAG, "[ChatActivity]sendMessage() -> Sending " + message.toString()
                + " to Firebase Database.");
            message.setUid(mDatabaseReference.push().getKey());
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(message.getUid(), message.toMap());
            mDatabaseReference.updateChildren(childUpdates);
            mBinding.idActivityChatMessageEditText.setText("");
        });
    }

    //--------------------------------------------------
    // Firebase Methods
    //--------------------------------------------------

    private void setFirebaseInstances() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("messages");
    }

    private void setDatabaseReference() {
        Log.d(MainActivity.TAG, "[ChatActivity]setDatabaseReference().");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(MainActivity.TAG, "[ChatActivity]setDatabaseReference().onDataChange().");
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                mMessages.clear();
                for (DataSnapshot snapshot : dataSnapshots) {
                    Message message = snapshot.getValue(Message.class);
                    mMessages.add(message);
                    Log.d(MainActivity.TAG, "[ChatActivity]setDatabaseReference().onDataChange() -> " +
                        "Re-adding message to adapter: " + message + ".");
                }
                mAdapter.notifyDataSetChanged();
                mBinding.idActivityChatListView.setSelection(mAdapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(MainActivity.TAG, "[ChatActivity]setDatabaseReference().onCancelled().");
            }
        });
    }
}