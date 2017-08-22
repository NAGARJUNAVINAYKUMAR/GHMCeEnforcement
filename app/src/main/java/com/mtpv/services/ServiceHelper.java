package com.mtpv.services;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.mtpv.ghmcepettycase.Login;

public class ServiceHelper {

	private static String NAMESPACE = "http://service.mother.com";
	private static String METHOD_NAME = "authenticateUser";
	public static String SOAP_ACTION = NAMESPACE + METHOD_NAME;

	public static String login_resp = null, psmater_resp = null,
			point_resp = null, print_resp = null, final_resp = null,
			otp_Send_resp = null, otp_verify_resp = null, aadhaar_resp = null,
			sectionmasters_resp = null, sub_sectionmasters_resp = null,
			tinDetails_resp = null, report_resp = null,
			duplicatePrint_resp = null, duplicatePrint_by_Eticket_resp = null,
			tab39b_resp = null, ghmc_hisrty_resp = null,
			prv_hisrty_resp = null, prv_hisrty_resp_firm = null,
			prv_hisrty_resp_tin = null, location_masters_resp = null;

	public static String[] psNames_master;

	public static String[] final_resp_master;
	public static String final_resp_ticketNo;

	public static String[] PointNamesBypsNames_master;

	public static String[] location_master;

	public static String[] temp_sections_master;

	public static String[] section_details_master;

	/* VILATION DETAILS */
	public static String[] sub_section_details_master;
	public static String[][] sub_section_detailed_views;
	static StringBuffer sbuffer_allsub_section;

