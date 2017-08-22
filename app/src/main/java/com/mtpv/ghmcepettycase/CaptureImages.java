package com.mtpv.ghmcepettycase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.ghmcenforcement.R;

public class CaptureImages extends Activity implements LocationListener {

	ImageView capture_encroachment ;
	Button back_btn, next_image_btn ;
	
	static String date;
    public static String Current_Date;
    
    
    /* GPS VALUES */
   	// flag for GPS status
   	boolean isGPSEnabled = false;
   	// flag for network status
   	boolean isNetworkEnabled = false;
   	boolean canGetLocation = false;
   	// The minimum distance to change Updates in meters
   	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
   	// The minimum time between updates in milliseconds
   	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
       LocationManager m_locationlistner;
   	android.location.Location location;

   	public static double latitude = 0.0;
   	public static double longitude = 0.0;
   	
   	byte[] byteArray;
	String imgString=null ;
	CountDownTimer newtimer ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_capture_images);
		
		
		getLocation();
		
		newtimer = new CountDownTimer(1000000000, 50) { 

            public void onTick(long millisUntilFinished) {
            	date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
        		
        		Calendar c1 = Calendar.getInstance();
        		int mSec = c1.get(Calendar.MILLISECOND);
        		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        		String strdate1 = sdf1.format(c1.getTime());
        		date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        		//id_date.setText(strdate1);
        		Current_Date=strdate1;
            }
            public void onFinish() {

            }
        };
        newtimer.start();
		
		capture_encroachment = (ImageView)findViewById(R.id.encroachment_image);
		
		back_btn = (Button)findViewById(R.id.back_Btn);
		
		next_image_btn = (Button)findViewById(R.id.next_image);
		
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				capture_encroachment.setImageDrawable(getResources().getDrawable(R.drawable.photo));
				imgString = null ;
				finish();
				newtimer.cancel();
			}
		});
		
		next_image_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (imgString!=null) {
					capture_encroachment.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					imgString = null ;	
					showToast("Captured Image Saved Succesufuly !!!");
				}else {
					showToast("Please Capture Image to Move to Next Image");
				}
				
			}
		});
		
		capture_encroachment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});
	}

	private void getLocation() {
		try {
			m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				latitude = 0.0;
				longitude = 0.0;
			} else {
				this.canGetLocation = true;
				String current_time;
				String current_date = null;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (m_locationlistner != null) {
						location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							
							long time = location.getTime();
							Date date = new Date(time);
							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String gps_Date = sdf.format(date);
							System.out.println(gps_Date); 
							
							
							//new Async_GetGPS_Address().execute();
							
						} else {
							latitude = 0.0;
							longitude = 0.0;
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						m_locationlistner.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (m_locationlistner != null) {
							location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								
								long time = location.getTime();
								Date date = new Date(time);
								

								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String gps_Date = sdf.format(date);
								System.out.println(gps_Date); 
								
							} else {
								latitude = 0.0;
								longitude = 0.0;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
	}

	protected void selectImage() {
		// TODO Auto-generated method stub
			final CharSequence[] options = { "Open Camera", "Cancel" };
			AlertDialog.Builder builder = new AlertDialog.Builder(CaptureImages.this);
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
	                /*else if (options[item].equals("Choose from Gallery"))
	                {
	                	
	                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                    startActivityForResult(intent, 2);
	                }*/
	                else if (options[item].equals("Cancel")) {
	                    dialog.dismiss();
				}
	            }
	        });
	        builder.show();
		}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        	
        	String picturePath="";
        	if (requestCode == 1) {
        		
                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
	                	String current_date = CaptureImages.date;
	                    Bitmap bitmap;
	                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
	                            bitmapOptions);
	                    
	                    String path = android.os.Environment
	                            .getExternalStorageDirectory()
	                            + File.separator
	                            + "GHMC-enforcement" + File.separator + "GHMC-Images" + File.separator + current_date;
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
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                    	Paint paint= new Paint();
                    	paint.setColor(Color.RED);
                    	paint.setTextSize(80);
                    	paint.setTextAlign(Paint.Align.CENTER);
                    	
                    	int xPos = (canvas.getWidth() / 2);
                    	int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                    	.descent() + paint.ascent()) / 2));

                    	canvas.drawText("Date & Time: " + Current_Date, xPos,
                            	yPos + 800, paint);
                            	canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
                            	canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);
                    	//canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);
                    
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();
                        
                        new SingleMediaScanner(this, file);
                        
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    	Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                    	Paint paint= new Paint();
                    	paint.setColor(Color.RED);
                    	paint.setTextSize(80);
                    	paint.setTextAlign(Paint.Align.CENTER);
                    	int xPos = (canvas.getWidth() / 2);
                    	int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                    	.descent() + paint.ascent()) / 2));

                    	canvas.drawText("Date & Time: " + Current_Date, xPos,
                            	yPos + 800, paint);
                            	canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
                            	canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);
                    	//canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);
                    
                        capture_encroachment.setImageBitmap(mutableBitmap);
                    	//picture1.setRotation(90);
                    	
                    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    	mutableBitmap.compress(Bitmap.CompressFormat.JPEG,20, bytes);
                        
                        byteArray = bytes.toByteArray();
                    	
                    	imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    	
                        Log.i("imgString 1::", ""+imgString);
                   
                } catch (Exception e) {
                    e.printStackTrace();
                }
        		} else if (requestCode == 2) {
	            	Uri selectedImage = data.getData();
	                String[] filePath = { MediaStore.Images.Media.DATA };
	                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
	                c.moveToFirst();
	                int columnIndex = c.getColumnIndex(filePath[0]);
	                picturePath = c.getString(columnIndex);
	                c.close();
	                
	                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
	                Log.w("path of image from gallery......******************.........", picturePath+"");
                
                	Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                	Paint paint= new Paint();
                	paint.setColor(Color.RED);
                	paint.setTextSize(100);
                	paint.setTextAlign(Paint.Align.CENTER);
                	//canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);
                	
                	int xPos = (canvas.getWidth() / 2);
                	int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                	.descent() + paint.ascent()) / 2));

//                	canvas.drawText("Date & Time: " + Current_Date, xPos,yPos + 800, paint);
//                	canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
//                	canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);
                	
                	capture_encroachment.setImageBitmap(mutableBitmap);
                	//picture1.setRotation(90);
                	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                	mutableBitmap.compress(Bitmap.CompressFormat.JPEG,20, bytes);
                    
                    byteArray = bytes.toByteArray();
                	
                	imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                	
        		}
	        }
	    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			latitude = (float) location.getLatitude();
			longitude = (float) location.getLongitude();
			// speed = location.getSpeed();
		} else {
			latitude = 0.0;
			longitude = 0.0;
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();
		
		ViewGroup group = (ViewGroup) toast.getView();
	    TextView messageTextView = (TextView) group.getChildAt(0);
	    messageTextView.setTextSize(24);
		
    	toastView.setBackgroundResource(R.drawable.toast_background);
	    toast.show();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		newtimer.cancel();
	}
	}
