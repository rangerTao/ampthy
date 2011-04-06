package com.weibo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weibo.OAuth.OAuth;
import com.weibo.utils.WeiboUtils;

public class Weibo extends Activity {
    /** Called when the activity is first created. */
	EditText etName;
	EditText etPasswd;
	Button btnLogin;
	Button btnCancel;
	
	Weibo appref;
	
	//the instance of OAuth
	OAuth oauth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initView();
    }
    
    public void initView(){
    	appref = this;
    	
    	oauth = new OAuth();
    	
    	//init the compenent in the view;
    	etName = (EditText)findViewById(R.id.etUsername);
    	etPasswd = (EditText)findViewById(R.id.etPasswd);
    	
    	btnLogin = (Button)findViewById(R.id.btnLogin);
    	btnCancel = (Button)findViewById(R.id.btnCancel);
    }
    
    OnClickListener loginListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == btnLogin.getId()){
				String username = etName.getText().toString();
				String passwd = etPasswd.getText().toString();
				
				//check the mail address.
				if(!WeiboUtils.isEmail(username)){
					Toast.makeText(appref, R.string.uNameError, 3000);
				}else{
					oauth.RequestAccessToken(appref, "app://Weibo");
				}
			}else if (arg0.getId() == btnCancel.getId()){
				System.exit(0);
			}

		}
    	
    };
}