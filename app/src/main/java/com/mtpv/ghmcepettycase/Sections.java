package com.mtpv.ghmcepettycase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.ghmcenforcement.R;
import com.mtpv.services.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint({ "InflateParams", "ShowToast" })
public class Sections extends Activity {

	public static EditText add, decription_text, remarks_text;
	Button btn, save_btn,back_btn;
	ImageView reset_btn;
	ListView list;
	ArrayList<String> arr;
	ListAdapter aliAdapter;
	private ListView mainListView;
	@SuppressWarnings("unused")
	private mItems_list[] itemss;
	private ArrayAdapter<mItems_list> listAdapter = null;
	ArrayList<String> checked = new ArrayList<String>();
	public static List<String> checkedList = new ArrayList<String>();

	// public static Map<String,String> idMap=null;
	public static Map<String, String> secMap = null;
	private String blockCharacterSet = ",'";

	Button add_section_btn, remove_section_btn;
	LinearLayout linear1, linear2;
	ListView mylist;

	CheckBox checkBox;
	TextView textView, section_txt;
	DataBase db;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sections);

		SharedPreferences prefs1 = getSharedPreferences("loginValues", MODE_PRIVATE);
		String psName = prefs1.getString("PS_NAME", "");
		String officer_Name1 = prefs1.getString("PID_NAME", "");
		TextView officer_PS = (TextView)findViewById(R.id.officer_PS);
		TextView officer_Name = (TextView)findViewById(R.id.officer_Name);
		TextView companyName = (TextView) findViewById(R.id.CompanyName);
		companyName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.marquee));
		officer_PS.setText(psName);
		officer_Name.setText(officer_Name1);

		section_txt = (TextView) findViewById(R.id.section_text);

		StringBuffer sbf = new StringBuffer();

		sbf.append("\t\tSection \t\t Fine \t\t Section Description");

		section_txt.setText("" + sbf);

		db = new DataBase(getApplicationContext());
		back_btn=(Button)findViewById(R.id.back_btn);

		back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		checkedList = new ArrayList<String>();

		mainListView = (ListView) findViewById(R.id.mylist);
		mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item,
									int position, long id) {

				mItems_list planet = listAdapter.getItem(position);
				Log.i("checked name :", planet.getName());
				if (planet.getName().contains("407(1)")) {
					// 407(1) 50 Prohibition of the tethering of animals
					// in public streets
					Intent pets = new Intent(Sections.this, Pet_count.class);
					startActivity(pets);
				}
				// Sections.checkedList
				// seccode:secname:fine@
				if (Sections.checkedList.contains((planet.getName()))) {
					Log.i("already checked name :", planet.getName());
					Sections.checkedList.remove(planet.getName());
					planet.toggleChecked();
					List_select_ViewHolder viewHolder = (List_select_ViewHolder) item.getTag();
					viewHolder.getCheckBox().setChecked(planet.isChecked());
					viewHolder.getCheckBox().setVisibility(View.VISIBLE);
				} else {
					Sections.checkedList.add(planet.getName());
					planet.toggleChecked();
					List_select_ViewHolder viewHolder = (List_select_ViewHolder) item.getTag();
					viewHolder.getCheckBox().setChecked(planet.isChecked());
					viewHolder.getCheckBox().setVisibility(View.VISIBLE);
				}
				for (String sec : Sections.checkedList) {
					Log.i(" Sections.checkedList :", sec);
				}
			}
		});

		Sections.secMap = new DataBase(getApplicationContext()).getSecMap(getApplicationContext());
		List<String> seclist = new ArrayList<String>();
		List<mItems_list> mlist = new ArrayList<mItems_list>();
		for (String secCode : secMap.keySet()) {
			seclist.add(secMap.get(secCode));
			mlist.add(new mItems_list(secMap.get(secCode)));
		}

		ArrayList<mItems_list> planetList = new ArrayList<mItems_list>();
		// planetList.addAll(Arrays.asList(sections));
		planetList.addAll(mlist);
		// Set our custom array adapter as the ListView's adapter.
		listAdapter = new ListArrayAdapter(this, planetList);
		mainListView.setAdapter(listAdapter);

		save_btn = (Button) findViewById(R.id.save_btn);
		save_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Sections.secMap == null) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Please Click on Violation Name to Select",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, -200);
					View toastView = toast.getView();
					toastView
							.setBackgroundResource(R.drawable.toast_background);
					toast.show();
				} else if (Sections.checkedList != null
						&& Sections.checkedList.size() == 0) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Please Click on Violation Name to Select",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, -200);
					View toastView = toast.getView();
					toastView
							.setBackgroundResource(R.drawable.toast_background);
					toast.show();
				} else {
					finish();

					Log.i("Section List :::", "" + Sections.checkedList);
					Log.i("Section List :::", "" + Sections.checkedList.size());

					GenerateCase.section_tick.setVisibility(View.VISIBLE);



				}
			}
		});

	}

	private InputFilter filter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
								   Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			if (source != null && blockCharacterSet.contains(("" + source))) {
				Toast.makeText(getApplicationContext(), "Not Allowed",
						Toast.LENGTH_LONG);

				return "";
			}
			return null;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, Menu.NONE, "Products");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case 1:

				for (int i = 0; i < checked.size(); i++) {
					Log.d("pos : ", "" + checked.get(i));
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Holds planet data. */
	public class mItems_list {
		private String name = "";
		private boolean checked = false;

		public mItems_list() {
		}

		public mItems_list(String name) {
			this.name = name;

		}

		public mItems_list(String name, boolean checked) {
			this.name = name;
			this.checked = checked;
			Toast.makeText(null, "Hi", Toast.LENGTH_LONG).show();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String toString() {
			return name;
		}

		public void toggleChecked() {
			checked = !checked;
		}
	}

	/** Holds child views for one row. */
	public class List_select_ViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public List_select_ViewHolder() {
		}

		public List_select_ViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;

		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
	}

	/** Custom adapter for displaying an array of Planet objects. */
	public class ListArrayAdapter extends ArrayAdapter<mItems_list> {

		private LayoutInflater inflater;

		public ListArrayAdapter(Context context, List<mItems_list> planetList) {
			super(context, R.layout.section_list, R.id.section_text, planetList);
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			mItems_list planet = (mItems_list) this.getItem(position);

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.section_list, null);

				textView = (TextView) convertView
						.findViewById(R.id.section_text);
				checkBox = (CheckBox) convertView
						.findViewById(R.id.CheckBox_sections);

				checkBox.setVisibility(View.VISIBLE);

				convertView.setTag(new List_select_ViewHolder(textView,
						checkBox));

				// If CheckBox is toggled, update the planet it is tagged with.
				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						CheckBox cb = (CheckBox) v;
						mItems_list planet = (mItems_list) cb.getTag();
						planet.setChecked(cb.isChecked());
					}
				});
			}
			// Re-use existing row view
			else {

				List_select_ViewHolder viewHolder = (List_select_ViewHolder) convertView
						.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			checkBox.setTag(planet);

			// Display planet data
			checkBox.setChecked(planet.isChecked());
			if (checkBox.isChecked()) {
				checkBox.setVisibility(View.VISIBLE);
				textView.setText(planet.getName());
			} else {
				checkBox.setVisibility(View.VISIBLE);
				textView.setText(planet.getName());
			}

			return convertView;
		}

	}

	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg,
				Toast.LENGTH_SHORT);
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
		if (Sections.secMap == null) {
			showToast("Please Select atleasr One Section");
		} else if (Sections.checkedList != null
				&& Sections.checkedList.size() == 0) {
			showToast("Please Select atleast One Section");
		} else {
			showToast("Please Click on Save Button");
			finish();
		}
	}
}