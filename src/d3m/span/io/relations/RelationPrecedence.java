package d3m.span.io.relations;

public class RelationPrecedence extends Relation {
	
	String antecedent, consequent;
	
	public RelationPrecedence(String instanceName, String antecedent, String consequent) {
		this.instanceName = instanceName;
		this.antecedent = antecedent;
		this.consequent = consequent;
	}
	
	public String getAntecedentInstanceName() {
		if(instanceName != null) {
			return this.antecedent;
		} else return "No Antecedent Instance Name defined";
	}
	
	public String getConsequentInstanceName() {
		if(instanceName != null) {
			return this.consequent;
		} else return "No Consequent Instance Name defined";
	}

	@Override
	public String getRelationName() {
		return "Precedence";
	}

}
