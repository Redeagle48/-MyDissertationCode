package d3m.span.io.relations;

public class RelationBegin extends Relation{

	String item;
	
	public RelationBegin(String instanceName, String item) {
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
		return "Begin";
	}
	
}
