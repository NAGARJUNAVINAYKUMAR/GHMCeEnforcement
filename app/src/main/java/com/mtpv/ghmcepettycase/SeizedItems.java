package com.mtpv.ghmcepettycase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.ghmcenforcement.R;

public class SeizedItems extends Activity {
	
	Button  back_btn, save_btn, reset_btn ;
	ImageView seized_item_image ;
	EditText Itemname_ET, qty_ET, Itemname_ET2, qty_ET2, Itemname_ET3, qty_ET3, 
					Itemname_ET4, qty_ET4, Itemname_ET5, qty_ET5, Itemname_ET6, qty_ET6;
	
	Button add_Btn, remove1, remove2 , remove3, remove4, remove5 ;
	
	LinearLayout seize_layout2, seize_layout3, seize_layout4, seize_layout5, seize_layout6 ;
	
	int clickcount = 0 ;
	public static String SelPicId = null;
	public static String date, Seize_image;
	public static byte[] seizedImageInbyteArray = null;
	
	public static boolean itemFlg1=false, itemFlg2=false, itemFlg3=false, itemFlg4=false, itemFlg5=false, itemFlg6=false,
			itemFlg7=false, itemFlg8=false, itemFlg9=false, itemFlg10=false;
	
	public static ArrayList<String> Ditenditems = new ArrayList<String>();
	
	RadioButton is_shop_seized_yes, is_shop_seized_no ;
	public static String is_shop_seized = "N", seizeItemsTxt  ;
	
