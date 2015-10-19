/**
 * BCNF decomposition
 * CS5320 Homework3
 * @author Yuzhuo Sun(ys684) Bangrui Chen(bc496) Jingyao Ren(jr986)
 * @date Oct 18, 2015
 */
import java.util.*;

public class BCNF {

  /**
   * Decompose the given relationship with BCNF
   * @param Input attribute set
   * @param Functional Dependencies
   * @return BCNF decomposition wrapped into a set of attribute set 
   **/
  public static Set<AttributeSet> decompose(AttributeSet attributeSet,
          Set<FunctionalDependency> functionalDependencies) {
	  Set<AttributeSet> result = new HashSet<AttributeSet>();
	  decomposeHelper(attributeSet, functionalDependencies, result);
	  return result;
  }
	
  /**
   * Overwritten method of BCNF decomposition
   * @param Input attribute set
   * @param Functional Dependencies
   * @param Result
   **/
  private static void decomposeHelper(AttributeSet attributeSet,
        Set<FunctionalDependency> functionalDependencies, Set<AttributeSet> result) {
	int size = attributeSet.size();
	List<Attribute> attributeList = new ArrayList<>();
	
	for(Iterator<Attribute> a = attributeSet.iterator(); a.hasNext();){
		attributeList.add(a.next());
	}
	
	for(int i = 1; i < Math.pow(2, size); i++){
		// Assign temp to be the X in the algorithm given on the book
		AttributeSet temp = new AttributeSet();
		int k = i;
		Iterator<Attribute> a;
		
		// Find the subset X in attributeSet
		for(a = attributeSet.iterator(); a.hasNext();){
			if(k % 2 == 1){
				temp.addAttribute(a.next());
			}else a.next();
			k = k / 2;
			if(k == 0) break;
		}

		// Compute closure
		AttributeSet closure = closure(temp, functionalDependencies);
		closure = intersect(closure, attributeSet);
		// Check if X determines itself or if X is a super key
		if((closure == null && temp == null) || (closure.size() == 0 && temp.size() == 0) || closure.equals(temp) || closure.equals(attributeSet)){
			continue;
		}else{
			// If X does not hold, further decompose attributeSet
			AttributeSet nonClosure = new AttributeSet();
			// Compute X union (X+)^C
			for(a = attributeSet.iterator(); a.hasNext();){
				Attribute tempA = a.next();
				if(!closure.contains(tempA) || temp.contains(tempA))	nonClosure.addAttribute(tempA);
			}
			// Recurse on both side
			decomposeHelper(closure, functionalDependencies, result);
			decomposeHelper(nonClosure, functionalDependencies, result);
			return;
		}
	}
	result.add(attributeSet);
  }
  

  /**
   * Compute the closure of a given attribute set
   * @param Input attribute set
   * @param Functional dependencies
   * @return The closure of input attribute set
   **/
  public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
	// initialization
	Hashtable<FunctionalDependency, Integer> count = new Hashtable<>();
	AttributeSet newdep = new AttributeSet(attributeSet); 
	AttributeSet update = new AttributeSet(attributeSet); 
	HashMap<Attribute, List<FunctionalDependency>> fdMap = new HashMap<>(); 

	for (FunctionalDependency fd : functionalDependencies){
		count.put(fd, fd.independent().size());
		for (Iterator<Attribute> it = fd.independent().iterator(); it.hasNext();){
			Attribute at = it.next();
			if(fdMap.containsKey(at))	fdMap.get(at).add(fd);
			else{
				List<FunctionalDependency> temp = new ArrayList<FunctionalDependency>();
				temp.add(fd);
				fdMap.put(at, temp);
			}
		}
	}
	
	// Computation
	while(!update.equals(null) && update.size() != 0){
		// Choose an A in update
		Attribute a = update.iterator().next();
		AttributeSet newUpdate = new AttributeSet();
		// update: = update - a
		for(Iterator<Attribute> it = update.iterator(); it.hasNext();){
			Attribute temp = it.next();
			if(temp != a)	newUpdate.addAttribute(temp);
		}
		
		update = newUpdate;
		
		if(fdMap.containsKey(a)){
			for(FunctionalDependency fd : fdMap.get(a)){
				count.put(fd, count.get(fd) - 1);
				if(count.get(fd) == 0){
					AttributeSet add = minus(fd.dependent(), newdep);
					union(add, newdep);
					union(add, update);
				}
			}
		}
	}
	  
    return newdep;
  }
   
  /**
   * Compute union between two attribute set
   * @param Input attribute set A
   * @param Input attribute set B, computed union saved here!
   **/
  private static void union(AttributeSet a, AttributeSet b){
	for(Iterator<Attribute> it = a.iterator(); it.hasNext();){
		Attribute at = it.next();
		if(!b.contains(at)){
			b.addAttribute(at);
		}
	}
  }
  
  /**
   * Compute intersect between two attribute set
   * @param Input attribute set A
   * @param Input attribute set B
   * @return Output intersected attribute
   **/
  private static AttributeSet intersect(AttributeSet a, AttributeSet b){
	AttributeSet result = new AttributeSet();
	for(Iterator<Attribute> it = a.iterator(); it.hasNext();){
		Attribute at = it.next();
		if(b.contains(at)){
			result.addAttribute(at);
		}
	}
	return result;
  }
  
  /**
   * Compute minus between two attribute set
   * @param Input attribute set A
   * @param Input attribute set B
   * @return Output minus attribute
   **/
  private static AttributeSet minus(AttributeSet a, AttributeSet b){
	AttributeSet result = new AttributeSet();
	for(Iterator<Attribute> it = a.iterator(); it.hasNext();){
		Attribute at = it.next();
		if(!b.contains(at)){
			result.addAttribute(at);
		}
	}
	return result;
  }
  
}
