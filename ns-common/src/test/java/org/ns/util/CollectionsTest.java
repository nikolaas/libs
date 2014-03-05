package org.ns.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import junit.framework.TestCase;

/**
 *
 * @author stupak
 */
public class CollectionsTest extends TestCase {
    
    public CollectionsTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of iterable method, of class Collections.
     */
    public void testList() {
        System.out.println("list");
        Vector<String> vector = new Vector<>(Arrays.asList("one", "two", "three"));
        for ( String e : Collections.list(vector.elements()) ) {
            vector.remove(e);
        }
        assertEquals(vector.isEmpty(), true);
    }

    public void testIterator() {
        System.out.println("arrayIterator");
        String[] array = new String[]{"one", "two", "three"};
        Iterator<String> iter = Collections.iterator(array);
        while ( iter.hasNext() ) {
            String e = iter.next();
            System.out.println(e);
        }
    }
}
