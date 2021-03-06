package org.hailong.dom;

import org.hailong.core.Edge;
import org.hailong.core.Rect;
import org.hailong.core.Size;

public class DOMLayoutElement extends DOMElement implements IDOMLayoutElement {

	private Rect _frame;
	private Size _contentSize;
	private Edge _padding;
	private Edge _margin;
	
	@Override
	public Rect getFrame() {
		if(_frame == null){
			_frame = new Rect(stringValue("left",null),stringValue("top",null),stringValue("width",null),stringValue("height",null));
		}
		return _frame;
	}

	@Override
	public void setFrame(Rect frame) {
		_frame = frame;
	}

	@Override
	public Size getContentSize() {
		if(_contentSize==null){
			_contentSize = new Size(0,0);
		}
		return _contentSize;
	}

	@Override
	public void setContentSize(Size contentSize) {
		_contentSize = contentSize;
	}

	@Override
	public Edge getPadding() {
		if(_padding == null){
			String v = stringValue("padding",null);
			_padding = new Edge(stringValue("padding-left",v),stringValue("padding-top",v),stringValue("padding-right",v),stringValue("padding-bottom",v));
		}
		return _padding;
	}

	@Override
	public void setPadding(Edge padding) {
		_padding = padding;
	}

	@Override
	public Edge getMargin() {
		if(_margin == null){
			String v = stringValue("margin",null);
			_margin = new Edge(stringValue("margin-left",v),stringValue("margin-top",v),stringValue("margin-right",v),stringValue("margin-bottom",v));
		}
		return _margin;
	}

	@Override
	public void setMargin(Edge margin) {
		_margin = margin;
	}

	@Override
	public Size layoutChildren(Edge padding) {
		
		Rect frame = getFrame();
	    Size size = new Size(0,0);
	    Size insetSize = new Size(frame.getWidth() - padding.getLeft() - padding.getRight()
	                                  , frame.getHeight() - padding.getTop() - padding.getBottom());
	    
	    String layout = stringValue("layout",null);
	    
	    if("flow".equals(layout)){
	        
	    	float x = padding.getLeft();
	    	float y = padding.getTop();
	        float lineHeight = 0;
	        float width = padding.getLeft() + padding.getRight();
	        float maxWidth = frame.getWidth();
	        
	        if(maxWidth == Float.MAX_VALUE){
	        	maxWidth = floatValue("max-width",maxWidth);
	        }
	        
	        for(DOMElement element : getChilds()){
	            
	        	if(element instanceof IDOMLayoutElement){
	        	
	        		IDOMLayoutElement layoutElement = (IDOMLayoutElement) element;
	        		
		            Edge margin = layoutElement.getMargin();
		            
		            layoutElement.layout(new Size(insetSize.getWidth() - margin.getLeft() - margin.getRight()
                            , insetSize.getHeight() - margin.getTop() - margin.getBottom()));
		            
		            Rect r = layoutElement.getFrame();
		  
		            if(( x + r.getWidth() + margin.getLeft() + margin.getRight() <= maxWidth - padding.getRight())){
		                
		            	r.x = x + margin.getLeft();
		            	r.y = y + margin.getTop();
		            	
		                x += r.getWidth() + margin.getLeft() + margin.getRight();
		                
		                if(lineHeight < r.getHeight() + margin.getTop() + margin.getBottom()){
		                    lineHeight = r.getHeight() + margin.getTop() + margin.getBottom();
		                }
		                if(width < x + padding.getRight()){
		                    width = x + padding.getRight();
		                }
		            }
		            else {
		                x = padding.getLeft();
		                y += lineHeight;
		                lineHeight = r.getHeight() + margin.getTop() + margin.getBottom();
		                r.x = x + margin.getLeft();
		                r.y = y + margin.getTop();
		                x += r.getWidth() + margin.getLeft() + margin.getRight();
		                if(width < x + padding.getRight()){
		                    width = x + padding.getRight();
		                }
		            }

	        	}
	        	else if(element instanceof DOMBRElement){
	        		x = padding.getLeft();
	        		y += lineHeight;
	        		lineHeight = 0;
	        	}
	        }
	        
	        size = new Size(width, y + lineHeight + padding.getBottom());
	        
	    }
	    else{
	        
	    	size.width = padding.getLeft() + padding.getRight();
	    	size.height = padding.getTop() + padding.getBottom();
	    	
	        for(DOMElement element : getChilds()){
	            
	        	if(element instanceof IDOMLayoutElement){
	        		
	        		IDOMLayoutElement layoutElement = (IDOMLayoutElement) element;
	        		
	        		layoutElement.layout(insetSize);
		            
		            Rect r = layoutElement.getFrame();
		            
		            String left = element.stringValue("left","0");
		            String right = element.stringValue("right","0");
		            String top = element.stringValue("top","0");
		            String bottom = element.stringValue("bottom","0");
		            
		            if("auto".equals(left)){
		                if("auto".equals(right)){
		                    r.x = (frame.getWidth() - r.getWidth()) / 2.0f;
		                }
		                else{
		                    r.x = (frame.getWidth() - r.getWidth() - padding.getRight() - Float.valueOf(right));
		                }
		            }
		            else{
		                r.x = padding.getLeft() + Float.valueOf(left);
		            }
		            
		            if("auto".equals(top)){
		                if("auto".equals(bottom)){
		                    r.y = (frame.getHeight() - r.getHeight()) / 2.0f;
		                }
		                else{
		                    r.y = frame.getHeight() - r.getHeight() - padding.getBottom() - Float.valueOf(bottom);
		                }
		            }
		            else{
		                r.y = padding.getTop() + Float.valueOf(top);
		            }
		            
		            if(r.getX() + r.getWidth() + padding.getRight() > size.getWidth()){
		                size.width = r.getX() +r.getWidth() +padding.getRight();
		            }
		            
		            if(r.getY() + r.getHeight() + padding.getBottom() > size.getHeight()){
		                size.height = r.getY() +r.getHeight() +padding.getBottom();
		            }
	        	}
	        }
	    }
	    
	    setContentSize(size);
	    
	    return size;
	}

