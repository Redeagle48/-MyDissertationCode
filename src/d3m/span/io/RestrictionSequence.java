package d3m.span.io;

import java.util.ArrayList;

import d3m.span.io.relationProperties.RelationProperty;
import d3m.span.io.relations.Relation;
import d3m.span.io.semanticRestrictions.RestrictionSemantic;


/****************
 * Class representing the restriction being analyzed
 * @author antoniopereira
 *
 */

public class RestrictionSequence {

	final int ID;
	String sequenceName;
	
	int countSequentialProperties = 0;
	int countConcurrentialProperties = 0;
	
	ArrayList<String> items;
	ArrayList<Relation> relations;
	ArrayList<RelationProperty> relationProperties;
	boolean hasRoot;
	boolean hasLeaf;
	
	public RestrictionSequence(String sequence) {
		ID = 1;
		this.sequenceName = sequence;
		relations = new ArrayList<Relation>();
		relationProperties = new ArrayList<RelationProperty>();
		items = new ArrayList<String>();
	}
	
	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequence) {
		this.sequenceName = sequence;
	}
	
	public void addRelation(Relation relation){
		relations.add(relation);
	}
	
	public ArrayList<Relation> getRelations(){
		return relations;
	}
	
	public void addRelationPropertie(RelationProperty relationProperty){
		relationProperties.add(relationProperty);
	}
	
	public ArrayList<RelationProperty> getRelationProperties(){
		return relationProperties;
	}
	
	public int getcountSequentialProperties() {
		++countSequentialProperties;
		return countSequentialProperties;
	}
	
	public int getcountConcurrentialProperties() {
		++countConcurrentialProperties;
		return countConcurrentialProperties;
	}
	
	public void insertItem(String item){
		items.add(item);
	}
	
	public boolean existsRoot () {
		return this.hasRoot;
	}
	
	public void insertRoot() {
		hasRoot = true;
	}
	
	public boolean existsLeaf () {
		return this.hasLeaf;
	}
	
	public void insertLeaf() {
		hasLeaf = true;
	}
}
