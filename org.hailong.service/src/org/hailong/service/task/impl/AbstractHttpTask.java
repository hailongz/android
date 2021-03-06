package org.hailong.service.task.impl;

import org.apache.http.client.methods.HttpUriRequest;
import org.hailong.service.tasks.IHttpAPITask;
import org.hailong.service.tasks.IHttpResourceTask;
import org.hailong.service.tasks.IHttpTask;

import android.os.Handler;
import android.os.Message;

public abstract class AbstractHttpTask<T> extends Handler implements IHttpTask<T>,IHttpResourceTask<T>,IHttpAPITask<T>{
	
	private final static int WHAT_FINISH = 1;
	private final static int WHAT_ERROR = 2;
	private final static int WHAT_WILLREQUEST = 3;
	
	protected HttpUriRequest httpRequest;
	private Object waiter;
	private boolean canceled;
	private Object _source;
	
	public Object getSource(){
		return _source;
	}
	
	public void setSource(Object source){
		_source = source;
	}
	
	public AbstractHttpTask(HttpUriRequest httpRequest){
		this.httpRequest = httpRequest;
	}
	
	public HttpUriRequest getHttpRequest() {
		return httpRequest;
	}
	
	public void setHttpRequest(HttpUriRequest httpRequest){
		this.httpRequest = httpRequest;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message message){
		
		if(!canceled){
			if(message.what == WHAT_FINISH){
				onLoaded((T)message.obj);
			}
			else if(message.what == WHAT_ERROR){
				onException((Exception)message.obj);
			}
			else if(message.what == WHAT_WILLREQUEST){
				onWillRequest();
			}
		}
		
		if(waiter != null){
			
			synchronized (waiter) {
				waiter.notifyAll();
			}
			
			waiter = null;
		}
	}
	
	public void onBackgroundLoaded(T result){
		
	}
	
	public abstract void onLoaded(T result);
	
	public abstract void onException(Exception ex);
	
	public void onWillRequest(){
		
	}

	public void sendFinishMessage(T result,Object waiter){
		
		onBackgroundLoaded(result);
		
		this.waiter = waiter;
		Message msg = new Message();
		msg.what = WHAT_FINISH;
		msg.obj = result;
		synchronized (waiter) {
			sendMessage(msg);
			try {
				waiter.wait(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	

	public void sendErrorMessage(Exception ex,Object waiter){
		this.waiter = waiter;
		Message msg = new Message();
		msg.what = WHAT_ERROR;
		msg.obj = ex;
		synchronized (waiter) {
			sendMessage(msg);
			try {
				waiter.wait(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void sendWillRequestMessage(Object waiter){
		this.waiter = waiter;
		Message msg = new Message();
		msg.what = WHAT_WILLREQUEST;
		synchronized (waiter) {
			sendMessage(msg);
			try {
				waiter.wait(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public boolean isCanceled(){
		return canceled;
	}
	
	public void setCanceled(boolean canceled){
		this.canceled = canceled;
	}

}