	// http://125.16.1.70:8080/GHMCWebService/services/GHMCWebServiceImpl?wsdl
	// authenticateUser(String pidCd, String password,String imei,String
	// gpsLattitude,String gpsLongitude);
	public static void getLogin(String pid_Cd, String pass_word,
			String imei_no, String gps_Lattitude, String gps_Longitude) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = new SoapObject(NAMESPACE, "authenticateUser");
			request.addProperty("pidCd", pid_Cd);
			request.addProperty("password", pass_word);
			request.addProperty("imei", imei_no);
			request.addProperty("gpsLattitude", gps_Lattitude);
			request.addProperty("gpsLongitude", gps_Longitude);
			Log.i("login_request :::", ""+ request);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			// Log.i("request", "" + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
			// Log.i("Login.URL ::", "" + Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			login_resp = result.toString();
			if ("NA".equals(login_resp) || "anyType{}".equals(login_resp)
					|| "0".equals(login_resp)) {
				login_resp = null;
			} else {
				login_resp = result.toString();
			}
			Log.i("login_resp :::", "" + login_resp);
			/*23001004|DEVELOPER|2300|TRAFFIC CELL|00|DEVP|WdSt24Pr|0|7893816681|Y|Y|N|N|N|Y|Y*/
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void getPSNames(String unit_code) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = new SoapObject(NAMESPACE, "getPSMaster");
			request.addProperty("pidCd", unit_code);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("getPSNames_request", "" + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
			Log.i("Login.URL ::", "" + Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			psmater_resp = result.toString();
			if ("NA".equals(psmater_resp) || "anyType{}".equals(psmater_resp)
					|| "0".equals(psmater_resp)) {
				psmater_resp = null;
			} else {
				psmater_resp = result.toString();
			}
			Log.i("psmater_resp", "" + psmater_resp);
			psNames_master = new String[0];
			psNames_master = psmater_resp.split("@");
			for (int i = 0; i < ServiceHelper.psNames_master.length; i++) {
				Log.i("**PSNAMES MASTER***", "" + ServiceHelper.psNames_master[i]);
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/* TO GET POINTNAME BY PS_NAME FOR SETTINGS */
	public static void getPointNameByPsNames(String ps_code) {
		try {
			Log.i("***getPointNameByPsNames PS CODE***", "" + ps_code);
			SoapObject request = new SoapObject(NAMESPACE, "getPointDetailsByPscode");
			request.addProperty("psCode", "" + ps_code);
			Log.i("getPointNameByPsNames_request", ""+ request);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			point_resp = result.toString();
			Log.i("**point_resp***", "" + point_resp);
			
			if ("NA".equals(point_resp) || "anyType{}".equals(point_resp)
					|| point_resp == null) {
				point_resp = null;
			} else {
				PointNamesBypsNames_master = new String[0];
				PointNamesBypsNames_master = point_resp.split("@");
				for (int i = 0; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
					Log.i("**POINT_NAME_BY_PSNAMES_MASTER***", "" + ServiceHelper.PointNamesBypsNames_master[i]);
				}
			}
		} catch (SoapFault fault) {
			Log.i("****getPointNameByPsNames SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			PointNamesBypsNames_master = new String[0];
		}
	}

	public static void generate_Challan(String unitCode, String unitName, String bookedPsCode, 
			String bookedPsName, String pointCode, String pointName, String pidCd, String pidName, 
			String password, String cadreCD, String cadre, String onlineMode, String imageEvidence, 
			String offenceImg1, String offenceDt, String offenceTime, String firmRegnNo, String shopName,
			String shopOwnerName, String shopAddress, String location, String psCode, String psName, 
			String respondantName, String respondantFatherName, String respondantAddress,
			String respondantContactNo, String respondantAge, String IDCode, String IDDetails, 
			String witness1Name, String witness1FatherName, String witness1Address, String witness2Name,
			String witness2FatherName, String witness2Address, String detainedItems, String simId, String imeiNo, 
			String macId, String gpsLatitude, String gpsLongitude, String totalFine, String encrHeight, 
			String encrWidth, String encrLength, String shopRunBy, String ghmcCirNo, String ghmcCirName,
			String basedOn, String offenceImg2, String aadharImg, String seizedImg, String sections, String owner_Aadhar,
			String owner_Name, String owner_FName, String owner_Address, String owner_Age, String owner_Mobile, 
			String vdeoEvdnce_Flg, String vdeo_Data) {
		
		Log.i("<--------------->", "<--------------->");
		Log.i("unitCode", "--->" + unitCode);
		Log.i("unitName", "--->" + unitName);
		Log.i("bookedPsCode", "--->" + bookedPsCode);
		Log.i("bookedPsName", "--->" + bookedPsName);
		Log.i("pointCode", "--->" + pointCode);
		Log.i("pointName", "--->" + pointName);
		Log.i("pidCd", "--->" + pidCd);
		Log.i("pidName", "--->" + pidName);
		Log.i("password", "--->" + password);
		Log.i("cadreCD", "--->" + cadreCD);
		Log.i("cadre", "--->" + cadre);
		Log.i("onlineMode", "--->" + onlineMode);
		Log.i("imageEvidence", "--->" + imageEvidence);
		Log.i("offenceImg1", "--->" + offenceImg1);
		Log.i("offenceDt", "--->" + offenceDt);
		Log.i("offenceTime", "--->" + offenceTime);
		Log.i("firmRegnNo", "--->" + firmRegnNo);
		Log.i("shopName", "--->" + shopName);
		Log.i("shopOwnerName", "--->" + shopOwnerName);
		Log.i("shopAddress ", "--->" + shopAddress);
		Log.i("location", "--->" + location);
		Log.i("psCode", "--->" + psCode);
		Log.i("psName", "--->" + psName);
		Log.i("respondantName", "--->" + respondantName);
		Log.i("respondantFatherName", "--->" + respondantFatherName);
		Log.i("respondantAddress", "--->" + respondantAddress);
		Log.i("respondantContactNo", "--->" + respondantContactNo);
		Log.i("respondantAge", "--->" + respondantAge);
		Log.i("IDCode", "--->" + IDCode);
		Log.i("IDDetails", "--->" + IDDetails);
		Log.i("witness1Name", "--->" + witness1Name);
		Log.i("witness1FatherName", "--->" + witness1FatherName);
		Log.i("witness1Address", "--->" + witness1Address);
		Log.i("witness2Name", "--->" + witness2Name);
		Log.i("witness2FatherName", "--->" + witness2FatherName);
		Log.i("witness2Address", "--->" + witness2Address);
		Log.i("detainedItems", "--->" + detainedItems);
		Log.i("simId", "--->" + simId);
		Log.i("imeiNo", "--->" + imeiNo);
		Log.i("macId", "--->" + macId);
		Log.i("gpsLatitude", "--->" + gpsLatitude);
		Log.i("gpsLongitude", "--->" + gpsLongitude);
		Log.i("totalFine", "--->" + totalFine);
		Log.i("encrHeight", "--->" + encrHeight);
		Log.i("encrWidth", "--->" + encrWidth);
		Log.i("encrLength", "--->" + encrLength);
		Log.i("shopRunBy", "--->" + shopRunBy);
		Log.i("ghmcCirNo", "--->" + ghmcCirNo);
		Log.i("ghmcCirName", "--->" + ghmcCirName);
		Log.i("basedOn", "--->" + basedOn);
		Log.i("offenceImg2", "--->" + offenceImg2);
		Log.i("aadharImg", "--->" + aadharImg);
		Log.i("seizedImg", "--->" + seizedImg);
		Log.i("sections", "--->" + sections);
		Log.i("ownerAadhar", "--->" + owner_Aadhar);
		Log.i("ownerName", "--->" + owner_Name);
		Log.i("ownerFName", "--->" + owner_FName);
		Log.i("ownerAddress", "--->" + owner_Address);
		Log.i("ownerAge", "--->" + owner_Age);
		Log.i("ownerMobile", "--->" + owner_Mobile);
		Log.i("vdeoEvdnceFlg", "--->" + vdeoEvdnce_Flg);
		Log.i("video data for time being treated as owner image data ", "--->" + vdeo_Data);
		Log.i("<--------------->", "<--------------->");

		try {
			SoapObject request = new SoapObject(NAMESPACE, "generateChallan");
			request.addProperty("unitCode", "" + unitCode);
			request.addProperty("unitName", "" + unitName);
			request.addProperty("bookedPsCode", "" + bookedPsCode);
			request.addProperty("bookedPsName", "" + bookedPsName);
			request.addProperty("pointCode", "" + pointCode);
			request.addProperty("pointName", "" + pointName);
			request.addProperty("pidCd", "" + pidCd);
			request.addProperty("pidName", "" + pidName);
			request.addProperty("password", "" + password);
			request.addProperty("cadreCD", "" + cadreCD);
			request.addProperty("cadre", "" + cadre);
			request.addProperty("onlineMode", "" + onlineMode);
			request.addProperty("imageEvidence", "" + imageEvidence);
			request.addProperty("offenceImg1", "" + offenceImg1);
			request.addProperty("offenceDt", "" + offenceDt);
			request.addProperty("offenceTime", "" + offenceTime);
			request.addProperty("firmRegnNo", "" + firmRegnNo);
			request.addProperty("shopName", "" + shopName);
			request.addProperty("shopOwnerName", "" + shopOwnerName);
			request.addProperty("shopAddress ", "" + shopAddress);
			request.addProperty("location", "" + location);
			request.addProperty("psCode", "" + psCode);
			request.addProperty("psName", "" + psName);
			request.addProperty("respondantName", "" + respondantName);
			request.addProperty("respondantFatherName", "" + respondantFatherName);
			request.addProperty("respondantAddress", "" + respondantAddress);
			request.addProperty("respondantContactNo", "" + respondantContactNo);
			request.addProperty("respondantAge", "" + respondantAge);
			request.addProperty("IDCode", "" + IDCode);
			request.addProperty("IDDetails", "" + IDDetails);
			request.addProperty("witness1Name", "" + witness1Name);
			request.addProperty("witness1FatherName", "" + witness1FatherName);
			request.addProperty("witness1Address", "" + witness1Address);
			request.addProperty("witness2Name", "" + witness2Name);
			request.addProperty("witness2FatherName", "" + witness2FatherName);
			request.addProperty("witness2Address", "" + witness2Address);
			request.addProperty("detainedItems", "" + detainedItems);
			request.addProperty("simId", "" + simId);
			request.addProperty("imeiNo", "" + imeiNo);
			request.addProperty("macId", "" + macId);
			request.addProperty("gpsLatitude", "" + gpsLatitude);
			request.addProperty("gpsLongitude", "" + gpsLongitude);
			request.addProperty("totalFine", "" + totalFine);
			request.addProperty("encrHeight", "" + encrHeight);
			request.addProperty("encrWidth", "" + encrWidth);
			request.addProperty("encrLength", "" + encrLength);
			request.addProperty("shopRunBy", "" + shopRunBy);
			request.addProperty("ghmcCirNo", "" + ghmcCirNo);
			request.addProperty("ghmcCirName", "" + ghmcCirName);
			request.addProperty("basedOn", "" + basedOn);
			request.addProperty("offenceImg2", "" + offenceImg2);
			request.addProperty("aadharImg", "" + aadharImg);
			request.addProperty("seizedImg", "" + seizedImg);
			request.addProperty("sections", "" + sections);
			// String ownerAadhar, String ownerName, String ownerFName, String
			// ownerAddress,
			// String ownerAge, String ownerMobile
			request.addProperty("ownerAadhar", "" + owner_Aadhar);
			request.addProperty("ownerName", "" + owner_Name);
			request.addProperty("ownerFName", "" + owner_FName);
			request.addProperty("ownerAddress", "" + owner_Address);
			request.addProperty("ownerAge", "" + owner_Age);
			request.addProperty("ownerMobile", "" + owner_Mobile);
			// vdeoEvdnceFlg,String vdeoData
			request.addProperty("vdeoEvdnceFlg", "" + vdeoEvdnce_Flg);
			request.addProperty("vdeoData", "" + vdeo_Data);
			Log.i("final_submit_request :::", "" + request);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("envelope", ""+ envelope);

			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			ServiceHelper.final_resp = result.toString();
			Log.i("****final_resp****:::", "" + final_resp);
			// /!final_resp.trim().equals("NA$NA")
			if (final_resp != null && "NA$NA".equals(final_resp)) {
				Log.i("****final_resp NA$NA****:::", "" + final_resp);
				// ServiceHelper.final_resp = null;
			} else if (final_resp != null && "0$0".equals(final_resp)) {
				Log.i("****final_resp 0$0****:::", "" + final_resp);
				// .final_resp = null;
			} else if (final_resp != null && "1$1".equals(final_resp)) {
				Log.i("****final_resp 1$1****:::", "" + final_resp);
				// ServiceHelper.final_resp = null;
			} else {
				final_resp_master = final_resp.split("\\$");
				ServiceHelper.print_resp = final_resp_master[0];
				ServiceHelper.final_resp_ticketNo = final_resp_master[1];
				Log.i("****ServiceHelper.print_resp ****:::", "" + ServiceHelper.print_resp);
				Log.i("****ServiceHelper.final_resp ****:::", "" + ServiceHelper.final_resp);
			}

		} catch (SoapFault fault) {
			Log.i("****final_resp ERROR****:::",
					"soapfault = " + fault.getMessage());
			final_resp = null;
			
		} catch (Exception e) {
			// TODO: handle exception
			final_resp = null;
		}
	}

	public static void sendOTPtoMobile(String mobileNo, String date) {
		try {

			SoapObject request = new SoapObject(NAMESPACE, "sendOTP");
			request.addProperty("mobileNo", mobileNo);
			request.addProperty("date", date);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("request", "" + request);
			Log.i("sendOTPtoMobile URL", "" + Login.URL);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			otp_Send_resp = result.toString();
			Log.i("**OTP TO MOBILE RESULT***", "" + otp_Send_resp);

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());
			otp_Send_resp = "NA";
		} catch (Exception E) {
			E.printStackTrace();
			otp_Send_resp = "NA";
			Log.e("Error", "" + E.toString());
		}
	}

	public static void verifyOTP(String mobileNo, String date, String otp,
			String verify_status) {
		try {

			SoapObject request = new SoapObject(NAMESPACE, "verifyOTP");
			request.addProperty("mobileNo", mobileNo);
			request.addProperty("date", date);
			request.addProperty("otp", otp);
			request.addProperty("verify_status", verify_status);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("request", "" + request);
			Log.i("sendOTPtoMobile URL", "" + Login.URL);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			otp_verify_resp = result.toString();

			if ("NA".equals(otp_verify_resp)
					|| "anyType{}".equals(otp_verify_resp)
					|| "0".equals(otp_verify_resp)) {
				otp_verify_resp = null;
			} else {
				otp_verify_resp = result.toString();
			}

			Log.i("**OTP Verification TO MOBILE RESULT***", ""
					+ otp_verify_resp);

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());
			otp_verify_resp = "NA";

		} catch (Exception E) {
			E.printStackTrace();
			otp_verify_resp = "NA";
			Log.e("Error", "" + E.toString());
		}
	}

	public static void getAadhaar(String uidNo, String eidNo) {
		try {
			SoapObject request = new SoapObject(NAMESPACE, "getAADHARData");
			request.addProperty("uid", uidNo);
			request.addProperty("eid", eidNo);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			Log.i("**MainActivity.URL ***", "" + Login.URL);

			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			aadhaar_resp = result.toString();

			if ("NA".equals(aadhaar_resp) || "anyType{}".equals(aadhaar_resp)
					|| "0".equals(aadhaar_resp)) {
				aadhaar_resp = null;
			} else {
				aadhaar_resp = result.toString();
			}

			Log.i("**getAadharDetails RESULT***", "" + aadhaar_resp);

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());

		} catch (Exception e) {
			// TODO: handle exception
			aadhaar_resp = "0";
		}
	}

	public static void getSectionMasters(String unit_code) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = new SoapObject(NAMESPACE, "getSectionsMaster");
			request.addProperty("unitCode", unit_code);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			Log.i("**sectionmasters_resp.URL ***", "" + Login.URL);

			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			sectionmasters_resp = result.toString();

			if ("NA".equals(sectionmasters_resp)
					|| "anyType{}".equals(sectionmasters_resp)
					|| "0".equals(sectionmasters_resp)) {
				sectionmasters_resp = null;
			} else {
				sectionmasters_resp = result.toString();
				if (sectionmasters_resp != null) {
					section_details_master = sectionmasters_resp.split("@");
					for (int i = 0; i < ServiceHelper.section_details_master.length; i++) {
						Log.i("**SECTIONS MASTER***", ""
								+ ServiceHelper.section_details_master[i]);

					}

				}
			}

			Log.i("**sectionmasters_resp RESULT***", "" + sectionmasters_resp);

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());

		} catch (Exception e) {
			// TODO: handle exception
			sectionmasters_resp = "0";
		}

	}

	public static void getgetSubSectionsBy_Sectioncode(String section_Cd, String officerType) {
		// TODO Auto-generated method stub
		
		try {
			SoapObject request = new SoapObject(NAMESPACE, "getSubSectionsBySectioncode");
			request.addProperty("sectionCode", section_Cd);
			request.addProperty("officerType", officerType);
			Log.i("getSubSectionsBySectionCode_request", ""+ request);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			//Log.i("URL :::", "" + Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			sub_sectionmasters_resp = result.toString();
			Log.i("sub_sectionmasters_resp", ""+ sub_sectionmasters_resp);
			/*18:407(1):50:Prohibition of the tethering of animals in public streets@
			20:413(1):5000:Removal of debris building material@
			34:402:3000:Prohibition of depositing of thing in streets*/

			if ("NA".equals(sub_sectionmasters_resp)
					|| "anyType{}".equals(sub_sectionmasters_resp)
					|| "0".equals(sub_sectionmasters_resp)) {
				sub_sectionmasters_resp = null;
			} else {
				sub_sectionmasters_resp = result.toString();
				temp_sections_master = new String[0];
				temp_sections_master = sub_sectionmasters_resp.split("@");
				for (int i = 0; i < ServiceHelper.temp_sections_master.length; i++) {
					Log.i("**temp_sections_master MASTER***", "" + ServiceHelper.temp_sections_master[i]);
				}
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			sub_sectionmasters_resp = "0";
		}
	}

	/* TO GET PENDING CHALLANS */
	// getTINRData(String tin);
	public static void getTinDetails(String tinNo) {
		try {
			// String pidCd, String password,String imei, String
			// gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "getTINRData");
			request.addProperty("tin", tinNo);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("request", "" + request);
			Log.i("URL", "" + Login.URL);
			
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			tinDetails_resp = result.toString();
			/*
			 * if ("NA".equals(tinDetails_resp) ||
			 * "anyType{}".equals(tinDetails_resp)||"0".equals(tinDetails_resp))
			 * { tinDetails_resp = null ; }else { tinDetails_resp =
			 * result.toString(); }
			 */
			Log.i("**getTinDetails***", "" + tinDetails_resp);
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			tinDetails_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			tinDetails_resp = null;
		}
	}

	public static void getReport(String offenceDate, String pidCode) {
		try {
			// String pidCd, String password,String imei,String
			// gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "getReport");
			request.addProperty("offenceDate", offenceDate);
			request.addProperty("pidCode", pidCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("request", "" + request);
			Log.i("URL", "" + Login.URL);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			report_resp = result.toString();
			Log.i("**getReport Resp ***", "" + report_resp);

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			report_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			report_resp = null;
		}
	}

	// getDuplicateEticketList(String offenceDate,String pidCode);
	public static void getDuplicatePrint(String offenceDate, String pidCode) {
		try {
			// String pidCd, String password,String imei,String
			// gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "getDuplicateEticketList");
			request.addProperty("offenceDate", offenceDate);
			request.addProperty("pidCode", pidCode);
			Log.i("request", "" + request);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("URL", "" + Login.URL);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			duplicatePrint_resp = result.toString();
			if ("NA".equals(duplicatePrint_resp)
					|| "anyType{}".equals(duplicatePrint_resp)
					|| "0".equals(duplicatePrint_resp)) {
				duplicatePrint_resp = null;
			} else {
				duplicatePrint_resp = result.toString();
			}
			Log.i("**duplicatePrint_resp***", ""+ duplicatePrint_resp);
			
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			duplicatePrint_resp = null;
			
		} catch (Exception e) {
			// TODO: handle exception
			duplicatePrint_resp = null;
		}
	}

	public static void getDupPrintBy_Eticket(String Eticket) {
		
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getDuplicatePrintByEticket");
			request.addProperty("Eticket", "" + Eticket);
			Log.i("Final Response ::::::", "" + Eticket);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			duplicatePrint_by_Eticket_resp = result.toString();
			Log.i("Final Response ::::::", "" + duplicatePrint_by_Eticket_resp);

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			duplicatePrint_by_Eticket_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			duplicatePrint_by_Eticket_resp = null;
		}
	}

	// public String getPreviousGHMCHis(String firmRegnNo, String shopName,
	// String idDetails, String gpsLatitude, String gpsLongitude);
	// public String getPreviousthirtyninebHis(String firmRegnNo, String
	// shopName, String idDetails, String gpsLatitude, String gpsLongitude);
	public static void get39Bhistry(String firmRegn_No, String shop_Name,
			String iD_No_Details, String gps_Latitude, String gps_Longitude) {
		
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getPreviousthirtyninebHis");
			request.addProperty("firmRegnNo", "" + firmRegn_No);
			request.addProperty("shopName", "" + shop_Name);
			request.addProperty("iDDetails", "" + iD_No_Details);
			request.addProperty("gpsLatitude", "" + gps_Latitude);
			request.addProperty("gpsLongitude", "" + gps_Longitude);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("get39Bhistry request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			tab39b_resp = result.toString();

			if (tab39b_resp.trim().equals("null") || tab39b_resp.trim() == null
					|| tab39b_resp.trim().equals("NA")
					|| tab39b_resp.trim().equals("anyType{}")
					|| tab39b_resp.trim().equals("0^NA^NA")) {
				tab39b_resp = null;
				Log.i("tab39b_resp Response Fail ::::::", "" + tab39b_resp);
			} else {
				tab39b_resp = result.toString();
				Log.i("tab39b_resp Response Success ::::::", "" + tab39b_resp);
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			tab39b_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			tab39b_resp = null;
		}
	}

	public static void get_ghmchistry(String firmRegn_No, String shop_Name,
			String iD_No_Details, String gps_Latitude, String gps_Longitude) {
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getPreviousGHMCHis");
			request.addProperty("firmRegnNo", "" + firmRegn_No);
			request.addProperty("shopName", "" + shop_Name);
			request.addProperty("iDDetails", "" + iD_No_Details);
			request.addProperty("gpsLatitude", "" + gps_Latitude);
			request.addProperty("gpsLongitude", "" + gps_Longitude);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghmc_hisrty_resp request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			ghmc_hisrty_resp = result.toString();

			if (ghmc_hisrty_resp.trim() == null
					|| ghmc_hisrty_resp.trim().equals("NA")
					|| ghmc_hisrty_resp.trim().equals("anyType{}")
					|| ghmc_hisrty_resp.trim().equals("0^NA^NA")) {
				ghmc_hisrty_resp = null;
			} else {
				ghmc_hisrty_resp = result.toString();
				Log.i("ghmc_hisrty_resp Response ::::::", "" + ghmc_hisrty_resp);
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			ghmc_hisrty_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			ghmc_hisrty_resp = null;
		}
	}

	public static void get39Bhistry_owner(String firmRegn_No, String shop_Name,
			String iD_No_Details, String gps_Latitude, String gps_Longitude) {
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getPreviousthirtyninebHis");
			request.addProperty("firmRegnNo", "" + firmRegn_No);
			request.addProperty("shopName", "" + shop_Name);
			request.addProperty("iDDetails", "" + iD_No_Details);
			request.addProperty("gpsLatitude", "" + gps_Latitude);
			request.addProperty("gpsLongitude", "" + gps_Longitude);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("get39Bhistry request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			tab39b_resp = result.toString();

			if (tab39b_resp.trim().equals("null") || tab39b_resp.trim() == null
					|| tab39b_resp.trim().equals("NA")
					|| tab39b_resp.trim().equals("anyType{}")
					|| tab39b_resp.trim().equals("0^NA^NA")) {
				tab39b_resp = null;
				Log.i("tab39b_resp Response Fail ::::::", "" + tab39b_resp);
			} else {
				tab39b_resp = result.toString();
				Log.i("tab39b_resp Response Success ::::::", "" + tab39b_resp);
			}

		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::", "soapfault = " + fault.getMessage());
			tab39b_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			tab39b_resp = null;
		}
	}

	public static void get_ghmchistry_owner(String firmRegn_No,
			String shop_Name, String iD_No_Details, String gps_Latitude,
			String gps_Longitude) {
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getPreviousGHMCHis");
			request.addProperty("firmRegnNo", "" + firmRegn_No);
			request.addProperty("shopName", "" + shop_Name);
			request.addProperty("iDDetails", "" + iD_No_Details);
			request.addProperty("gpsLatitude", "" + gps_Latitude);
			request.addProperty("gpsLongitude", "" + gps_Longitude);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghmc_hisrty_resp request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			ghmc_hisrty_resp = result.toString();

			if (ghmc_hisrty_resp.trim() == null
					|| ghmc_hisrty_resp.trim().equals("NA")
					|| ghmc_hisrty_resp.trim().equals("anyType{}")
					|| ghmc_hisrty_resp.trim().equals("0^NA^NA")) {
				ghmc_hisrty_resp = null;
			} else {
				ghmc_hisrty_resp = result.toString();
				Log.i("ghmc_hisrty_resp Response ::::::", "" + ghmc_hisrty_resp);
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());
			ghmc_hisrty_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			ghmc_hisrty_resp = null;
		}
	}

	public static void get39Bhistry_tin(String firmRegn_No) {
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getPreviousthirtyninebHis");
			request.addProperty("firmRegnNo", "" + firmRegn_No);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("get39Bhistry request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			tab39b_resp = result.toString();
			if (tab39b_resp.trim().equals("null") || tab39b_resp.trim() == null
					|| tab39b_resp.trim().equals("NA")
					|| tab39b_resp.trim().equals("anyType{}")
					|| tab39b_resp.trim().equals("0^NA^NA")) {
				tab39b_resp = null;
				Log.i("tab39b_resp Response Fail ::::::", "" + tab39b_resp);
			} else {
				tab39b_resp = result.toString();
				Log.i("tab39b_resp Response Success ::::::", "" + tab39b_resp);
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());
			tab39b_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			tab39b_resp = null;
		}
	}

	public static void get_ghmchistry_tin(String firmRegn_No) {
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getPreviousGHMCHis");
			request.addProperty("firmRegnNo", "" + firmRegn_No);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghmc_hisrty_resp request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			ghmc_hisrty_resp = result.toString();
			if (ghmc_hisrty_resp.trim() == null
					|| ghmc_hisrty_resp.trim().equals("NA")
					|| ghmc_hisrty_resp.trim().equals("anyType{}")
					|| ghmc_hisrty_resp.trim().equals("0^NA^NA")) {
				ghmc_hisrty_resp = null;
			} else {
				ghmc_hisrty_resp = result.toString();
				Log.i("ghmc_hisrty_resp Response ::::::", "" + ghmc_hisrty_resp);
			}
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());
			ghmc_hisrty_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			ghmc_hisrty_resp = null;
		}
	}

	// String getGHMCLocationMaster(String selectedPsCode);
	public static void get_location_masters(String selected_PsCode) {
		try {
			Log.i("****LOCATION METHOD****:::", "Entered");
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getGHMCLocationMaster");
			request.addProperty("selectedPsCode", "" + selected_PsCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("location_masters request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			location_masters_resp = result.toString();
			Log.i("location_masters_resp Response before ::::::", "" + location_masters_resp);

			if ("anyType{}".equals(location_masters_resp)
					|| "NA".equals(location_masters_resp)) {
				location_masters_resp = null;
			} else {
				location_masters_resp = result.toString();
			}
			Log.i("location_masters_resp Response after ::::::", "" + location_masters_resp);
		} catch (SoapFault fault) {
			Log.i("****SOAP FAULT ERROR****:::",
					"soapfault = " + fault.getMessage());
			location_masters_resp = null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SPOT EXCPTN", "" + e.getStackTrace());
			location_masters_resp = null;
		}
	}

	public static void getPreviousHstry(String idCode, String idValue,
			String firmName, String tinNumber) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "previousHistory");
			request.addProperty("IDCode", "" + idCode);
			request.addProperty("IdValue", "" + idValue);
			request.addProperty("ShopName", "" + firmName);
			request.addProperty("GHMCTIN", "" + tinNumber);
			Log.i("getPreviousHstry_request", ""+ request);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			
			//Log.i("ghmc_hisrty_resp request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			
			Object result = envelope.getResponse();
			prv_hisrty_resp = result.toString();
			if (prv_hisrty_resp.trim() == null
					|| prv_hisrty_resp.trim().equals("NA")
					|| prv_hisrty_resp.trim().equals("anyType{}")
					|| prv_hisrty_resp.trim().equals("0^NA^NA")) {
				prv_hisrty_resp = null;
			} else {
				prv_hisrty_resp = result.toString();
				Log.i("ghmc_prev_hisrty_resp :::", ""+ prv_hisrty_resp);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void getPreviousHstryFirm(String idCode, String idValue,
			String firmName, String tinNumber) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "previousHistory");
			request.addProperty("IDCode", "" + idCode);
			request.addProperty("IdValue", "" + idValue);
			request.addProperty("ShopName", "" + firmName);
			request.addProperty("GHMCTIN", "" + tinNumber);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghmc_hisrty_resp request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			prv_hisrty_resp_firm = result.toString();
			Log.i("ghmc_prev_hisrty_resp Response ::::::", "" + prv_hisrty_resp_firm);
			if (prv_hisrty_resp_firm.trim() == null
					|| prv_hisrty_resp_firm.trim().equals("NA")
					|| prv_hisrty_resp_firm.trim().equals("anyType{}")
					|| prv_hisrty_resp_firm.trim().equals("0^NA^NA")) {
				prv_hisrty_resp_firm = null;
			} else {
				prv_hisrty_resp_firm = result.toString();
				// Log.i("ghmc_prev_hisrty_resp Response ::::::",
				// ""+prv_hisrty_resp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// previousHistory(String idCode,String idValue,String firmName, String
	// tinNumber)
	public static void getPreviousHstryTin(String idCode, String idValue,
			String firmName, String tinNumber) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "previousHistory");
			request.addProperty("IDCode", "" + idCode);
			request.addProperty("IdValue", "" + idValue);
			request.addProperty("ShopName", "" + firmName);
			request.addProperty("GHMCTIN", "" + tinNumber);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghmc_hisrty_resp request ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			prv_hisrty_resp_tin = result.toString();
			if (prv_hisrty_resp_tin.trim() == null
					|| prv_hisrty_resp_tin.trim().equals("NA")
					|| prv_hisrty_resp_tin.trim().equals("anyType{}")
					|| prv_hisrty_resp_tin.trim().equals("0^NA^NA")) {
				prv_hisrty_resp_tin = null;
			} else {
				prv_hisrty_resp_tin = result.toString();
				Log.i("ghmc_prev_hisrty_resp Response ::::::", "" + prv_hisrty_resp_tin);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void getDuplicatePrintByEticket(String eticketNo) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = null;
			request = new SoapObject(NAMESPACE, "getDuplicatePrintByEticket");
			request.addProperty("Ticket", "" + eticketNo);
			Log.i("getDuplicatePrint_request", ""+ request);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			
			// Log.i("ghmc_hisrty_duplicate_print ::::::", "" + request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			print_resp = result.toString();
			if (print_resp.trim() == null || print_resp.trim().equals("NA")
					|| print_resp.trim().equals("anyType{}")
					|| print_resp.trim().equals("0^NA^NA")) {
				print_resp = null;
			} else {
				print_resp = result.toString();
				Log.i("print_resp :::", "" + print_resp);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}