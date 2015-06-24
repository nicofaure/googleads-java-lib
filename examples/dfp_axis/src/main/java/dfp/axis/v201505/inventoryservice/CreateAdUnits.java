// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dfp.axis.v201505.inventoryservice;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201505.AdUnit;
import com.google.api.ads.dfp.axis.v201505.AdUnitSize;
import com.google.api.ads.dfp.axis.v201505.AdUnitTargetWindow;
import com.google.api.ads.dfp.axis.v201505.ApiException;
import com.google.api.ads.dfp.axis.v201505.EnvironmentType;
import com.google.api.ads.dfp.axis.v201505.InventoryServiceInterface;
import com.google.api.ads.dfp.axis.v201505.NetworkServiceInterface;
import com.google.api.ads.dfp.axis.v201505.Size;
import com.google.api.ads.dfp.axis.v201505.TargetPlatform;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example creates new ad units. To determine which ad units exist, run GetAllAdUnits.java.
 * Credentials and properties in {@code fromFile()} are pulled from the "ads.properties" file. See
 * README for more info. Tags: InventoryService.createAdUnits
 *
 * @author Adam Rogal
 */
public class CreateAdUnits {

	public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
		// Get the InventoryService.
		InventoryServiceInterface inventoryService = dfpServices.get(session, InventoryServiceInterface.class);

		// Get the NetworkService.
		NetworkServiceInterface networkService = dfpServices.get(session, NetworkServiceInterface.class);

		// Set the parent ad unit's ID for all ad units to be created under.

		BufferedReader csv = new BufferedReader(new FileReader("/Users/nfaure/dfp_google/exampleMLA.csv"));
		String line;
		Map<String, List<AdUnit>> adUnitsMap = new HashMap<String, List<AdUnit>>();
		System.out.println("Loading CSV file");
		while ((line = csv.readLine()) != null) {
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
			webAdUnit.setTargetPlatform(TargetPlatform.WEB);
			webAdUnit.setTargetWindow(AdUnitTargetWindow.BLANK);
			webAdUnit.setAdUnitSizes(new AdUnitSize[] { webAdUnitSize });
			webAdUnit.setParentId(splitLine[3]);

			List<AdUnit> adUnits = adUnitsMap.get(splitLine[0]);
			if (adUnits == null) {
				adUnits = new ArrayList<AdUnit>();
				adUnitsMap.put(splitLine[0], adUnits);
			}
			adUnits.add(webAdUnit);
		}
		csv.close();

		Map<String, AdUnit> createdAdUnitMap = new HashMap<String, AdUnit>();
		createL1AdUnit(createdAdUnitMap, adUnitsMap, inventoryService, networkService);
		createLnAdUnit(createdAdUnitMap, adUnitsMap, inventoryService, networkService);

	}

	private static void createLnAdUnit(Map<String, AdUnit> createdAdUnitMap, Map<String, List<AdUnit>> adUnitsMap,
			InventoryServiceInterface inventoryService, NetworkServiceInterface networkService) {
		try {
			for (String key : adUnitsMap.keySet()) {
				if (!"1".equals(key)) {
					AdUnit[] createdAdUnits;
					for (AdUnit adUnit : adUnitsMap.get(key)) {
						String parentAdUnitId = createdAdUnitMap.get(adUnit.getParentId()).getId();
						System.out.println(format("Setting parent id %s for AdUnit %s", parentAdUnitId, adUnit.getAdUnitCode()));
						adUnit.setParentId(parentAdUnitId);
					}
					createdAdUnits = inventoryService.createAdUnits(adUnitsMap.get(key).toArray(new AdUnit[] {}));
					for (AdUnit adUnit : createdAdUnits) {
						createdAdUnitMap.put(adUnit.getAdUnitCode(), adUnit);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Map<String, AdUnit> createL1AdUnit(Map<String, AdUnit> createdAdUnitMap, Map<String, List<AdUnit>> adUnitsMap,
			InventoryServiceInterface inventoryService, NetworkServiceInterface networkService) {
		AdUnit[] createdAdUnits = null;
		try {
			String parentAdUnitId = networkService.getCurrentNetwork().getEffectiveRootAdUnitId();
			for (AdUnit adUnit : adUnitsMap.get("1")) {
				adUnit.setParentId(parentAdUnitId);
			}
			createdAdUnits = inventoryService.createAdUnits(adUnitsMap.get("1").toArray(new AdUnit[] {}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AdUnit adUnit : createdAdUnits) {
			createdAdUnitMap.put(adUnit.getAdUnitCode(), adUnit);
		}
		return createdAdUnitMap;
	}

	public static void main(String[] args) throws Exception {
		// Generate a refreshable OAuth2 credential.
		Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.DFP).fromFile().build().generateCredential();
		DfpSession session = new DfpSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();

		DfpServices dfpServices = new DfpServices();

		runExample(dfpServices, session);
	}
}