	@Override
	public Size layout(Size size) {
		
		Edge insets = getPadding();
	    
	    Rect frame = new Rect(0,0
	    		,Rect.getValue(stringValue("width",null), 0.0f, size.getWidth())
	    		,Rect.getValue(stringValue("height",null), 0.0f, size.getHeight()));
	    
	
	    
	    setFrame(frame);
	    
	    if(frame.getWidth() == Float.MAX_VALUE || frame.getHeight() == Float.MAX_VALUE){
	        
	        Size contentSize = layoutChildren(insets);
	        
	        if(frame.getWidth() == Float.MAX_VALUE){
	        	
	            frame.width = contentSize.width;
	            
	            float max = Rect.getValue(stringValue("max-width",null), frame.getWidth(), size.getWidth());
	            float min = Rect.getValue(stringValue("min-width",null), frame.getWidth(), size.getWidth());
	            
	            if(frame.getWidth() > max){
	                frame.width = max;
	            }
	            if(frame.getWidth() < min){
	                frame.width = min;
	            }
	        }
	        
	        if(frame.getHeight() == Float.MAX_VALUE){
	        	
	            frame.height = contentSize.height;
	            
	            float max = Rect.getValue(stringValue("max-height",null), frame.getHeight(), size.getHeight());
	            float min = Rect.getValue(stringValue("min-height",null), frame.getHeight(), size.getHeight());
	          
	            if(frame.getHeight() > max){
	                frame.height = max;
	            }
	            if(frame.getHeight() < min){
	                frame.height = min;
	            }
	        }
	        
	        setFrame(frame);
	        
	        return contentSize;
	    }
	    else{
	        return layoutChildren(insets);
	    }
	}

	@Override
	public Size layout() {
		return layoutChildren(getPadding());
	}

	public boolean isLayouted(){
		return _frame != null;
	}
}
