// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dfp.axis.v201505.inventoryservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201505.AdUnit;
import com.google.api.ads.dfp.axis.v201505.AdUnitSize;
import com.google.api.ads.dfp.axis.v201505.AdUnitTargetWindow;
import com.google.api.ads.dfp.axis.v201505.EnvironmentType;
import com.google.api.ads.dfp.axis.v201505.InventoryServiceInterface;
import com.google.api.ads.dfp.axis.v201505.NetworkServiceInterface;
import com.google.api.ads.dfp.axis.v201505.Size;
import com.google.api.ads.dfp.axis.v201505.TargetPlatform;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example creates new ad units. To determine which ad units exist, run
 * GetAllAdUnits.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: InventoryService.createAdUnits
 *
 * @author Adam Rogal
 */
public class CreateAdUnits {

  public static void runExample(DfpServices dfpServices, DfpSession session)
      throws Exception {
    // Get the InventoryService.
    InventoryServiceInterface inventoryService =
        dfpServices.get(session, InventoryServiceInterface.class);

    // Get the NetworkService.
    NetworkServiceInterface networkService =
        dfpServices.get(session, NetworkServiceInterface.class);

    // Set the parent ad unit's ID for all ad units to be created under.
    String parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();

    BufferedReader csv = new BufferedReader(new FileReader("/Users/goliva/google_dfp/exampleMLA.csv"));
    String line;
    List<AdUnit> adUnits = new ArrayList<AdUnit>();
    while ((line=csv.readLine()) != null) {
//    	try{
    		String[] splitLine = line.split(",");
    		Size webSize = new Size();
    		webSize.setWidth(new Integer(splitLine[4]));
    		webSize.setHeight(new Integer(splitLine[5]));
    		webSize.setIsAspectRatio(false);
    		
    		AdUnitSize webAdUnitSize = new AdUnitSize();
    		webAdUnitSize.setSize(webSize);
    		webAdUnitSize.setEnvironmentType(EnvironmentType.BROWSER);
    		
    		
    		AdUnit webAdUnit = new AdUnit();
    		webAdUnit.setName(splitLine[2]);
    		webAdUnit.setAdUnitCode(splitLine[1]);
    		if (StringUtils.isEmpty(splitLine[3])){
    			webAdUnit.setParentId(parentAdUnitId);        	
    		}else{
    			webAdUnit.setParentId(splitLine[3]);
    		}
    		
    		webAdUnit.setTargetPlatform(TargetPlatform.WEB);
    		webAdUnit.setTargetWindow(AdUnitTargetWindow.BLANK);
    		webAdUnit.setAdUnitSizes(new AdUnitSize[]{webAdUnitSize});
    		adUnits.add(webAdUnit);
    		
//    		
//    	}catch(Exception e){
//    		System.out.printf("Error in line: "+line+":"+e.getMessage()+" "+e.getCause()+" "+e.getStackTrace()+"\n");
//    	}
    }
    csv.close();
    
    AdUnit[] adUnitsAsList = (AdUnit[]) adUnits.toArray(new AdUnit[adUnits.size()]);
    
    AdUnit[] adUnitsLog = inventoryService.createAdUnits(adUnitsAsList);
	
	for (AdUnit adUnit : adUnitsLog) {
		System.out.printf("An ad unit with ID \"%s\", name \"%s\" was created.\n", adUnit.getId(),
				adUnit.getName());
	}
  }

  public static void main(String[] args) throws Exception {
    // Generate a refreshable OAuth2 credential.
    Credential oAuth2Credential = new OfflineCredentials.Builder()
        .forApi(Api.DFP)
        .fromFile()
        .build()
        .generateCredential();
    DfpSession session = new DfpSession.Builder()
        .fromFile()
        .withOAuth2Credential(oAuth2Credential)
        .build();

    DfpServices dfpServices = new DfpServices();

    runExample(dfpServices, session);
  }
}
