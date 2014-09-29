package com.wan.cropper;

import com.wan.cropper.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button portraitBtn,landscapeBtn;
	private ImageView croppedImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		portraitBtn = (Button)findViewById(R.id.portrait_btn);
		portraitBtn.setOnClickListener(this);
		landscapeBtn = (Button)findViewById(R.id.landscape_btn);
		landscapeBtn.setOnClickListener(this);
		croppedImageView = (ImageView)findViewById(R.id.croppedImage);
		Intent intent = getIntent();
		byte buff[] = (byte[]) intent.getSerializableExtra("bitmap");
		if(buff != null){
			Bitmap bmp=BitmapFactory.decodeByteArray(buff, 0, buff.length);//重新编码出Bitmap对象
		if(bmp != null)
			croppedImageView.setImageBitmap(bmp);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MainActivity.this,TestActivity.class);
		switch (v.getId()) {
		case R.id.portrait_btn:
			intent.putExtra("image", R.drawable.portrait);
			break;
		case R.id.landscape_btn:
			intent.putExtra("image", R.drawable.landscape);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	
}
