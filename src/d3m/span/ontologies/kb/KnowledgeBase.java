/**
 * 
 */
package d3m.span.ontologies.kb;

//import java.util.Arrays;
import java.util.HashMap;

import ontologies.*;

/** Class <Code><B>KnowledgeBase</B></Code> for representing a knowledge base as 
 * a tuple KB=(O,I,,).
 * @author Claudia Antunes
 * @version 1.0
 */
public class KnowledgeBase{
	/** The next index for a new feature. */
	public static int KB_NR_FEATURES = 0;

	/** The next index for a new instance. */
	public static int KB_NR_INSTANCES = 0;
	
	/** The ontology behind this knowledge base. */
	public Ontology ONTOLOGY;

	/** The set of existing features, indexed by name and attribute. */
	protected HashMap<Integer,HashMap<Object,Feature>> m_featuresByName;
	
	/** The set of existing features, indexed by ID. */
	protected HashMap<Integer,Feature> m_featuresByID;
	
	/** The list of instances created. */
	protected HashMap<Integer,Instance> m_instances;


	/**
	 * Default constructor
	 */
	public KnowledgeBase(){
	}
	
	/**
	 * Constructor from an ontology
	 * @param onto
	 */
	public KnowledgeBase(Ontology onto){
		ONTOLOGY = onto;
		m_featuresByName = new  HashMap<Integer,HashMap<Object,Feature>>();
		m_featuresByID = new HashMap<Integer,Feature>();
		m_instances = new HashMap<Integer,Instance>();

	}
	
	/**
	 * 
	 * @param id
	 * @return the instance with id
	 */
/*	public Instance getInstance(int id){
		return m_instances[id];
	}
*/
	
	/**
	 * Add a new instance to the knowledge base indexed by its id
	 * @param inst
	 */
	public void addInstance(Instance inst){
		m_instances.put(inst.getID(), inst);
	}
	
	/**
	 * 
	 * @return the last instance introduced in the KB
	 */
	public Instance getLastInstance(){
		if (m_instances.isEmpty())
			return null;
		else
			return m_instances.get(KB_NR_INSTANCES-1);
	}
	
	/**
	 * Looks for an instance in the kb.
	 * @param c
	 * @param name the term that identifies the instance
	 * @return return the instance or null if it does not belong to the ontology
	 */
/*	public Instance getInstance(Concept c, String name) {
		int fID, keyAtt = c.getDiffAttributeIndex();
		for (int i = 0; i < m_instances.length; i++) {
			if (c.getID() == m_instances[i].getConcept()) {
				fID = m_instances[i].getFeatureGlobalID(keyAtt);
				if (0 == name.compareToIgnoreCase((String)(m_featuresByName.get(keyAtt).get(fID).getValue())))
					return m_instances[i];
			}
		}
		return null;
	}*/
	
	/**
	 * 
	 * @param key
	 * @return the feature id in attribute att
	 */
	public Feature getFeature(int key){
		return m_featuresByID.get(key);
	}
	
	/**
	 * Look 4 the feature named name for attribute with index att
	 * @param name the name for the future to look 4
	 * @param att the attribute index to be instantiated
	 * @return the feature found, or -1 if it doesn't exist
	 */
	public Feature getFeature(String name, int att){
		HashMap<Object, Feature> map = m_featuresByName.get(att);
		if (null != map)
			return map.get(name);
		
		return null;
	}

	/**
	 * Add a new feature to the KB
	 * @param f the feature to add
	 */
	public void addFeature(Feature f){
		HashMap<Object, Feature> map = m_featuresByName.get(f.getAttribute());
		if (null == map){
			map = new HashMap<Object, Feature>();
			map.put(f.getValue(),f);
			m_featuresByName.put(f.getAttribute(),map);
			
		}
		else
			map.put(f.getValue(),f);
		m_featuresByID.put(f.getID(),f);
	}	
	
/*	public void addNewInstance(Instance inst, Feature f){
		Feature[] fets = new Feature[m_features.length+1];
		fets = Arrays.copyOf(m_features, m_features.length+1);
		fets[fets.length-1] = f;
		m_features = fets;
		Instance[] insts = new Instance[m_instances.length+1];
		insts = Arrays.copyOf(m_instances, m_instances.length+1);
		insts[insts.length-1] = inst;
		m_instances = insts;
	}*/
	
	/**
	 * 
	 * @param inst the instance index
	 * @return the concept implemented by the instance
	 */
	public Concept getConceptFor(int inst){
		return ONTOLOGY.getConcept(m_instances.get(inst).getConcept());
	}
	
	/**
	 * 
	 * @param f
	 * @return the concept where the attribute is defined
	 */
	public Concept getConceptFor(Feature f){
		return ONTOLOGY.getConcept(f.getConcept());
	}
	
	@Override
	public String toString(){
		String st = "KB: Instances \n";
		for (Instance inst : m_instances.values()){
			st += inst.toString(this)+"\n";
		}
		return st;
	}
	
	/**
	 * 
	 * @return some statistics about the KB
	 */
	public String statistics(){
		String st = "";
		st += ONTOLOGY.statistics() + "\tNR INSTANCES=\t" + m_instances.size() + "\tNR FEATURES=\t" + m_featuresByID.size();
		return st;
	}
}
