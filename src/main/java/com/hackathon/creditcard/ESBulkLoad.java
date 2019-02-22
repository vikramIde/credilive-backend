package com.hackathon.creditcard;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

/*********************************************************************************************
 * This is where we are loading the csv with credit card info into the elastic search cluster												  *
 * 																					  	
 ********************************************************************************************/
@Component
public class ESBulkLoad {

	final String CREDIT_CARD_FILE_NAME= "C:\\TestClient\\BermudaDIY-master-783d11d84feae4510ad60158c37afdbb67f49265\\credit_card57066eaProd.csv";
    String indexName,indexTypeName;
    TransportClient client = null;
    static Logger log = Logger.getLogger(ESBulkLoad.class.getName());

    /**************************************************************************************
	 * Entry point for the bulk loading													  *
	 * 																					  *	
	 *************************************************************************************/
    public void bulkLoad() {
    	ESBulkLoad esExample = new ESBulkLoad();
        try {
            esExample.initEStransportClinet(); 

            esExample.CSVbulkImport(true); 

            esExample.refreshIndices();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            esExample.closeTransportClient(); 
        }
    }


    public ESBulkLoad(){
        indexName = "creditcardvikdevfun";
        indexTypeName = "bulkindexing";
    }

    /**************************************************************************************
	 * Initialize the elastic search transport client									  *
	 * 																					  *	
	 *************************************************************************************/
    public boolean initEStransportClinet()
    {
        try {
            // un-command this, if you have multiple node
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

            return true;
        } catch (Exception ex) {				
        	log.error("Exception occurred while getting Client : " + ex, ex);
     
            return false;
        }
    }


    /**************************************************************************************
	 * CSV bulk load into the elastic search cluster 									  *
	 * 																					  *	
	 *************************************************************************************/
    public void CSVbulkImport(boolean isHeaderIncluded) throws IOException, ExecutionException, InterruptedException {

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        File file = new File(CREDIT_CARD_FILE_NAME);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        int count=0,noOfBatch=1;
        if(bufferedReader!=null && isHeaderIncluded){
            bufferedReader.readLine();//ignore first line
        }
        while ((line = bufferedReader.readLine())!=null){

            if(line.trim().length()==0){
                continue;
            }
            String data [] = line.split(",");
            
            if(data.length==26){

                try {
                    XContentBuilder xContentBuilder = jsonBuilder()
                            .startObject()
                            .field("ID", data[0])
                            .field("Name", data[1].substring(0, data[1].length()-2 ))
                            .field("LIMIT_BAL", data[2])
                            .field("SEX", data[3])
                            .field("EDUCATION", data[4])
                            .field("MARRIAGE", data[5])
                            .field("AGE", data[6])
                            .field("PAY_0", data[7])
                            .field("PAY_2", data[8])
                            .field("PAY_3", data[9])
                            .field("PAY_4", data[10])
                            .field("PAY_5", data[11])
                            .field("PAY_6", data[12])
                            .field("BILL_AMT1", data[13])
                            .field("BILL_AMT2", data[14])
                            .field("BILL_AMT3", data[15])
                            .field("BILL_AMT4", data[16])
                            .field("BILL_AMT5", data[17])
                            .field("BILL_AMT6", data[18])
                            .field("PAY_AMT1", data[19])
                            .field("PAY_AMT2", data[20])
                            .field("PAY_AMT3", data[21])
                            .field("PAY_AMT4", data[22])
                            .field("PAY_AMT5", data[23])
                            .field("PAY_AMT6", data[24])
                            .field("default.payment.next.month", data[25])
                            .endObject();

                    bulkRequest.add(client.prepareIndex(indexName, indexTypeName, data[0])
                            .setSource(xContentBuilder));

                    if ((count+1) % 500 == 0) {
                        count = 0;
                        addDocumentToESCluser(bulkRequest, noOfBatch, count);
                        noOfBatch++;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    //skip records if wrong date in input file
                }
            }
            count++;
        }
        bufferedReader.close();
        addDocumentToESCluser(bulkRequest,noOfBatch,count);


    }

    public void addDocumentToESCluser(BulkRequestBuilder bulkRequest,int noOfBatch,int count){

        if(count==0){
           
            return;
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {

          
            int numberOfDocFailed = 0;
            Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
            while (iterator.hasNext()){
                BulkItemResponse response = iterator.next();
                if(response.isFailed()){
                    
                    numberOfDocFailed++;
                }
            }
           
        }
    }

    /**************************************************************************************
	 *Refresh before search, so you will get latest indices result												  *
	 * 																					  *	
	 *************************************************************************************/
    public void refreshIndices(){
        client.admin().indices()
                .prepareRefresh(indexName)
                .get(); 
    }


    public void closeTransportClient(){
        if(client!=null){
            client.close();
        }
    }
}
