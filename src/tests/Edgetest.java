
package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import MydataStructure.edge_data;
import MydataStructure.edges;



public class Edgetest {

@Test
void testConstructor(){
		
	edge_data edge1 = new edges(1, 2, 4, 2);
	
	assertEquals(1, edge1.getSrc());
	assertEquals(2, edge1.getDest());
	assertEquals(4, edge1.getWeight());
	assertEquals(2, edge1.getTag());

	edge1.setInfo("test");
	edge1.setTag(0);
	
	assertEquals("test", edge1.getInfo());
	assertEquals(0, edge1.getTag());
	}
	


}
