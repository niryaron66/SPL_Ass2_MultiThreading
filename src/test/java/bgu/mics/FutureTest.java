package bgu.mics;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest extends TestCase {

    private static Future<String> future;

    @Before
    public void setUp() throws Exception {
        future = new Future<String>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGet() {
        assertFalse(future.isDone());
        future.resolve("test");
        assertTrue(future.isDone());
        assertEquals("test", future.get());
    }

    @Test
    public void testResolve() {
        String str = "Somthing";
        Thread t1 = new Thread(() -> {
            future.get();
        });

        Thread t2 = new Thread(() -> {
            future.resolve(str);
        });
        t1.start();
        t2.start();
        assertEquals(future.get(), str);
    }

    @Test
    public void testIsDone() {
        assertFalse(future.isDone());
        future.resolve("result");
        assertTrue(future.isDone());
    }

    @Test
    public void testGetWithTime() { //Todo: need to change!
        String str = "Somthing";
        Thread t1 = new Thread(() -> {
            future.get(1000, TimeUnit.MILLISECONDS);
        });

        Thread t2 = new Thread(() -> {
            try {
                assertFalse(future.isDone());
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve(str);
            try {
                Thread.sleep(2000);
                assertTrue(future.isDone());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        Thread t3 = new Thread(() -> {
            future.get(2000, TimeUnit.MILLISECONDS);
        });
        t1.start();
        t2.start();
        t3.start();



    }
}