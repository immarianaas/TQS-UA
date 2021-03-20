/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * @author ico0
 */
public class SetOfNaturalsTest {
    private SetOfNaturals setA;
    private SetOfNaturals setB;
    private SetOfNaturals setC;
    private SetOfNaturals setD;

    @BeforeEach
    public void setUp() {
        setA = new SetOfNaturals();
        setB = SetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});

        setC = new SetOfNaturals();
        for (int i = 5; i < 50; i++) {
            setC.add(i * 10);
        }
        setD = SetOfNaturals.fromArray(new int[]{30, 40, 50, 60, 10, 20});
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = setD = null;
    }

    @Test
    public void testAddElement() {

        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in set.");
        assertEquals(1, setA.size());

        setB.add(11);
        assertTrue(setB.contains(11), "add: added element not found in set.");
        assertEquals(7, setB.size(), "add: elements count not as expected.");
    }

    @Test
    public void addRepeatedElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            setB.add(10); // repeated
        });
        assertEquals(6, setB.size(), "add: size should be equal");
        assertTrue(setB.contains(10), "contains: item should be in set");
    }

    @Test
    public void arrayWithRepeatedElement() {
        assertThrows(IllegalArgumentException.class, () -> {
            SetOfNaturals.fromArray(new int[] { 1, 1});
        });
        assertThrows(IllegalArgumentException.class, () -> {
            setA.add(new int[] {1,1});
        });
    }

    @Test
    public void startsEmpty() {
        assertEquals(0, setA.size(), "size: set is not empty on construction");
    }

    @Test
    public void addInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            setA.add(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            setA.add(0);
        });
        
    }

    @Test
    public void testAddBadArray() {
        int[] elems = new int[]{10, 20, -30};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));
        assertThrows(IllegalArgumentException.class, () -> SetOfNaturals.fromArray(elems));
        
    }


    @Test
    public void testIntersectForNoIntersection() {
        assertFalse(setA.intersects(setB), "no intersection but was reported as existing");

    }

    @Test
    public void testIntersection() {
        // with equal sets
        assertTrue(setB.intersects(setD));

        // with an empty set
        assertTrue(setB.intersects(setA));
        assertFalse(setA.intersects(setB));

        // "normal" valid case
        SetOfNaturals newset = SetOfNaturals.fromArray(new int[] {10, 20});
        assertTrue(setB.intersects(newset));

        // and the opposite...
        assertFalse(newset.intersects(setB));
    }



}
