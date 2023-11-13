package com.example.fall_2022;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //ActionBAr and its title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        firebaseAuth =firebaseAuth.getInstance();
tv=findViewById(R.id.Textv);
    }
    private void checkUserStatus(){
        //get Current user
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            //user is signed stay here
            //set email of logged in user
tv.setText(user.getEmail().toString());
        }else{
            //  user not signed go to main actvivty
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            finish();;
        }
    }
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    protected void onStart() {
// check on start of app
        checkUserStatus();
        super.onStart();

    }



    /*inflate options menu*/

    public boolean onCreateOptionsMenu (Menu menu) {
//inflating menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //get item id
        int id=item.getItemId();
if(id==R.id.action_logout){
firebaseAuth.signOut();
checkUserStatus();
}
return super.onOptionsItemSelected(item);
    }
        /*handle menu item clicks*/

}