// collect the data, and call the functions for the project
package com.LMFM.musicmood;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.R.array;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import android.view.Menu;


import com.neurosky.thinkgear.*;
import com.LMFM.musicmood.R;

import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
//import android.bluethooth.BluetoothAdapter;
//import android.bluethooth.BluetoothDevice;

//import com.google.appengine.api.files.AppEngineFile;
//import com.google.appengine.api.files.FileReadChannel;
//import com.google.appengine.api.files.FileService;
//import com.google.appengine.api.files.FileServiceFactory;
//import com.google.appengine.api.files.FileWriteChannel;
//import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
public class MainActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;

	BluetoothAdapter bluetoothAdapter;
	TextView tv;
	TextView rawData;
	TextView AttentionMes;
	TextView MediationMes;
	Button b;
	EditText fileName;
	String rawDATA = "";
	array Meddata;
	array Attdata;

	TGDevice tgDevice;
	final boolean rawEnabled = false;
	Boolean playing = false;
	
	int[] AttArray = {0};
	int[] MedArray = {0};
	
	//ArrayList<String> AttList;
	//ArrayList<String> MedList;
	
	ArrayList<String> AttList= new ArrayList();
	ArrayList<String> MedList= new ArrayList();

	//List list=new ArrayList();
	
	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinterface);
        tv = (TextView)findViewById(R.id.textView1);
        tv.setText("");
        tv.append("Android version: " + Integer.valueOf(android.os.Build.VERSION.SDK) + "\n" );
        
        fileName = (EditText)findViewById(R.id.editText1);
       // fileName.setText("Please type in the name of the File");
        //fileName.getText();
       // rawData = (TextView)findViewById(R.id.textView8);
       // rawData.setText("");
        
        
        
        AttentionMes = (TextView)findViewById(R.id.textView3);
        AttentionMes.setText("");
        
        MediationMes = (TextView)findViewById(R.id.textView5);
        MediationMes.setText("");   
        
        
        // create an object
        //FileService fileService = FileServiceFactory.getFileService();

        /**
         * Set up properties of your new object
         * After finalizing objects, they are accessible
         * through Cloud Storage with the URL:
         * http://storage.googleapis.com/my_bucket/my_object
         */
        /*GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
        .setBucket("pipelinetest")
        .setKey("test_object")
        .setAcl("public-read")
        .setMimeType("text/html");
        //.setUserMetadata("date-created", "092011", "owner", "Jon");
        
        try {
			AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
        // Play the music
        final MediaPlayer mediaPlayer1 = MediaPlayer.create(MainActivity.this, R.raw.seagull);
        
        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setEnabled(false);
        
        button1.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		//MediaPlayer mediaPlayer = MediaPlayer.create(HelloEEGActivity.this, R.raw.seagull);
        		button1.setEnabled(false);
        		mediaPlayer1.start();
    			long yourmilliseconds = System.currentTimeMillis();
    	        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

    	        Date resultdate = new Date(yourmilliseconds);
    	        System.out.println(sdf.format(resultdate));

        		playing = true;
        		
        		
        	}
        });
        
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		playing = false;
        		mediaPlayer1.pause();
        		mediaPlayer1.seekTo(0);
        		
        		button1.setEnabled(true);

        		//int size=list.size(); 
            	//String[] Rawarray=new String[size]; 
            	//for(int i=0;i<list.size();i++){ 
            	//Rawarray[i]=(String)list.get(i); 
           //} 
            	
        		//save(AttArray, MedArray,fileName.getText().toString());
        		saveString(AttList, MedList, fileName.getText().toString());

        		//save(AttentionMes.getText().toString(), MediationMes.getText().toString(),fileName.getText().toString(),rawDATA.toString());
        		fileName.setText("");

        		
        	}
        });
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
        
        final Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				button3.setEnabled(false);
				if(bluetoothAdapter == null) {
		        	// Alert user that Bluetooth is not available
		        	//Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
		        	finish();
		        	return;
		        }else {
		        	/*if (bluetoothAdapter.isEnabled()) {
		        	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		        	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		        	}*/
		        	/* create the TGDevice */
		        	/*ArrayAdapter mArrayAdapter = null;
		        	Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		        	if (pairedDevices.size()>0){
		        		for (BluetoothDevice device : pairedDevices){
		        			mArrayAdapter.add(device.getName() +"\n"+device.getAddress());
		        		}
		        	}*/
		        	tgDevice = new TGDevice(bluetoothAdapter, handler);
		        	/*if (tgDevice == null){
		        		tv.append("there ");
		        	}*/
		        	tgDevice.connect(true);
		        	tgDevice.start();
		        	
		   
		        }  
			}
		});

        //String bluetoothname = bluetoothAdapter.getName();
        
        final Button button4 = (Button) findViewById(R.id.button4);

        
        button4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
	    		button4.setEnabled(false);
	            ConnectivityManager networkManager =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    		NetworkInfo NetworkInfo = networkManager.getActiveNetworkInfo(); 
	    		  // Check the network connection, if it connectted
	    		  if (NetworkInfo != null && NetworkInfo.isAvailable()){ 
	    			  	
	                	//text.setText("hey your online!!!");
	                	System.out.println("your are online!!!");
	                	try {
							predictiontest predicttest = new predictiontest();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                	System.out.println("hellohello!!!!!!!!!!!!!!!");
	                	
	                	
	                	// link to google account
	                	//uploadFile(fileName.getText().toString());
	                	//Toast toast;
	                	//toast.setView(warn);
	                    //Toast.makeText(this,"Connected",Toast.LENGTH_LONG).show();
	                	//Upload file when we are connected   
	                	//button4.setEnabled(true);

	                	            }
	                else{// otherwise, inform user to connect to the Internet or open the WI-FI
	                	System.out.println("Please connect to the internet!!!");
	                	//button4.setEnabled(false);
	                	//showToast(MainActivity);
	                	
	                } 


			}
        });
        
        
    }
    
    @Override
    public void onDestroy() {
    	
    	tgDevice.close();
        super.onDestroy();
        
    }
    /**
     * Handles messages from TGDevice
     */
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	int rawdata;
        	Button button1 = (Button)findViewById(R.id.button1);
        	Button button3 = (Button)findViewById(R.id.button3);
			switch (msg.what) {
            case TGDevice.MSG_STATE_CHANGE:
            	tv.append("get message");
                switch (msg.arg1) {
	                case TGDevice.STATE_IDLE:
	                	//button1.setEnabled(false);
	                    break;
	                case TGDevice.STATE_CONNECTING:		                	
	                	tv.append("Connecting...\n");
	                	//button1.setEnabled(false);
	                	break;		                    
	                case TGDevice.STATE_CONNECTED:
	                	tv.append("Connected.\n");
	                	//button1.setEnabled(true);
	                	button3.setEnabled(false);
	                	tgDevice.start();
	                    break;
	                case TGDevice.STATE_NOT_FOUND:
	                	tv.append("Can't find\n");
	                	//button1.setEnabled(false);
	                	button3.setEnabled(true);
	                	break;
	                case TGDevice.STATE_NOT_PAIRED:
	                	tv.append("not paired\n");
	                	//button1.setEnabled(false);
	                	break;
	                case TGDevice.STATE_DISCONNECTED:
	                	tv.append("Disconnected mang\n");
	                	//button1.setEnabled(false);
	                	button3.setEnabled(true);
	                    System.out.println(getFilesDir());
                }

                break;
            case TGDevice.MSG_POOR_SIGNAL:
            		//signal = msg.arg1;
            		//tv.append("PoorSignal: " + msg.arg1 + "\n");
                break;
                
            case TGDevice.MSG_RAW_DATA:	  
            	rawdata = msg.arg1;
 
            	//rawDATA = rawDATA.concat(msg.arg1+" ");

            	//if (playing == true){
            		//rawData.append(msg.arg1+"");
            	//}
            	break;
            case TGDevice.MSG_HEART_RATE:
        		//tv.append("Heart rate: " + msg.arg1 + "\n");
                break;
            case TGDevice.MSG_ATTENTION:
            		int att = msg.arg1;
            		String attString = Integer.toString(att);
            		if (att >0){
            			button1.setEnabled(true);
            			if (playing == true){
            			//button1.setEnabled(true);
            			//Arrays.fill(AttArray, att);
            			AttList.add(attString);
               			//Method for Array
            			//int lengthArray = AttArray.length;
            			//int indexAtt = lengthArray;
            			//AttArray[indexAtt] = att;
            			
            			
            			AttentionMes.append(att+ ",");
            			long yourmilliseconds = System.currentTimeMillis();
            	        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

            	        Date resultdate = new Date(yourmilliseconds);
            	        System.out.println(sdf.format(resultdate));

            		    }
            			
            		}
            		else{
            			button1.setEnabled(false);
            			
            		}
            		//Log.v("HelloA", "Attention: " + att + "\n");
            	break;
            case TGDevice.MSG_MEDITATION:
            	int med = msg.arg1;
            	String medString = Integer.toString(med);
            	//record the data, while the music is playing
            	if (med >0){
            		button1.setEnabled(true);
            		if(playing ==true){
            		//Arrays.fill(MedArray, Med);
            		
            		MedList.add(medString);
        			//int lengthArray = MedArray.length;
        			//AttArray[lengthArray + 1] = Med;
            		MediationMes.append(med+ ",");
        			long yourmilliseconds = System.currentTimeMillis();
        	        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

        	        Date resultdate = new Date(yourmilliseconds);
        	        System.out.println(sdf.format(resultdate));

            	}
            	}
            	else{
            		button1.setEnabled(false);
            	}
            	break;
            case TGDevice.MSG_BLINK:
            		//tv.append("Blink: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_RAW_COUNT:
            		//tv.append("Raw Count: " + msg.arg1 + "\n");
            	break;
            case TGDevice.MSG_LOW_BATTERY:
            	Toast.makeText(getApplicationContext(), "Low battery!", Toast.LENGTH_SHORT).show();
            	break;
            case TGDevice.MSG_RAW_MULTI:
            	//TGRawMulti rawM = (TGRawMulti)msg.obj;
            	//tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
            default:
            	break;
        }
        }
    };
    
    public void doStuff(View view) {
    	if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
    		tgDevice.connect(rawEnabled);   
    	//tgDevice.ena
    }
    
    public void saveinRow(ArrayList<String> AttList, ArrayList<String> MedList, String fileName)
    {
    	try{
			String changeRow = "\n";
			String comma = ",";
			
			String[] mStringAtt = new String[AttList.size()];
			mStringAtt = AttList.toArray(mStringAtt);
			String[] mStringMed = new String[MedList.size()];
			mStringMed = MedList.toArray(mStringMed);
			
			FileOutputStream outStream=openFileOutput(fileName+".txt",Activity.MODE_WORLD_WRITEABLE+Activity.MODE_WORLD_READABLE);
			
			for (int i = 0; i<30; i++)
			{
				for (int j = 0; j<15; j++)
				{
			//for (int i = 0; i<mStringAtt.length; i++){
				String attData = mStringAtt[j];
				
				String medData = mStringMed[j];
				
				outStream.write(attData.getBytes());
				
				outStream.write(comma.getBytes());
				
				outStream.write(medData.getBytes());
				
				outStream.write(changeRow.getBytes());
				

				Log.d("the Attention", (String)mStringAtt[i]);

				Log.d("the Mediation", (String)mStringMed[i]);
				
			}
			
			}
    	}
    	catch (FileNotFoundException e) {
            
    		return;
        }
    
    	catch (IOException e){
        
    		return ;
    
    	}
    }
    
    public void saveString(ArrayList<String> AttList, ArrayList<String> MedList, String fileName)
    {
    	try{
    		
			String changeRow = "\n";
			String comma = ",";
			
			String[] mStringAtt = new String[AttList.size()];
			mStringAtt = AttList.toArray(mStringAtt);
			String[] mStringMed = new String[MedList.size()];
			mStringMed = MedList.toArray(mStringMed);
			
			//Change the ArrayList to array
			//Object[] attArray = AttList.toArray();
			//Object[] medArray = MedList.toArray();
			
			//convert the object to string
			//for (int i=0; i< attArray.length; i++){
			//	Log.d("the Attention", (String)attArray[i]);
				
			//}
			//for (int i=0; i< medArray.length; i++){
			//	Log.d("the mediation", (String)medArray[i]);
				
			//}

			//String attString = attArray.toString();
			//String medString = medArray.toString();
			
			//String[] attArrayString=Arrays.copyOf(attArray, attArray.length, String[].class);
			FileOutputStream outStream=openFileOutput(fileName+".txt",Activity.MODE_WORLD_WRITEABLE+Activity.MODE_WORLD_READABLE);
			
			
			for (int i = 0; i<mStringAtt.length; i++){
			//for (int i = 0; i<mStringAtt.length; i++){
				String attData = mStringAtt[i];
				String medData = mStringMed[i];
				
				outStream.write(attData.getBytes());
				
				outStream.write(comma.getBytes());
				
				outStream.write(medData.getBytes());
				
				outStream.write(changeRow.getBytes());
				

				Log.d("the Attention", (String)mStringAtt[i]);

				Log.d("the Mediation", (String)mStringMed[i]);
				outStream.close();
			}
    	}
    	catch (FileNotFoundException e) {
    		return;
    		}
    	catch (IOException e){
        
    		return ;
    
    	}
    }
    
    // Save the data in text file
    // public void save(String A, String B, String x, String r)
    public void save(int[] A, int[]B, String x)
    {
        try {
            
			FileOutputStream outStream=openFileOutput(x+".txt",Activity.MODE_WORLD_WRITEABLE+Activity.MODE_WORLD_READABLE);
            
			//save the data in one stream and the other stream
			//String AttString = Arrays.toString(A);
			//outStream.write(AttString.getBytes());
			//int lenArray = A.length;
			//String changeRow = "\n";
			//String comma = ",";
			//int[][] CollectedData = {};
			//for (int i = 0; i < lenArray;i++){

			//		CollectedData [i][0] = A[i];
			//		CollectedData [i][1] = B[i];
			//		String AttString = A[i].toString();
			//		String MedString = Arrays.toString(B[i]);
			//		outStream.write();

			//}
			
			String AttString = Arrays.toString(A);
			String MedString = Arrays.toString(B);
			outStream.write(AttString.getBytes());
			//outStream.write(changeRow.getBytes());
            outStream.write(MedString.getBytes());
            
          //  outStream.write(r.getBytes());
           // int size=length(rawdata);  
          //  for(int i=0;i<list.size();i++){ 
           //     outStream.write(rawarray[i].getBytes());
           // 	} 

            
            //outStream.write(B.toString().getBytes());
            outStream.close();
            Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            return;
        }
        catch (IOException e){
            return ;
        }
    }
    


    
    // upload the file to google account
    public void uploadFile(String x){
    	//x is the file name, typed in the textEdit
        String end = "/r/n";
        String Hyphens = "--";
        String boundary = "*****";
        HttpServletRequest req = null; 
        HttpServletResponse resp = null;
        
        
        
        try
        {
        	//GuestbookServlet eegServlet = new GuestbookServlet();
        	//eegServlet.doGet(req,resp);
        }catch(Exception e){
            System.out.println("failed to upload" + e.getMessage());
            Toast.makeText(MainActivity.this, "failed to upload" + e.getMessage(),
                Toast.LENGTH_LONG).show();
        }
    	
    
    }
    
    // read the file in google account
    public void readGoogle(){
    	
    }
    
    // Check wether the network is conencted.
    public static boolean isNetworkAvaiable(Activity mActivity){
    	Context context = mActivity.getApplicationContext();
  	  ConnectivityManager networkManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo NetworkInfo = networkManager.getActiveNetworkInfo(); 
	  // Check the network connection, if it connectted
	  if (NetworkInfo != null && NetworkInfo.isAvailable()){ 
		  	
        	//text.setText("hey your online!!!");
        	System.out.println("your are online!!!");
            //Toast.makeText(this,"Connected",Toast.LENGTH_LONG).show();
        	//Upload file when we are connected   
        	return true;

        	            }
        else{// otherwise, inform user to connect to the Internet or open the WI-FI
        	System.out.println("Please connect to the internet!!!");
        	return false;
        }
    }
    
    
    

}