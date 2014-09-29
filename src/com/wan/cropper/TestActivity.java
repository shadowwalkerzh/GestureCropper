package com.wan.cropper;

import java.io.ByteArrayOutputStream;

import com.wan.cropper.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity implements OnClickListener {

	private CropImageView cropImageView;
	private Button cropBtn;
	private Bitmap croppedImage;

	private Drawable testDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		cropBtn = (Button) findViewById(R.id.Button_crop_test);
		cropBtn.setOnClickListener(this);
		cropImageView = (CropImageView) findViewById(R.id.CropImageViewTest);
		int drawableId = getIntent().getIntExtra("image",
				R.drawable.ic_launcher);
		testDrawable = getResources().getDrawable(drawableId);
		cropImageView.setImageResource(drawableId);
		System.out.println("111111111111111111111111111=========="
				+ testDrawable.getIntrinsicWidth() + "========="
				+ testDrawable.getIntrinsicHeight());
		if (testDrawable.getIntrinsicWidth() > testDrawable
				.getIntrinsicHeight()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		cropImageView.setAspectRatio(
				AppConstants.DEFAULT_ASPECT_RATIO_VALUES_X,
				AppConstants.DEFAULT_ASPECT_RATIO_VALUES_Y);
		cropImageView.setFixedAspectRatio(true);
		cropImageView.setGuidelines(0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Button_crop_test:
			croppedImage = cropImageView.getCroppedImage();
			byte buff[] = new byte[2*1024*1024];//看图有多大,自己看着改
			buff = Bitmap2Bytes(croppedImage);
			Intent myIntent = new Intent(TestActivity.this,MainActivity.class);
			myIntent.putExtra("bitmap",buff);
			startActivity(myIntent);
			break;

		default:
			break;
		}
	}

	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

}
