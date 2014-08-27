package d3m.span.io.relations;

public class RelationExists extends Relation{

	String item;
	
	public RelationExists(String instanceName, String item) {
		this.instanceName = instanceName;
		this.item = item;
	}
	
	public String getItemInstanceName() {
		if(instanceName != null) {
			return this.item;
		} else return "No Item Instance Name defined";
	}

	@Override
	public String getRelationName() {
		return "Exists";
	}
	
}
