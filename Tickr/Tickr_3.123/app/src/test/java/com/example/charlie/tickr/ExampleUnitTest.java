package com.example.charlie.tickr;

import org.json.JSONException;
import org.junit.Test;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    MainActivity testMain = new MainActivity();
    @Test
    public void testGoogleLogin() {
        assertThat(testMain.loginResult, is(true));
    }




    Stocks testStock = new Stocks();
    @Test
    public void testStockResult() throws IOException, JSONException {

        //Can input any stock so we are using Microsoft as an example (MSFT)
        // If there is a return such that it is greater than 0.0 then that
        // means that the API was accesed and the value returned was
        // gerater than 0.0, also since stockPrice is initially initiallized as a -1

        assertThat(testStock.getStockPrice("MSFT") > 0.0, is(true));

//        if (testStock.getStockPrice("MSFT") > 0.0) {
//            return true;
//        } else {
//            return false;
//        }
    }

}