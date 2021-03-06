package org.hailong.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hailong.core.Color;
import org.hailong.core.Font;

public class DOMElement  {

	private String _namespace;
	private String _name;
	private String _text;
	private Map<String,String> _attributes;
	private DOMElement _parent;
	private List<DOMElement> _childs;
	private DOMDocument _document;
	private DOMStyle _style;
	private IDOMViewEntity _viewEntity;
	private Map<String,Object> _values;
	
	public String getNamespace(){
		return _namespace;
	}
	
	public void setNamespace(String namespace){
		_namespace = namespace;
	}
	
	public String getName(){
		return _name;
	}
	
	public void setName(String name){
		_name = name;
	}
	
	public String getText(){
		return _text;
	}
	
	public void setText(String text){
		_text = text;
	}
	
	public Map<String,String> getAttributes(){
		return _attributes;
	}
	
	public String getAttributeValue(String name){
		return _attributes != null ? _attributes.get(name) : null;
	}
	
	public void setAttributeValue(String name, String value){
		if(_attributes == null){
			_attributes = new HashMap<String,String>(4);
		}
		_attributes.put(name, value);
	}
	
	public void removeAttributeValue(String name){
		if(_attributes  != null){
			_attributes.remove(name);
		}
	}
	
	public DOMElement getParent(){
		return _parent;
	}
	
	public DOMElement[] getChilds(){
		return _childs != null ? _childs.toArray(new DOMElement[_childs.size()]) : new DOMElement[0];
	}
	
	public DOMDocument getDocument(){
		return _document;
	}
	
	public void setDocument(DOMDocument document){
		_document = document;
		if(_childs != null){
			for(DOMElement child : _childs){
				child.setDocument(document);
			}
		}
	}
	
	public void removeFromParent(){
		
		if(_parent != null){
			
			if(_parent._childs != null){
				_parent._childs.remove(this);
			}
			
			_parent = null;
			_document = null;
		}
		
	}
	
	public void addChild(DOMElement element){
		addChild(element,_childs == null ? 0 : _childs.size());
	}
	
	public void addChild(DOMElement element,int index){
		
		if( element._parent != null){
			element.removeFromParent();
		}
		
		element._parent = this;
		element._document = _document;
		
		if(_childs == null){
			_childs = new ArrayList<DOMElement>();
		}
		
		_childs.add(index, element);
	}
	
	public DOMStyle getStyle(){
		return _style;
	}
	
	public void setStyle(DOMStyle style){
		_style = style;
	}
	
	public long longValue(String name,long defaultValue){
		
		String v = getAttributeValue(name);
		
		if(v != null){
			return Long.valueOf(v);
		}
		
		if(_style != null){
			return _style.longValue(name, defaultValue);
		}
		
		return defaultValue;
	}
	
	public int intValue(String name,int defaultValue){

		String v = getAttributeValue(name);
		
		if(v != null){
			return Integer.valueOf(v);
		}
		
		if(_style != null){
			return _style.intValue(name, defaultValue);
		}
		
		return defaultValue;
	}
	
	public double doubleValue(String name,double defaultValue){

		String v = getAttributeValue(name);
		
		if(v != null){
			return Double.valueOf(v);
		}
		
		if(_style != null){
			return _style.doubleValue(name, defaultValue);
		}
		
		return defaultValue;
	}
	
	public float floatValue(String name,float defaultValue){

		String v = getAttributeValue(name);
		
		if(v != null){
			return Float.valueOf(v);
		}
		
		if(_style != null){
			return _style.floatValue(name, defaultValue);
		}
		
		
		return defaultValue;
	}
	
	public String stringValue(String name,String defaultValue){
		
		String v = getAttributeValue(name);
		
		if(v != null){
			return v;
		}
		
		if(_style != null){
			return _style.stringValue(name, defaultValue);
		}
	
		return defaultValue;
	}
	
	public boolean booleanValue(String name,boolean defaultValue){
		
		String v = getAttributeValue(name);
		
		if(v != null){
			return ! ("false".equals(v) ||  "no".equals(v) ||  "".equals(v) || "0".equals(v));
		}
		
		if(_style != null){
			return _style.booleanValue(name, defaultValue);
		}
	
		return defaultValue;
	}
	
	public Color colorValue(String name,Color defaultValue){
		
		String v = getAttributeValue(name);
		
		if(v != null){
			return Color.valueOf(v);
		}
		
		if(_style != null){
			return _style.colorValue(name, defaultValue);
		}
		
		return defaultValue;
		
	}
	
	
	public Font fontValue(String name,Font defaultValue){
		
		String v = getAttributeValue(name);
		
		if(v != null){
			return Font.valueOf(v);
		}
		
		if(_style != null){
			return _style.fontValue(name, defaultValue);
		}
		
		return defaultValue;
	}
	
	public int getChildCount(){
		return _childs != null ? _childs.size() : 0;
	}
	
	public DOMElement getChildAt(int index){
		return _childs != null && index >=0 && index<_childs.size() ? _childs.get(index) : null;
	}
	
	protected IDOMViewEntity elementViewEntity(DOMElement element){
		if(_viewEntity == null && _parent != null){
			return _parent.elementViewEntity(element);
		}
		return _viewEntity;
	}
	
	public IDOMViewEntity getViewEntity(){
		if(_viewEntity==null && _parent != null){
			return _parent.elementViewEntity(this);
		}
		return _viewEntity;
	}
	
	public void setViewEntity(IDOMViewEntity viewEntity){
		if(_viewEntity != viewEntity){
			if(_viewEntity != null){
				_viewEntity.elementDetach(this);
			}
			_viewEntity = viewEntity;
			onViewEntityChanged(_viewEntity);
		}
	}
	
	public void setNeedsDisplay(){
		IDOMViewEntity viewEntity = getViewEntity();
		if(viewEntity != null){
			viewEntity.doNeedsDisplay(this);
		}
	}
	
	public void doAction(){
		IDOMViewEntity viewEntity = getViewEntity();
		if(viewEntity != null){
			viewEntity.doAction(viewEntity,this);
		}
	}
	
	protected void onViewEntityChanged(IDOMViewEntity viewEntity){
		
		for(DOMElement element : getChilds()){
			
			if(element._viewEntity == null){
				element.onViewEntityChanged(viewEntity);
			}
			
		}
		
	}
	
	public boolean isViewEntity(IDOMViewEntity viewEntity){
		return _viewEntity == null || _viewEntity == viewEntity;
	}

	public Object getValue(String key){
		return _values == null ? null : _values.get(key);
	}
	
	public void setValue(String key,Object value){
		if(value == null){
			if(_values !=null){
				_values.remove(key);
			}
		}
		else {
			if(_values == null){
				_values = new HashMap<String,Object>(4);
			}
			_values.put(key, value);
		}
	}
}
