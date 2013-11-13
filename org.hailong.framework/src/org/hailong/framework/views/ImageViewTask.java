package org.hailong.framework.views;

import java.io.File;

import org.hailong.framework.tasks.ILocalResourceTask;
import org.hailong.framework.tasks.IResourceTask;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewTask extends ImageView implements IResourceTask ,ILocalResourceTask {

	private String _imageUrl = null;
	private boolean _forceDownload = false;
	private double _resourceSize = 200;

	
	public ImageViewTask(Context context) {
		super(context);

	}
	
	public ImageViewTask(Context context, AttributeSet attrs) {
		super(context, attrs);

	}


	public ImageViewTask(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public String getImageUrl(){
		return _imageUrl;
	}
	
	public void setImageUrl(String imageUrl){
		_imageUrl = imageUrl;
	}
	
	public String getResourceUri() {
		return _imageUrl;
	}


	public boolean isNeedDownload() {
		return _imageUrl != null && _imageUrl.length() >0;
	}


	public Object setResourceLocalFile(File localUri) {
		if(localUri != null ){
			Drawable image = Drawable.createFromPath(localUri.getPath());
			setImageDrawable(image);
			_imageUrl = null;
			
			return image;
		}
		return null;
	}
	
	public boolean isForceDownload(){
		return _forceDownload;
	}
	
	public void setForceDownload(boolean forceDownload){
		_forceDownload = forceDownload;
	}

	public double getResourceSize(){
		return _resourceSize;
	}
	
	public void setResourceSize(double size){
		_resourceSize = size;
	}

	public void setResourceObject(Object obj) {
		if(obj != null){
			setImageDrawable((Drawable)obj);
			_imageUrl = null;
		}
	}
}
