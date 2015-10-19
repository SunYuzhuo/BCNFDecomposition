import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BCNFTest {
  /**
   * Performs a basic test on a simple table.
   * gives input attributes (a,b,c) and functional dependency a->c
   * and expects output (a,c),(b,c) or any reordering
   **/
  @Test
  public void testSimpleBCNF() {
    //construct table
    AttributeSet attrs = new AttributeSet();
    attrs.addAttribute(new Attribute("a"));
    attrs.addAttribute(new Attribute("b"));
    attrs.addAttribute(new Attribute("c"));

    //create functional dependencies
    Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
    AttributeSet ind = new AttributeSet();
    AttributeSet dep = new AttributeSet();
    ind.addAttribute(new Attribute("a"));
    dep.addAttribute(new Attribute("c"));
    FunctionalDependency fd = new FunctionalDependency(ind, dep);
    fds.add(fd);

    //run client code
    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

    //verify output
    assertEquals("Incorrect number of tables", 2, bcnf.size());

    for(AttributeSet as : bcnf) {
      assertEquals("Incorrect number of attributes", 2, as.size());
      assertTrue("Incorrect table", as.contains(new Attribute("a")));
    }

  }
  
  @Test
  public void testComplicatedBCNF() {
    //construct table
    AttributeSet attrs = new AttributeSet();
    attrs.addAttribute(new Attribute("a"));
    attrs.addAttribute(new Attribute("b"));
    attrs.addAttribute(new Attribute("c"));
    attrs.addAttribute(new Attribute("d"));
    attrs.addAttribute(new Attribute("e"));

    //create functional dependencies
    Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
    AttributeSet ind1 = new AttributeSet();
    AttributeSet dep1 = new AttributeSet();
    ind1.addAttribute(new Attribute("a"));
    dep1.addAttribute(new Attribute("b"));
    FunctionalDependency fd1 = new FunctionalDependency(ind1, dep1);
    fds.add(fd1);
    
    AttributeSet ind2 = new AttributeSet();
    AttributeSet dep2 = new AttributeSet();
    ind2.addAttribute(new Attribute("c"));
    dep2.addAttribute(new Attribute("d"));
    FunctionalDependency fd2 = new FunctionalDependency(ind2, dep2);
    fds.add(fd2);

    //run client code
    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

    //verify output
    assertEquals("Incorrect number of tables", 3, bcnf.size());

  }
}