package d3m.span.io.relations;

public abstract class Relation {
	
	String instanceName;
	
	public String getInstanceName() {
		if(instanceName != null) {
			return this.instanceName;
		} else return "No Instance Name defined";
	}
	
	public abstract String getRelationName();
			
}
