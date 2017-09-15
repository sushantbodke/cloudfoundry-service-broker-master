package org.springframework.cloud.servicebroker.mongodb.service;

import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationRequest;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationResponse;
import org.springframework.cloud.servicebroker.model.OperationState;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.mongodb.model.ServiceInstance;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class EmailServiceInstanceService implements ServiceInstanceService {

	private static Map<String, ServiceInstance> repository = new HashMap<>();


	@Override
	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
		ServiceInstance instance = repository.get(request.getServiceInstanceId());
		if (instance != null) {
			throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
		}

		instance = new ServiceInstance(request);

		System.out.println("Service Instance Created : " +instance.getServiceInstanceId());
		if (repository.containsKey(instance.getServiceInstanceId())) {
			repository.remove(instance.getServiceInstanceId());
		}

		repository.put(request.getServiceInstanceId(), instance);

		return new CreateServiceInstanceResponse();
	}

	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
		return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
	}

	public ServiceInstance getServiceInstance(String id) {
		return repository.get(id);
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) {
		String instanceId = request.getServiceInstanceId();
		System.out.println("Service Instance Deleted : " +instanceId);

		ServiceInstance instance = repository.get(instanceId);
		if (instance == null) {
			throw new ServiceInstanceDoesNotExistException(instanceId);
		}

		repository.remove(instanceId);
		return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {
		String instanceId = request.getServiceInstanceId();
		System.out.println("Service Instance Updated 1 : " +instanceId);

		ServiceInstance instance = repository.get(instanceId);
		if (instance == null) {
			throw new ServiceInstanceDoesNotExistException(instanceId);
		}

		repository.remove(instanceId);
		ServiceInstance updatedInstance = new ServiceInstance(request);
		System.out.println("Service Instance Updated 2 : " +updatedInstance.getServiceInstanceId());

		repository.put(updatedInstance.getServiceInstanceId(), updatedInstance);
		return new UpdateServiceInstanceResponse();
	}

}