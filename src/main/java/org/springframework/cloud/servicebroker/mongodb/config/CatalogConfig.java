package org.springframework.cloud.servicebroker.mongodb.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {
	
	@Bean
	public Catalog catalog() {
		return new Catalog(Collections.singletonList(
				new ServiceDefinition(
						"email-service-broker",
						"email-service",
						"A simple G-mail service broker implementation",
						true,
						false,
						Collections.singletonList(
								new Plan("gmail-plan",
										"default",
										"This is a default email plan.  Pay $1 per email",
										getPlanMetadata())),
						Arrays.asList("smtp", "document"),
						getServiceDefinitionMetadata(),
						null,
						null)));
	}
	
/* Used by Pivotal CF console */

	private Map<String, Object> getServiceDefinitionMetadata() {
		Map<String, Object> sdMetadata = new HashMap<>();
		sdMetadata.put("displayName", "Email");
		sdMetadata.put("imageUrl", "https://cdn4.iconfinder.com/data/icons/ionicons/512/icon-email-128.png");
		sdMetadata.put("longDescription", "Email Service");
		sdMetadata.put("providerDisplayName", "Sushant Bodke");
		// sdMetadata.put("documentationUrl", "https://github.com/spring-cloud-samples/cloudfoundry-mongodb-service-broker");
		// sdMetadata.put("supportUrl", "https://github.com/spring-cloud-samples/cloudfoundry-mongodb-service-broker");
		return sdMetadata;
	}
	
	private Map<String,Object> getPlanMetadata() {
		Map<String,Object> planMetadata = new HashMap<>();
		planMetadata.put("costs", getCosts());
		planMetadata.put("bullets", getBullets());
		return planMetadata;
	}

	private List<Map<String,Object>> getCosts() {
		Map<String,Object> costsMap = new HashMap<>();
		
		Map<String,Object> amount = new HashMap<>();
		amount.put("usd", 1.0);
	
		costsMap.put("amount", amount);
		costsMap.put("unit", "EMAIL");
		
		return Collections.singletonList(costsMap);
	}
	
	private List<String> getBullets() {
		return Arrays.asList("Basic email service",
				"send via Gmail servers");
	}
	
}
