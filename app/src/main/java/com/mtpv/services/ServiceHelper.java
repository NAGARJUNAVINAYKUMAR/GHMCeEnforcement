package com.mtpv.services;

import android.util.Log;

import com.mtpv.ghmcepettycase.Login;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

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
    public static String final_resp_ticketNo, otpStatusnTime;

    public static String[] PointNamesBypsNames_master;

    public static String[] location_master;

    public static String[] temp_sections_master;

    public static String[] section_details_master;

    /* VILATION DETAILS */
    public static String[] sub_section_details_master;
    public static String[][] sub_section_detailed_views;
    static StringBuffer sbuffer_allsub_section;


    public static String getOtpStatusNTime(String unitcode) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getOtpStatusNTime");

            request.addProperty("unitcCode", unitcode);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);


            HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            try {
                if (result != null) {
                    otpStatusnTime = result.toString().trim();
                } else {
                    otpStatusnTime = "NA|NA";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                otpStatusnTime = "NA|NA";
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            otpStatusnTime = "NA|NA";

        } catch (Exception E) {
            E.printStackTrace();
            otpStatusnTime = "NA|NA";
        }

        return otpStatusnTime;
    }


    public static void getLogin(String pid_Cd, String pass_word,
                                String imei_no, String gps_Lattitude, String gps_Longitude, String appVersion) {
        // TODO Auto-generated method stub
        try {
            SoapObject request = new SoapObject(NAMESPACE, "authenticateUser");
            request.addProperty("pidCd", pid_Cd);
            request.addProperty("password", pass_word);
            request.addProperty("imei", imei_no);
            request.addProperty("gpsLattitude", gps_Lattitude);
            request.addProperty("gpsLongitude", gps_Longitude);
            request.addProperty("appVersion", appVersion);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                login_resp = result.toString();
                if ("NA".equals(login_resp) || "anyType{}".equals(login_resp)
                        || "0".equals(login_resp)) {
                    login_resp = null;
                } else {
                    login_resp = result.toString();
                }
            } else {
                login_resp = null;
            }
        } catch (SoapFault fault) {
            login_resp = null;
            fault.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            login_resp = null;
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
            HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                psmater_resp = result.toString();
                if ("NA".equals(psmater_resp) || "anyType{}".equals(psmater_resp)
                        || "0".equals(psmater_resp)) {
                    psmater_resp = null;
                } else {
                    psmater_resp = result.toString();
                }
                psNames_master = new String[0];
                psNames_master = psmater_resp.split("@");

            } else {
                psmater_resp = null;
                psNames_master = new String[0];
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            psmater_resp = null;
            psNames_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            psmater_resp = null;
            psNames_master = new String[0];
        }
    }

    /* TO GET POINTNAME BY PS_NAME FOR SETTINGS */
    public static void getPointNameByPsNames(String ps_code) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getPointDetailsByPscode");
            request.addProperty("psCode", "" + ps_code);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                point_resp = result.toString();

                if ("NA".equals(point_resp) || "anyType{}".equals(point_resp)
                        || point_resp == null) {
                    point_resp = null;
                } else {
                    PointNamesBypsNames_master = new String[0];
                    PointNamesBypsNames_master = point_resp.split("@");

                }
            } else {
                point_resp = null;
                PointNamesBypsNames_master = new String[0];
            }
        } catch (SoapFault fault) {
            point_resp = null;
            PointNamesBypsNames_master = new String[0];
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            point_resp = null;
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
            request.addProperty("ownerAadhar", "" + owner_Aadhar);
            request.addProperty("ownerName", "" + owner_Name);
            request.addProperty("ownerFName", "" + owner_FName);
            request.addProperty("ownerAddress", "" + owner_Address);
            request.addProperty("ownerAge", "" + owner_Age);
            request.addProperty("ownerMobile", "" + owner_Mobile);
            request.addProperty("vdeoEvdnceFlg", "" + vdeoEvdnce_Flg);
            request.addProperty("vdeoData", "" + vdeo_Data);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {

                ServiceHelper.final_resp = result.toString();

                // /!final_resp.trim().equals("NA$NA")
                if (final_resp != null && "NA$NA".equals(final_resp)) {
                    // ServiceHelper.final_resp = null;
                } else if (final_resp != null && "0$0".equals(final_resp)) {
                    // .final_resp = null;
                } else if (final_resp != null && "1$1".equals(final_resp)) {
                    // ServiceHelper.final_resp = null;
                } else {
                    final_resp_master = new String[0];

                    final_resp_master = final_resp.split("\\$");
                    ServiceHelper.print_resp = final_resp_master[0];
                    ServiceHelper.final_resp_ticketNo = final_resp_master[1];
                }
            } else {
                final_resp = null;
                final_resp_master = new String[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
            final_resp = null;
            final_resp_master = new String[0];
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

            if (result != null) {
                otp_Send_resp = result.toString();
            } else {
                otp_Send_resp = "NA";
            }

        } catch (SoapFault fault) {

            otp_Send_resp = "NA";
        } catch (Exception E) {
            E.printStackTrace();
            otp_Send_resp = "NA";
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
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    Login.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                otp_verify_resp = new com.mtpv.services.PidSecEncrypt().decrypt(result.toString());

                if ("NA".equals(otp_verify_resp)
                        || "anyType{}".equals(otp_verify_resp)
                        || "0".equals(otp_verify_resp)) {
                    otp_verify_resp = "NA";
                } else {
                    otp_verify_resp = new com.mtpv.services.PidSecEncrypt().decrypt(result.toString());
                }

            } else {
                otp_verify_resp = "NA";
            }


        } catch (SoapFault fault) {

            otp_verify_resp = "NA";

        } catch (Exception E) {
            otp_verify_resp = "NA";
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

            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                aadhaar_resp = result.toString();

                if ("NA".equals(aadhaar_resp) || "anyType{}".equals(aadhaar_resp)
                        || "0".equals(aadhaar_resp)) {
                    aadhaar_resp = null;
                } else {
                    aadhaar_resp = result.toString();
                }
            } else {
                aadhaar_resp = "0";
            }


        } catch (SoapFault fault) {

            fault.printStackTrace();
            aadhaar_resp = "0";
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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

            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                sectionmasters_resp = result.toString();

                if ("NA".equals(sectionmasters_resp)
                        || "anyType{}".equals(sectionmasters_resp)
                        || "0".equals(sectionmasters_resp)) {
                    sectionmasters_resp = null;
                } else {
                    sectionmasters_resp = result.toString();
                    if (sectionmasters_resp != null) {
                        section_details_master = new String[0];
                        section_details_master = sectionmasters_resp.split("@");
                    }
                }

            } else {
                sectionmasters_resp = "0";
                section_details_master = new String[0];
            }

        } catch (SoapFault fault) {
            sectionmasters_resp = "0";
            section_details_master = new String[0];
            fault.printStackTrace();

        } catch (Exception e) {
            // TODO: handle exception
            sectionmasters_resp = "0";
            section_details_master = new String[0];
            e.printStackTrace();
        }

    }

    public static void getgetSubSectionsBy_Sectioncode(String section_Cd, String officerType) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, "getSubSectionsBySectioncode");
            request.addProperty("sectionCode", section_Cd);
            request.addProperty("officerType", officerType);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                sub_sectionmasters_resp = result.toString();


                if ("NA".equals(sub_sectionmasters_resp)
                        || "anyType{}".equals(sub_sectionmasters_resp)
                        || "0".equals(sub_sectionmasters_resp)) {
                    sub_sectionmasters_resp = null;
                } else {
                    sub_sectionmasters_resp = result.toString();
                    temp_sections_master = new String[0];
                    temp_sections_master = sub_sectionmasters_resp.split("@");

                }
            } else {
                temp_sections_master = new String[0];
                sub_sectionmasters_resp = "0";
            }
        } catch (SoapFault fault) {
            temp_sections_master = new String[0];
            sub_sectionmasters_resp = "0";
            fault.printStackTrace();
        } catch (Exception e) {
            sub_sectionmasters_resp = "0";
            temp_sections_master = new String[0];
            e.printStackTrace();
        }
    }

    public static void getTinDetails(String tinNo) {
        try {

            SoapObject request = new SoapObject(NAMESPACE, "getTINRData");
            request.addProperty("tin", tinNo);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);


            HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                tinDetails_resp = result.toString();
            } else {
                tinDetails_resp = null;
            }

        } catch (SoapFault fault) {
            tinDetails_resp = null;
            fault.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            tinDetails_resp = null;
        }
    }

    public static void getReport(String offenceDate, String pidCode) {
        try {

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

            if (result != null) {
                report_resp = result.toString();
            } else {
                report_resp = null;
            }

        } catch (SoapFault fault) {
            fault.printStackTrace();
            report_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            report_resp = null;
        }
    }

    public static void getDuplicatePrint(String offenceDate, String pidCode) {
        try {

            SoapObject request = new SoapObject(NAMESPACE, "getDuplicateEticketList");
            request.addProperty("offenceDate", offenceDate);
            request.addProperty("pidCode", pidCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(Login.URL);
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                duplicatePrint_resp = result.toString();
                if ("NA".equals(duplicatePrint_resp)
                        || "anyType{}".equals(duplicatePrint_resp)
                        || "0".equals(duplicatePrint_resp)) {
                    duplicatePrint_resp = "NA";
                } else {
                    duplicatePrint_resp = result.toString();
                }
            } else {
                duplicatePrint_resp = "NA";
            }

        } catch (SoapFault fault) {
            duplicatePrint_resp = "NA";
            fault.printStackTrace();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            duplicatePrint_resp = "NA";
        }
    }

    public static void getDupPrintBy_Eticket(String Eticket) {

        try {
            SoapObject request = null;
            request = new SoapObject(NAMESPACE, "getDuplicatePrintByEticket");
            request.addProperty("Eticket", "" + Eticket);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                duplicatePrint_by_Eticket_resp = result.toString();
            } else {
                duplicatePrint_by_Eticket_resp = null;
            }

        } catch (SoapFault fault) {
            duplicatePrint_by_Eticket_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            duplicatePrint_by_Eticket_resp = null;
        }
    }


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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                tab39b_resp = result.toString();

                if (tab39b_resp.trim().equals("null") || tab39b_resp.trim() == null
                        || tab39b_resp.trim().equals("NA")
                        || tab39b_resp.trim().equals("anyType{}")
                        || tab39b_resp.trim().equals("0^NA^NA")) {
                    tab39b_resp = null;
                } else {
                    tab39b_resp = result.toString();
                }
            } else {
                tab39b_resp = null;
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            tab39b_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                ghmc_hisrty_resp = result.toString();

                if (ghmc_hisrty_resp.trim() == null
                        || ghmc_hisrty_resp.trim().equals("NA")
                        || ghmc_hisrty_resp.trim().equals("anyType{}")
                        || ghmc_hisrty_resp.trim().equals("0^NA^NA")) {
                    ghmc_hisrty_resp = null;
                } else {
                    ghmc_hisrty_resp = result.toString();
                }
            } else {
                ghmc_hisrty_resp = null;
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            ghmc_hisrty_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            ghmc_hisrty_resp = null;
            e.printStackTrace();
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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                tab39b_resp = result.toString();

                if (tab39b_resp.trim().equals("null") || tab39b_resp.trim() == null
                        || tab39b_resp.trim().equals("NA")
                        || tab39b_resp.trim().equals("anyType{}")
                        || tab39b_resp.trim().equals("0^NA^NA")) {
                    tab39b_resp = null;
                } else {
                    tab39b_resp = result.toString();
                }
            } else {
                tab39b_resp = null;
            }

        } catch (SoapFault fault) {
            tab39b_resp = null;
            fault.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            tab39b_resp = null;
            e.printStackTrace();
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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {

                ghmc_hisrty_resp = result.toString();

                if (ghmc_hisrty_resp.trim() == null
                        || ghmc_hisrty_resp.trim().equals("NA")
                        || ghmc_hisrty_resp.trim().equals("anyType{}")
                        || ghmc_hisrty_resp.trim().equals("0^NA^NA")) {
                    ghmc_hisrty_resp = null;
                } else {
                    ghmc_hisrty_resp = result.toString();
                }
            } else {
                ghmc_hisrty_resp = null;
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            ghmc_hisrty_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (result != null) {
                tab39b_resp = result.toString();

                if (tab39b_resp.trim().equals("null") || tab39b_resp.trim() == null
                        || tab39b_resp.trim().equals("NA")
                        || tab39b_resp.trim().equals("anyType{}")
                        || tab39b_resp.trim().equals("0^NA^NA")) {
                    tab39b_resp = null;
                } else {
                    tab39b_resp = result.toString();
                }
            } else {
                tab39b_resp = null;
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            tab39b_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                ghmc_hisrty_resp = result.toString();
                if (ghmc_hisrty_resp.trim() == null
                        || ghmc_hisrty_resp.trim().equals("NA")
                        || ghmc_hisrty_resp.trim().equals("anyType{}")
                        || ghmc_hisrty_resp.trim().equals("0^NA^NA")) {
                    ghmc_hisrty_resp = null;
                } else {
                    ghmc_hisrty_resp = result.toString();
                }
            } else {
                ghmc_hisrty_resp = null;
            }
        } catch (SoapFault fault) {
            fault.printStackTrace();
            ghmc_hisrty_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            ghmc_hisrty_resp = null;
        }
    }

    // String getGHMCLocationMaster(String selectedPsCode);
    public static void get_location_masters(String selected_PsCode) {
        try {
            SoapObject request = null;
            request = new SoapObject(NAMESPACE, "getGHMCLocationMaster");
            request.addProperty("selectedPsCode", "" + selected_PsCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                location_masters_resp = result.toString();

                if ("anyType{}".equals(location_masters_resp)
                        || "NA".equals(location_masters_resp)) {
                    location_masters_resp = null;
                } else {
                    location_masters_resp = result.toString();
                }
            } else {
                location_masters_resp = null;
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            location_masters_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
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

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            //Log.i("ghmc_hisrty_resp request ::::::", "" + request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);

            Object result = envelope.getResponse();
            if (result != null) {
                prv_hisrty_resp = result.toString();
                if (prv_hisrty_resp.trim() == null
                        || prv_hisrty_resp.trim().equals("NA")
                        || prv_hisrty_resp.trim().equals("anyType{}")
                        || prv_hisrty_resp.trim().equals("0^NA^NA")) {
                    prv_hisrty_resp = null;
                } else {
                    prv_hisrty_resp = result.toString();
                }
            } else {
                prv_hisrty_resp = null;
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            prv_hisrty_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            prv_hisrty_resp = null;
            e.printStackTrace();
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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                prv_hisrty_resp_firm = result.toString();
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
            } else {
                prv_hisrty_resp_firm = null;
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            prv_hisrty_resp_firm = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            prv_hisrty_resp_firm = null;
        }
    }


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
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                prv_hisrty_resp_tin = result.toString();
                if (prv_hisrty_resp_tin.trim() == null
                        || prv_hisrty_resp_tin.trim().equals("NA")
                        || prv_hisrty_resp_tin.trim().equals("anyType{}")
                        || prv_hisrty_resp_tin.trim().equals("0^NA^NA")) {
                    prv_hisrty_resp_tin = null;
                } else {
                    prv_hisrty_resp_tin = result.toString();
                }
            } else {
                prv_hisrty_resp_tin = null;
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            prv_hisrty_resp_tin = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            prv_hisrty_resp_tin = null;
        }
    }

    public static void getDuplicatePrintByEticket(String eticketNo) {
        // TODO Auto-generated method stub
        try {
            SoapObject request = null;
            request = new SoapObject(NAMESPACE, "getDuplicatePrintByEticket");
            request.addProperty("Ticket", "" + eticketNo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Log.i("ghmc_hisrty_duplicate_print ::::::", "" + request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(Login.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (result != null) {
                print_resp = result.toString();

                if (print_resp.trim() == null || print_resp.trim().equals("NA")
                        || print_resp.trim().equals("anyType{}")
                        || print_resp.trim().equals("0^NA^NA")) {
                    print_resp = null;
                } else {
                    print_resp = result.toString();
                }
            } else {
                print_resp = null;
            }
        } catch (SoapFault fault) {

            fault.printStackTrace();
            print_resp = null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            print_resp = null;
        }
    }
}