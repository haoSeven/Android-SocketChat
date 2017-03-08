package com.example.wechat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyOpenHelper extends SQLiteOpenHelper{
public static final String CREAT_USER = "create table userTable(id integer primary key autoincrement,name,pwd)";
public static final String CREAT_Message =	"create table messageTable(id integer primary key autoincrement,name,message,type integer)";
private Context mycContext;
private int type;
private String name;
	public MyOpenHelper(Context context, String name, CursorFactory factory,
			int version, int type) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mycContext=context;
		this.type=type;
		this.name=name;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		if(type==0){
			arg0.execSQL(CREAT_USER);}
		if(type==1){
			arg0.execSQL(CREAT_Message);}
		Toast.makeText(mycContext, "数据库创建成功", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public boolean deleteDatabase(Context context) {  
		return context.deleteDatabase(name);  
		} 

}
