package com.LMFM.musicmood;



import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.prediction.*;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.api.services.prediction.model.Output;
import com.google.api.services.prediction.model.Training;
 
public class predictiontest {
 
    private static final String DATA_STORAGE_LOCATION = "pipelinetest/language_id.txt";
    private static final String MODEL_ID = "language";
    /**
     * @param args
     */
    
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    
    public predictiontest() throws Exception, IOException {

			GoogleCredential credential = new GoogleCredential.Builder()
			.setTransport(HTTP_TRANSPORT)
			.setJsonFactory(JSON_FACTORY)

			.setServiceAccountId("84801611687-bcrp7p0pk0pu0lptknr1tjmf4j3340k2@developer.gserviceaccount.com")

			.setServiceAccountScopes(PredictionScopes.PREDICTION)

			.setServiceAccountPrivateKeyFromP12File(new File("/Users/dizhan/Documents/Important Documents/145a32577e8a09313798b81d100637a92f15e53c-privatekey.p12"))
			.build();
			
	        Prediction prediction = new Prediction.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName("PredictionTest")
            .build();
 
        System.out.println("Authorization completed.");
 

        Training training = new Training();
        training.setId(MODEL_ID);
        training.setStorageDataLocation(DATA_STORAGE_LOCATION);
        
   
 
        prediction.trainedmodels().insert(training).execute();
        
        System.out.println("Training!!!");
 

        int count = -1;
        while (++count < 100) {
            HttpResponse response = prediction.trainedmodels().get(MODEL_ID)
                    .executeUnparsed();
            if (response.getStatusCode() == 200) {
                training = response.parseAs(Training.class);
                if (training.getTrainingStatus().equals("DONE")) {
                    System.out.println("\nTraining completed.");
                    break;
                }
            }
            response.ignore();
 
            Thread.sleep(5000);
            System.out.print(".");
        }
    }

}

