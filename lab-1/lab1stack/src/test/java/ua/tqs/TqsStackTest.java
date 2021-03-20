package ua.tqs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TqsStackTest {

    /*
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
    */

    TqsStack<String> tstack;

    @BeforeEach
    public void setUp() {
        tstack = new TqsStack<String>();
    }

    @Test
    public void isEmptyTest() {
        /*
         * a) A stack is empty on construction. 
         * b) A stack has size 0 on construction.
         */
        TqsStack<String> stack = new TqsStack<String>();
        assertTrue(stack.isEmpty(), "the stack is not empty on construction");
        assertEquals(0, stack.size(), "the stack has size != 0 on construction");
    }

    @Test
    public void pushTest() {
        // TqsStack<String> stack = new TqsStack<String>();
        for (int i = 0; i<5; i++) {
            String s = "teste_" + i;
            tstack.push(s);
            assertTrue(tstack.size() > 0, "after n pushes, the stack size must be > 0");
            assertFalse(tstack.isEmpty(), "after n pushes, the stack cannot be empty");
            assertEquals(i+1, tstack.size(), "after n pushes, the stack size is not n");
        }
    }

    @Test
    public void pushAndPopEqualTest() {
        String s = "abcabc";
        tstack.push(s);
        assertEquals(tstack.pop(),s);
    }

    @Test
    public void peekSizeTest() {
        tstack.push("abc");
        int sizeBeforePeek = tstack.size();
        tstack.peek();
        assertEquals(sizeBeforePeek, tstack.size(), "size after peek is different");
    }

    @Test
    public void popTest() {
        tstack.push("testeteste");
        // ^ so para sabermos que nao esta vazio
        for (int i = 0; i<tstack.size(); i++) {
            tstack.pop();
        }
        assertEquals(0, tstack.size(), "after <size> 'pops', stack size should be 0");
        assertTrue(tstack.isEmpty(), "after <size> 'pops', stack should be empty");
    }

    @Test
    public void poppingEmptyTest() {
        // NoSuchElementException
        TqsStack<String> stack = new TqsStack<String>();
        assertThrows(NoSuchElementException.class, () -> {
            stack.pop();
        });
    }

    @Test
    public void peekingEmptyTest() {
        // NoSuchElementException
        TqsStack<String> stack = new TqsStack<String>();
        assertThrows(NoSuchElementException.class, () -> {
            stack.peek();
        });
    }




}
