package zigen.plugin.db.ui.internal;

import zigen.plugin.db.ext.oracle.internal.OracleSourceInfo;
import zigen.plugin.db.ext.oracle.internal.OracleTriggerInfo;

public class Trigger extends OracleSource {
	
	ITable table;
	
	OracleTriggerInfo oracleTriggerInfo;
	
	public Trigger(ITable table, OracleTriggerInfo oracleTriggerInfo){
		super(oracleTriggerInfo.getName());
		this.table = table;
		this.oracleTriggerInfo = oracleTriggerInfo;
		
		OracleSourceInfo info = new OracleSourceInfo();
		info.setName(oracleTriggerInfo.getName());
		info.setOwner(oracleTriggerInfo.getOwner());
		info.setType("TRIGGER");
		setOracleSourceInfo(info);
		
	}
	public ITable getTable(){
		return table;
	}

	public String getOwner(){
		return oracleTriggerInfo.getOwner();	// trigger's owner
	}
	public String getType(){
		return oracleTriggerInfo.getType();
	}
	
	public String getEvent(){
		return oracleTriggerInfo.getEvent();
	}
}
