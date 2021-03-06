package org.hailong.service.tasks;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.hailong.service.ITask;

public interface IHttpTask<T> extends ITask { 
	
	public HttpUriRequest getHttpRequest();
	public ResponseHandler<T> getResponseHandler();
	
	public boolean isCanceled();
	public void setCanceled(boolean canceled);
	public void sendFinishMessage(T result,Object waiter);
	public void sendErrorMessage(Exception ex,Object waiter);
	public void sendWillRequestMessage(Object waiter);
	
}
