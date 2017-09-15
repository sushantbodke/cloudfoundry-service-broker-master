package org.springframework.cloud.servicebroker.mongodb.service;

import java.util.*;

import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.mongodb.model.ServiceInstanceBinding;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceInstanceBindingService implements ServiceInstanceBindingService {

	private static Map<String, ServiceInstanceBinding> bindingRepository = new HashMap<>();


	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();

		ServiceInstanceBinding binding = bindingRepository.get(bindingId);
		if (binding != null) {
			throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
		}

		Map<String, Object> credentials = new HashMap<String, Object>();
		credentials.put("uri", (Object) UUID.randomUUID());
		credentials.put("smtphost", (Object) "smtp.gmail.com");
		credentials.put("smtpport", (Object) "587");
		credentials.put("smtpusername", (Object) "cahtestmail");
		credentials.put("smtppassword", (Object) "pass@123$");
		credentials.put("smtpauth", (Object) true);
		credentials.put("smtpstarttls", (Object) true);


		binding = new ServiceInstanceBinding(bindingId, serviceInstanceId, credentials, null, request.getBoundAppGuid());
		bindingRepository.put(bindingId, binding);
		return new CreateServiceInstanceAppBindingResponse().withCredentials(credentials);
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		String bindingId = request.getBindingId();
		ServiceInstanceBinding binding = getServiceInstanceBinding(bindingId);

		if (binding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(bindingId);
		}

		bindingRepository.remove(bindingId);
	}

	protected ServiceInstanceBinding getServiceInstanceBinding(String id) {
		return bindingRepository.get(id);
	}
}
