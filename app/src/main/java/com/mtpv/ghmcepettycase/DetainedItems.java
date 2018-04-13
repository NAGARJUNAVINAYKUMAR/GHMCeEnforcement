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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mtpv.ghmcenforcement.BuildConfig;
import com.mtpv.ghmcenforcement.R;

public class DetainedItems extends Activity {

	Button add_btn ;
	public static EditText Itemname_ET,qty_ET, amount_ET;
	public static String items = null ;
	static ArrayList<String> Ditenditems = new ArrayList<String>();
	static StringBuffer detItems=new StringBuffer();
	static StringBuffer detendItemsA=new StringBuffer();
	static LinearLayout detendLinearLayout;
	
	
	Button  back_btn, save_btn, reset_btn ;
	ImageView seized_item_image ;
	
	Button add_Btn, remove1, remove2 , remove3, remove4, remove5 ;
	
	LinearLayout seize_layout2, seize_layout3, seize_layout4, seize_layout5, seize_layout6 ;
	
	int clickcount = 0 ;
	public static String SelPicId = null;
	public static String date, Seize_image;
	public static byte[] seizedImageInbyteArray = null;
	
	public static boolean itemFlg1=false, itemFlg2=false, itemFlg3=false, itemFlg4=false, itemFlg5=false, itemFlg6=false,
			itemFlg7=false, itemFlg8=false, itemFlg9=false, itemFlg10=false;
	
	RadioButton is_shop_seized_yes, is_shop_seized_no ;
	public static String is_shop_seized = "N", seizeItemsTxt  ;
	
	public static StringBuffer seizBuff = new StringBuffer();
	
	Button back_buton, save_button ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detained_items);
		
		Seize_image =null ;
		
		is_shop_seized_yes = (RadioButton)findViewById(R.id.shop_seized_yes);
		is_shop_seized_no = (RadioButton)findViewById(R.id.shop_seized_no);
		
		 back_buton = (Button)findViewById(R.id.back_buton);
		 save_button = (Button)findViewById(R.id.save_button);
		
		seizBuff = new StringBuffer();
		detItems = new StringBuffer();
		detendItemsA = new StringBuffer();
		
		detItems.setLength(0);
		detendItemsA.setLength(0);
		
		seized_item_image = (ImageView)findViewById(R.id.camera_seize);
		
		Itemname_ET = (EditText)findViewById(R.id.Itemname_ET);
		qty_ET =(EditText)findViewById(R.id.qty_ET);
		amount_ET = (EditText)findViewById(R.id.amount_ET);
		detendLinearLayout = (LinearLayout)findViewById(R.id.detendL);
		
		add_btn =(Button)findViewById(R.id.add_Btn);
		
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
		
		
		
		if(Ditenditems==null){
			Ditenditems = new ArrayList<String>();
		}
		Ditenditems = new ArrayList<String>();
		
		
		seized_item_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SeizedItems.SelPicId="1";
				selectImage();	
			}
		});
		
		add_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Itemname_ET.getText().toString().equals("")) {
					Itemname_ET.setError(Html.fromHtml("<font color='black'>Please Enter Item Name</font>"));
					Itemname_ET.requestFocus();
					
				} else if (qty_ET.getText().toString().equals("")) {
					qty_ET.setError(Html.fromHtml("<font color='black'>Please Enetr Quantity</font>"));
					qty_ET.requestFocus();
					
				}/*else if (amount_ET.getText().toString().equals("")) {
					amount_ET.setError(Html.fromHtml("<font color='black'>Please Enetr Amount</font>"));
					amount_ET.requestFocus();
				}*/else {
					LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View addView = layoutInflater.inflate(R.layout.row, null);
					final TextView textOut = (TextView)addView.findViewById(R.id.textout);
					final TextView qty = (TextView)addView.findViewById(R.id.qty);
					final TextView amount = (TextView)addView.findViewById(R.id.amnt);
					textOut.setText(Itemname_ET.getText().toString());
					qty.setText(qty_ET.getText().toString());
					//amount.setText(""+selected_val);
					//items = textOut.getText().toString() +":"+qty.getText().toString()+":"+amount.getText().toString();
					items = textOut.getText().toString() +":"+qty.getText().toString();
					Ditenditems.add(items);
					Log.e("DitendItems", items);
					Itemname_ET.setText("");
					Itemname_ET.requestFocus();
					qty_ET.setText("");
					amount_ET.setText("");
					
					detendLinearLayout.addView(addView);

					Button buttonRemove = (Button) addView.findViewById(R.id.remove);
					buttonRemove.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							((LinearLayout) addView.getParent()).removeView(addView);
							// Ditenditems.clear();
							Ditenditems.remove(items);
						}
					});
				}
			}
		});
		
		back_buton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		save_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> Items = Ditenditems;
				
				for (int i = 0; i < Items.size(); i++) {
					detendItemsA.append(Items.get(i));
					detendItemsA.append(", ");
				}
				System.out.println("Ditenditems:" +Ditenditems);
			// detItems ::(22867): GG:44@
			// detendItemsA :::(22867): GG:44, 
				
				for (int i = 0; i < Items.size(); i++) {
					detItems.append(Items.get(i));
					detItems.append("@");
				}
				
				if (!Items.isEmpty()) {
					detItems = detItems.append("$"+is_shop_seized) ;	
					GenerateCase.seized_tick.setVisibility(View.VISIBLE);
					
				} else {
					detItems.setLength(0);
					detendItemsA.setLength(0);
					GenerateCase.seized_tick.setVisibility(View.GONE);
				}
				 
				Log.i("detItems ::", "" + detItems);
				Log.i("detendItemsA :::", "" + detendItemsA);
				finish();
			}
		});
	}

	protected void selectImage() {
		// TODO Auto-generated method stub
		final CharSequence[] options = { "Open Camera", "Choose from Gallery","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(DetainedItems.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

        	@Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera"))
                {
					if (Build.VERSION.SDK_INT <= 23) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
						startActivityForResult(intent, 1);
					} else {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DetainedItems.this,
								BuildConfig.APPLICATION_ID + ".provider", f));
						intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						startActivityForResult(intent, 1);
					}
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
                    	//seized_item_image.setRotation(90);
                    	ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    	mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream1);
                    	seizedImageInbyteArray = stream1.toByteArray();
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
                if("1".equals(SeizedItems.SelPicId)){
                	
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
                	//seized_item_image.setRotation(90);
                	ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                	mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream1);
                	seizedImageInbyteArray = stream1.toByteArray();
                	
                	 Seize_image = Base64.encodeToString(seizedImageInbyteArray, Base64.NO_WRAP);
                	
                }
            }
        }
    }  
}
