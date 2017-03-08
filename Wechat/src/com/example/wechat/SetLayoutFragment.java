package com.example.wechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  我页面碎片
 */

public class SetLayoutFragment extends Fragment implements OnClickListener{
    private View view;
    private Button btnAbout,btnLogout,btnExit;
    private TextView User;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.set_layout, container, false);
        btnAbout=(Button) view.findViewById(R.id.about);
        initView();
        
        return view;
    }
    private void initView(){
    	sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
    	 btnAbout=(Button) view.findViewById(R.id.about);
         btnLogout=(Button) view.findViewById(R.id.logout);
         btnExit=(Button) view.findViewById(R.id.exit);
         User=(TextView) view.findViewById(R.id.user); 
         btnAbout.setOnClickListener(this);
         btnLogout.setOnClickListener(this);
         btnExit.setOnClickListener(this);
         User.setText(sharedPreferences.getString("account", ""));
    }
    public void onClick(View v) {

        switch (v.getId()){
        case R.id.about:
        	startActivity(new Intent(getActivity(), About.class));
            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        	break;
        case R.id.logout:
        	startActivity(new Intent(getActivity(), LoginActivity.class));
        	getActivity().finish();
        	break;
        case R.id.exit:
        	getActivity().finish();
        	break;
        }
    }
}
