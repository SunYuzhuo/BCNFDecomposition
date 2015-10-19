import java.util.*;

public class BCNF {

  /**
   * Implement your algorithm here
   **/
  public static Set<AttributeSet> decompose(AttributeSet attributeSet,
          Set<FunctionalDependency> functionalDependencies) {
	  Set<AttributeSet> result = new HashSet<AttributeSet>();
	  decomposeHelper(attributeSet, functionalDependencies, result);
	  return result;
  }
	
  public static void decomposeHelper(AttributeSet attributeSet,
        Set<FunctionalDependency> functionalDependencies, Set<AttributeSet> result) {
	int size = attributeSet.size();
	List<Attribute> attributeList = new ArrayList<>();
	
	
	
	for(Iterator<Attribute> a = attributeSet.iterator(); a.hasNext();){
		attributeList.add(a.next());
	}
	
	for(int i = 1; i < Math.pow(2, size); i++){
		AttributeSet temp = new AttributeSet();
		int k = i;
		Iterator<Attribute> a;

		for(a = attributeSet.iterator(); a.hasNext();){
			if(k % 2 == 1){
				temp.addAttribute(a.next());
			}
			k = k / 2;
			if(k == 0) break;
		}

		AttributeSet closure = closure(temp, functionalDependencies);
		closure = intersect(closure, attributeSet);
		if((closure == null && temp == null) || (closure.size() == 0 && temp.size() == 0) || closure.equals(temp) || closure.equals(attributeSet)){
			continue;
		}else{
			AttributeSet nonClosure = new AttributeSet();
			for(int cnt = 0; cnt < attributeList.size(); cnt++){
			}
			for(a = attributeSet.iterator(); a.hasNext();){
				Attribute tempA = a.next();
				if(!closure.contains(tempA) || temp.contains(tempA))	nonClosure.addAttribute(tempA);
			}
			decomposeHelper(closure, functionalDependencies, result);
			decomposeHelper(nonClosure, functionalDependencies, result);
			return;
		}
	}
	result.add(attributeSet);
  }
  

  /**
   * Recommended helper method
   **/
  public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
    // TODO: implement me!
	// initialization
	Hashtable<FunctionalDependency, Integer> count = new Hashtable<>();
	AttributeSet newdep = new AttributeSet(attributeSet); // *****************
	AttributeSet update = new AttributeSet(attributeSet); // *****************
	HashMap<Attribute, List<FunctionalDependency>> fdMap = new HashMap<>(); 
	for (FunctionalDependency fd : functionalDependencies){
		count.put(fd, fd.independent().size());
		//List<FunctionalDependency> fdtemp = new ArrayList();
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
	
	while(!update.equals(null) && update.size() != 0){
		Attribute a = update.iterator().next();
		//update.iterator().remove();
		AttributeSet newUpdate = new AttributeSet();
		for(Iterator<Attribute> it = update.iterator(); it.hasNext();){
			Attribute temp = it.next();
			if(temp != a)	newUpdate.addAttribute(temp);
		}
		update = newUpdate;
		if(fdMap.containsKey(a)){
			for(FunctionalDependency fd : fdMap.get(a)){
				count.put(fd, count.get(fd) - 1);
				if(count.get(fd) == 0){
					AttributeSet add = new AttributeSet();
					for(Iterator<Attribute> it = fd.dependent().iterator(); it.hasNext();){
						Attribute at = it.next();
						if(!newdep.contains(at)){
							add.addAttribute(at);
						}
					}
					union(add, newdep);
					union(add, update);
				}
			}
		}
	}
	  
    return newdep;
  }
  
  private static void union(AttributeSet a, AttributeSet b){
	for(Iterator<Attribute> it = a.iterator(); it.hasNext();){
		Attribute at = it.next();
		if(!b.contains(at)){
			b.addAttribute(at);
		}
	}
  }
  
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
  
}
