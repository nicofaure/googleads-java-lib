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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201505.StatementBuilder;
import com.google.api.ads.dfp.axis.v201505.AdUnit;
import com.google.api.ads.dfp.axis.v201505.AdUnitPage;
import com.google.api.ads.dfp.axis.v201505.AdUnitSize;
import com.google.api.ads.dfp.axis.v201505.EnvironmentType;
import com.google.api.ads.dfp.axis.v201505.InventoryServiceInterface;
import com.google.api.ads.dfp.axis.v201505.Size;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Sleeper;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * This example updates ad unit sizes by adding a banner ad size. To determine which ad units exist,
 * run GetAllAdUnits.java. Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info. Tags: InventoryService.getAdUnitsByStatement
 * Tags: InventoryService.updateAdUnits
 *
 * @author Adam Rogal
 */
public class UpdateAdUnits {

	@SuppressWarnings("resource")
	public static void runExample(DfpServices dfpServices, DfpSession session, String filePath) throws Exception {
		BufferedReader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
		InventoryServiceInterface inventoryService = dfpServices.get(session, InventoryServiceInterface.class);
		String line;
		while ((line = inputFile.readLine()) != null) {
			System.out.println(format("Reading line %s from file", line));
			String[] dataAsArray = line.split("\t");
			String adUnitCode = dataAsArray[0];
			AdUnitSize[] sizes = obtainSizes(dataAsArray[1]);
			StatementBuilder statementBuilder = new StatementBuilder().where("adunitcode = :code").orderBy("id ASC").limit(100)
					.withBindVariableValue("code", adUnitCode);

			// Get the ad unit.
			AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());

			if (page != null && page.getResults() != null && page.getResults().length > 0) {
				AdUnit adUnit = Iterables.getOnlyElement(Arrays.asList(page.getResults()));
				System.out.println("ADUNITCODE:" + adUnit.getAdUnitCode());

				if (adUnit != null) {
					List<AdUnitSize> allSizes = new ArrayList<AdUnitSize>(Arrays.asList(sizes));
					if (adUnit.getAdUnitSizes() != null) {
						allSizes.addAll(Arrays.asList(adUnit.getAdUnitSizes()));
					}
					AdUnitSize[] updatedSizes = allSizes.toArray(new AdUnitSize[allSizes.size()]);
					adUnit.setAdUnitSizes(updatedSizes);
					adUnit.getInheritedAdSenseSettings().getValue().setAdSenseEnabled(false);
					System.out.println(format("AdUnit with id: %s will be updated", adUnit.getId()));
					AdUnit[] adUnits = inventoryService.updateAdUnits(new AdUnit[] { adUnit });
					System.out.println("AdUnits:" + adUnits);
					for (AdUnit updatedAdUnit : adUnits) {
						List<String> adUnitSizeStrings = Lists.newArrayList();
						for (AdUnitSize updatedAdUnitSize : updatedAdUnit.getAdUnitSizes()) {
							adUnitSizeStrings.add(String.format("%dx%d", updatedAdUnitSize.getSize().getWidth(), updatedAdUnitSize.getSize()
									.getHeight()));
						}
						System.out.printf("Ad unit with ID \"%s\", name \"%s\", and sizes [%s] was updated.\n", updatedAdUnit.getId(),
								updatedAdUnit.getName(), Joiner.on(", ").join(adUnitSizeStrings));
						System.out
								.println(String
										.format("You can check this adunit going to: https://www.google.com/dfp/105773011#inventory/inventory/adSlotId=%s&createTargetPlatform=WEB",
												updatedAdUnit.getId()));
						System.out.println("Waiting 500 ms");
					}
				}
			} else {
				System.out.println(format("AdUnit with AdUnitCode: %s was not found", adUnitCode));
			}
		}

		System.out.println("END");
	}

	private static AdUnitSize[] obtainSizes(String sizes) {
		AdUnitSize[] adUnitSizes;
		System.out.println("Size: " + sizes + ";");
		if (!"".equals(sizes)) {
			String[] sizeAsCollection = sizes.split(";");
			adUnitSizes = new AdUnitSize[sizeAsCollection.length];
			for (int i = 0; i < sizeAsCollection.length; i++) {
				System.out.println(format("Adding new size: %s", sizeAsCollection[i]));
				String[] widthAndHeight = sizeAsCollection[i].split("x");
				Size webSize = new Size();
				webSize.setWidth(new Integer(widthAndHeight[0]));
				webSize.setHeight(new Integer(widthAndHeight[1]));
				webSize.setIsAspectRatio(false);
				AdUnitSize webAdUnitSize = new AdUnitSize();
				webAdUnitSize.setSize(webSize);
				webAdUnitSize.setEnvironmentType(EnvironmentType.BROWSER);
				adUnitSizes[i] = webAdUnitSize;
			}
		} else {
			adUnitSizes = new AdUnitSize[] {};
		}
		return adUnitSizes;
	}

	public static void main(String[] args) throws Exception {
		// Generate a refreshable OAuth2 credential.
		Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.DFP).fromFile().build().generateCredential();

		// Construct a DfpSession.
		DfpSession session = new DfpSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();

		DfpServices dfpServices = new DfpServices();

		runExample(dfpServices, session, args[0]);
	}
}
