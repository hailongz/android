package org.hailong.framework.data.predicate;


import org.hailong.framework.data.IDataEntityRawData;

class ExpNotEqual extends ExpValue {

	protected ExpNotEqual(Value left, Value right) {
		super(left, right);
	}

	@Override
	public boolean result(IDataEntityRawData data){
		return super.compare(data) !=0;
	}
	
	@Override
	public String sql(String prefix,String suffix){
		return "("+_left.sql(prefix, suffix)+ " <> "+_right.sql(prefix, suffix)+")";
	}
}