	public static StringBuffer seizBuff = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seized_items);

		SharedPreferences prefs1 = getSharedPreferences("loginValues", MODE_PRIVATE);
		String psName = prefs1.getString("PS_NAME", "");
		String officer_Name1 = prefs1.getString("PID_NAME", "");
		TextView officer_PS = (TextView)findViewById(R.id.officer_PS);
		TextView officer_Name = (TextView)findViewById(R.id.officer_Name);
		TextView companyName = (TextView) findViewById(R.id.CompanyName);
		companyName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.marquee));
		officer_PS.setText(psName);
		officer_Name.setText(officer_Name1);

		Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(SeizedItems.this));
		is_shop_seized_yes = (RadioButton)findViewById(R.id.shop_seized_yes);
		is_shop_seized_no = (RadioButton)findViewById(R.id.shop_seized_no);
		
		seizBuff = new StringBuffer();
		Seize_image = null ;
		
		is_shop_seized_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				is_shop_seized = "Y" ;
			}
		});
 
		is_shop_seized_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				is_shop_seized = "N" ;
			}
		});
		
		seized_item_image = (ImageView)findViewById(R.id.camera_seize);
		back_btn = (Button)findViewById(R.id.back_seized);
		save_btn = (Button)findViewById(R.id.save_seized);
		reset_btn = (Button)findViewById(R.id.reset_btnseized);
		
		Itemname_ET = (EditText)findViewById(R.id.Itemname_ET);
		qty_ET = (EditText)findViewById(R.id.qty_ET);
		
		Itemname_ET2 = (EditText)findViewById(R.id.Itemname_ET2);
		qty_ET2 = (EditText)findViewById(R.id.qty_ET2);
		
		Itemname_ET3 = (EditText)findViewById(R.id.Itemname_ET3);
		qty_ET3 = (EditText)findViewById(R.id.qty_ET3);
		
		Itemname_ET4 = (EditText)findViewById(R.id.Itemname_ET4);
		qty_ET4 = (EditText)findViewById(R.id.qty_ET4);
		
		Itemname_ET5 = (EditText)findViewById(R.id.Itemname_ET5);
		qty_ET5 = (EditText)findViewById(R.id.qty_ET5);
		
		Itemname_ET6 = (EditText)findViewById(R.id.Itemname_ET6);
		qty_ET6 = (EditText)findViewById(R.id.qty_ET6);
		
		add_Btn = (Button)findViewById(R.id.add_Btn);
		remove1 = (Button)findViewById(R.id.remove1);
		remove2 = (Button)findViewById(R.id.remove2);
		remove3 = (Button)findViewById(R.id.remove3);
		remove4 = (Button)findViewById(R.id.remove4);
		remove5 = (Button)findViewById(R.id.remove5);
		
		seize_layout2 = (LinearLayout)findViewById(R.id.seize_layout2);
		seize_layout3 = (LinearLayout)findViewById(R.id.seize_layout4);
		seize_layout4 = (LinearLayout)findViewById(R.id.seize_layout3);
		seize_layout5 = (LinearLayout)findViewById(R.id.seize_layout5);
		seize_layout6 = (LinearLayout)findViewById(R.id.seize_layout6);
		
		
		if(Ditenditems==null){
			Ditenditems = new ArrayList<String>();
		}
		Ditenditems = new ArrayList<String>();
		
		
		 ArrayList<String> seizedItemsList =SeizedItems.Ditenditems;
	        if(seizedItemsList!=null){
	        	Log.i("seizedItemsList :::", seizedItemsList.size()+"");
	        	int k=1;
		        for(String siezedItem:seizedItemsList){
		        	Log.i("siezedItem :",siezedItem);
		        	 String []prevseizedItems=siezedItem.toString().split("\\:");
		        	 if(k==1){
		        		 Itemname_ET.setText(prevseizedItems[0]);
		        		 qty_ET.setText(prevseizedItems[1]);
		        	 }
		        	 if(k==2){
		        		 seize_layout2.setVisibility(View.VISIBLE);
		        		 Itemname_ET2.setText(prevseizedItems[0]);
		        		 qty_ET2.setText(prevseizedItems[1]);
		        	 }
		        	 if(k==3){
		        		 seize_layout3.setVisibility(View.VISIBLE);
		        		 Itemname_ET3.setText(prevseizedItems[0]);
		        		 qty_ET3.setText(prevseizedItems[1]);
		        	 }
		        	 if(k==4){
		        		 seize_layout4.setVisibility(View.VISIBLE);
		        		 Itemname_ET4.setText(prevseizedItems[0]);
		        		 qty_ET4.setText(prevseizedItems[1]);
		        	 }
		        	 if(k==5){
		        		 seize_layout5.setVisibility(View.VISIBLE);
		        		 Itemname_ET5.setText(prevseizedItems[0]);
		        		 qty_ET5.setText(prevseizedItems[1]);
		        	 }
		        	 if(k==6){
		        		 seize_layout6.setVisibility(View.VISIBLE);
		        		 Itemname_ET6.setText(prevseizedItems[0]);
		        		 qty_ET6.setText(prevseizedItems[1]);
		        	 }
		        	 
		        	 k++;
		        }
		        
		        
	        }
	        
		seized_item_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SeizedItems.SelPicId="1";
				selectImage();	
			}
		});
		
		add_Btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clickcount=clickcount+1;
				 if(clickcount>4){
				     	Toast toast = Toast.makeText(getApplicationContext(), "You Can Add Only 5 Seized Items", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, -200);
						View toastView = toast.getView();
				    	toastView.setBackgroundResource(R.drawable.toast_background);
					    toast.show();
				 	}
				 else{
					 if(clickcount==1){
						 seize_layout2.setVisibility(View.VISIBLE);
						 Itemname_ET2.setFocusable(true);
					 }
					 if(clickcount==2){
						 seize_layout3.setVisibility(View.VISIBLE);
					 }
					 if(clickcount==3){
						 seize_layout4.setVisibility(View.VISIBLE);
					 }
					 if(clickcount==4){
						 seize_layout5.setVisibility(View.VISIBLE);
					 }
				 }
			}
		});
		
		
		remove1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickcount--;
				seize_layout2.setVisibility(View.GONE);
				Itemname_ET2.setText("");
				qty_ET2.setText("");
				
			}
		});
		
		remove2.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickcount--;
				seize_layout3.setVisibility(View.GONE);
				Itemname_ET3.setText("");
				qty_ET3.setText("");
			}
		});
		
		remove3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickcount--;
				seize_layout4.setVisibility(View.GONE);
				Itemname_ET4.setText("");
				qty_ET4.setText("");
			}
		});
		
		remove4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickcount--;
				seize_layout5.setVisibility(View.GONE);
				Itemname_ET5.setText("");
				qty_ET5.setText("");
			}
		});
		
		remove5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickcount--;
				seize_layout6.setVisibility(View.GONE);
				Itemname_ET6.setText("");
				qty_ET6.setText("");
			}
		});
		
		

		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		reset_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Itemname_ET.setText("");
				Itemname_ET2.setText("");
				Itemname_ET3.setText("");
				Itemname_ET4.setText("");
				Itemname_ET5.setText("");
				Itemname_ET6.setText("");
				
				qty_ET.setText("");
				qty_ET2.setText("");
				qty_ET3.setText("");
				qty_ET4.setText("");
				qty_ET5.setText("");
				qty_ET6.setText("");
				
				seize_layout2.setVisibility(View.GONE);
				seize_layout3.setVisibility(View.GONE);
				seize_layout4.setVisibility(View.GONE);
				seize_layout5.setVisibility(View.GONE);
				seize_layout6.setVisibility(View.GONE);
			}
		});
		
		save_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					Log.i("Entered", "Yes");
					boolean validFlg=true;
					 if(Itemname_ET.getText().toString()!=null && !"".equals(Itemname_ET.getText().toString().trim()) &&
							 qty_ET.getText().toString()!=null && !"".equals(qty_ET.getText().toString().trim())){
						
						seizeItemsTxt = Itemname_ET.getText().toString() +":"+qty_ET.getText().toString();
						 
						 if(SeizedItems.Ditenditems.contains(seizeItemsTxt)){
							 Log.i("item 1 already added", seizeItemsTxt);
						 }else{
							 SeizedItems.Ditenditems.add(seizeItemsTxt+"@");
							 itemFlg1=true;
							 Log.i("ITEM 1 ADDED", Itemname_ET.getText().toString() +":"+qty_ET.getText().toString());
						 }
					 }else if (Itemname_ET.getText().toString()!=null && "".equals(Itemname_ET.getText().toString()) &&
							 qty_ET.getText().toString()!=null && "".equals(qty_ET.getText().toString())) {
						 validFlg=false;
						 	Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Seized Item Name and Quantity ", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, -200);
							View toastView = toast.getView();
					    	toastView.setBackgroundResource(R.drawable.toast_background);
						    toast.show(); 
					}
					 
					 if(seize_layout2.getVisibility()==View.VISIBLE && Itemname_ET2.getText().toString()!=null && !"".equals(Itemname_ET2.getText().toString().trim()) &&
							 qty_ET2.getText().toString()!=null && !"".equals(qty_ET2.getText().toString().trim())){
						
						 String seizeItemsTxt = Itemname_ET2.getText().toString() +":"+qty_ET2.getText().toString();
						 if(SeizedItems.Ditenditems.contains(seizeItemsTxt)){
							 Log.i("item 2 already added", seizeItemsTxt);
						 }else{
							 SeizedItems.Ditenditems.add(seizeItemsTxt+"@");
							 itemFlg2=true;
							 Log.i("ITEM 2 ADDED", Itemname_ET2.getText().toString() +":"+qty_ET2.getText().toString());
						 }
					 }else if (seize_layout2.getVisibility()==View.VISIBLE && Itemname_ET2.getText().toString()!=null && "".equals(Itemname_ET2.getText().toString().trim()) &&
							 qty_ET2.getText().toString()!=null && "".equals(qty_ET2.getText().toString().trim())) {
						 Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Seized Item Name and Quantity ", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, -200);
							View toastView = toast.getView();
					    	toastView.setBackgroundResource(R.drawable.toast_background);
						    toast.show(); 
						 	validFlg=false;
						
					}
					 if(seize_layout3.getVisibility()==View.VISIBLE && Itemname_ET3.getText().toString()!=null && !"".equals(Itemname_ET3.getText().toString().trim()) &&
							 qty_ET3.getText().toString()!=null && !"".equals(qty_ET3.getText().toString().trim())){
						String seizeItemsTxt = Itemname_ET3.getText().toString() +":"+qty_ET3.getText().toString();
						 if(SeizedItems.Ditenditems.contains(seizeItemsTxt)){
							 Log.i("item 3 already added", seizeItemsTxt);
						 }else{
							 SeizedItems.Ditenditems.add(seizeItemsTxt+"@");
							 itemFlg3=true;
							 Log.i("ITEM 3 ADDED", Itemname_ET3.getText().toString() +":"+qty_ET3.getText().toString());
						 }
					 }else if (seize_layout3.getVisibility()==View.VISIBLE && Itemname_ET3.getText().toString()!=null && "".equals(Itemname_ET3.getText().toString().trim()) &&
							 qty_ET3.getText().toString()!=null && "".equals(qty_ET3.getText().toString().trim())) {
						 Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Seized Item Name and Quantity ", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, -200);
							View toastView = toast.getView();
					    	toastView.setBackgroundResource(R.drawable.toast_background);
						    toast.show(); 	
						 	 validFlg=false;
						
					}
					 
					 if(seize_layout4.getVisibility()==View.VISIBLE && Itemname_ET4.getText().toString()!=null && !"".equals(Itemname_ET4.getText().toString().trim()) &&
							 qty_ET4.getText().toString()!=null && !"".equals(qty_ET4.getText().toString().trim())){
						 String seizeItemsTxt = Itemname_ET4.getText().toString() +":"+qty_ET4.getText().toString();
						 if(SeizedItems.Ditenditems.contains(seizeItemsTxt)){
							 Log.i("item 4 already added", seizeItemsTxt);
						 }else{
							 SeizedItems.Ditenditems.add(seizeItemsTxt+"@");
							 itemFlg4=true;
							 Log.i("ITEM 4 ADDED", Itemname_ET4.getText().toString() +":"+qty_ET4.getText().toString());
						 }
					 }else if (seize_layout4.getVisibility()==View.VISIBLE && Itemname_ET4.getText().toString()!=null && "".equals(Itemname_ET4.getText().toString().trim()) &&
							 qty_ET4.getText().toString()!=null && "".equals(qty_ET4.getText().toString().trim())) {
						 Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Seized Item Name and Quantity ", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, -200);
							View toastView = toast.getView();
					    	toastView.setBackgroundResource(R.drawable.toast_background);
						    toast.show(); 
						 	 validFlg=false;
						
					}
					 
					 if(seize_layout5.getVisibility()==View.VISIBLE && Itemname_ET5.getText().toString()!=null && !"".equals(Itemname_ET5.getText().toString().trim()) &&
							 qty_ET5.getText().toString()!=null && !"".equals(qty_ET5.getText().toString().trim())){
						 String seizeItemsTxt = Itemname_ET5.getText().toString() +":"+qty_ET5.getText().toString();
						 if(SeizedItems.Ditenditems.contains(seizeItemsTxt)){
							 Log.i("item 5 already added", seizeItemsTxt);
						 }else{
							 SeizedItems.Ditenditems.add(seizeItemsTxt+"@");
							 itemFlg5=true;
							 Log.i("ITEM 5 ADDED", Itemname_ET5.getText().toString() +":"+qty_ET5.getText().toString());
						 }
					 }else if (seize_layout5.getVisibility()==View.VISIBLE && Itemname_ET5.getText().toString()!=null && "".equals(Itemname_ET5.getText().toString().trim()) &&
							 qty_ET5.getText().toString()!=null && "".equals(qty_ET5.getText().toString().trim())) {
						 Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Seized Item Name and Quantity ", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, -200);
							View toastView = toast.getView();
					    	toastView.setBackgroundResource(R.drawable.toast_background);
						    toast.show(); 	
						 	 validFlg=false;
						
					}
					 if(seize_layout6.getVisibility()==View.VISIBLE && Itemname_ET6.getText().toString()!=null && !"".equals(Itemname_ET6.getText().toString().trim()) &&
							 qty_ET6.getText().toString()!=null && !"".equals(qty_ET6.getText().toString().trim())){
						 String seizeItemsTxt = Itemname_ET6.getText().toString() +":"+qty_ET6.getText().toString();
						 if(SeizedItems.Ditenditems.contains(seizeItemsTxt)){
							 Log.i("item 6 already added", seizeItemsTxt);
						 }else{
							 SeizedItems.Ditenditems.add(seizeItemsTxt+"@");
							 itemFlg6=true;
							 Log.i("ITEM 6 ADDED", Itemname_ET6.getText().toString() +":"+qty_ET6.getText().toString());
						 }
					 }else if (seize_layout6.getVisibility()==View.VISIBLE && Itemname_ET6.getText().toString()!=null && "".equals(Itemname_ET6.getText().toString().trim()) &&
							 qty_ET6.getText().toString()!=null && "".equals(qty_ET6.getText().toString().trim())) {
						 Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Seized Item Name and Quantity ", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, -200);
							View toastView = toast.getView();
					    	toastView.setBackgroundResource(R.drawable.toast_background);
						    toast.show(); 
						 	 validFlg=false;
						
					}
					 
					 Log.i("SeizedItems ::::", ""+SeizedItems.Ditenditems);
					 for(String item:SeizedItems.Ditenditems){
						 Log.i("Ditenditems ::::", item);
						 seizBuff.append(item);
						 
					 }
					 
					 seizBuff.append("$"+is_shop_seized);
					 Log.i("seizBuff :::", ""+seizBuff);
					if(validFlg){
					 finish();
					 GenerateCase.seized_tick.setVisibility(View.VISIBLE);
					}
					
			
			}
		});
	}

	protected void selectImage() {
		// TODO Auto-generated method stub
		final CharSequence[] options = { "Open Camera", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(SeizedItems.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

        	@Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

        	if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                	String current_date = GenerateCase.date;
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "GHMC-ePettyCase" + File.separator + current_date + File.separator + "eCase_no" ;
                    File camerapath = new File(path);
                    if(!camerapath.exists()){
                    	camerapath.mkdirs();
                    }
	                    f.delete();
	                    OutputStream outFile = null;
	                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    
                    try {
                    	Log.i("Camera Path :::",""+file.getAbsolutePath());
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    if("1".equals(SeizedItems.SelPicId)){
                      	Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                    	Paint paint= new Paint();
                    	paint.setColor(Color.RED);
                    	paint.setTextSize(80);
                    	paint.setTextAlign(Paint.Align.CENTER);
                    	String latitude = null;
						String longitude = null;
						canvas.drawText("Date & Time: "+ GenerateCase.Current_Date +" Lat :"+ latitude+ " Long :"+longitude,1250, 1500, paint);
                    	seized_item_image.setImageBitmap(mutableBitmap);
                    	seized_item_image.setRotation(90);
                    	ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    	mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream1);
                    	seizedImageInbyteArray = stream1.toByteArray();
                        Log.i("seizedImageInbyteArray 1::", ""+seizedImageInbyteArray);

                        Seize_image = Base64.encodeToString(seizedImageInbyteArray, Base64.NO_WRAP);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
	            	Uri selectedImage = data.getData();
	                String[] filePath = { MediaStore.Images.Media.DATA };
	                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
	                c.moveToFirst();
	                int columnIndex = c.getColumnIndex(filePath[0]);
	                String picturePath = c.getString(columnIndex);
	                c.close();
	                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
	 //               Bitmap image = (Bitmap) data.getExtras().get("data");
	                Log.w("path of image from gallery......******************.........", picturePath+"");
	                //picture1.setImageBitmap(thumbnail);
                if("1".equals(SeizedItems.SelPicId)){
                    
                	/*seized_item_image.setImageBitmap(thumbnail);
                	ByteArrayOutputStream stream = new ByteArrayOutputStream();
                	thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                	seizedImageInbyteArray = stream.toByteArray();*/
                	
                	Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                	Paint paint= new Paint();
                	paint.setColor(Color.RED);
                	paint.setTextSize(80);
                	paint.setTextAlign(Paint.Align.CENTER);
                	String latitude = null;
					String longitude = null;
					canvas.drawText("Date & Time: "+ GenerateCase.Current_Date +" Lat :"+ latitude+ " Long :"+longitude,1250, 1500, paint);
                	seized_item_image.setImageBitmap(mutableBitmap);
                	seized_item_image.setRotation(90);
                	ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                	mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream1);
                	seizedImageInbyteArray = stream1.toByteArray();
                	
                	 Seize_image = Base64.encodeToString(seizedImageInbyteArray, Base64.NO_WRAP);
                	
                }
            }
        }
    }  
}
