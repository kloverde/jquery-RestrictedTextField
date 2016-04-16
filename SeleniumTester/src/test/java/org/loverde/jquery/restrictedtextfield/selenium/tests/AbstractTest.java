/*
 * RestrictedTextField
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * This software is licensed under the 3-clause BSD license.
 *
 * Copyright (c) 2016 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/10
 */

package org.loverde.jquery.restrictedtextfield.selenium.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.loverde.jquery.restrictedtextfield.selenium.Characters;
import org.loverde.jquery.restrictedtextfield.selenium.FieldType;
import org.loverde.jquery.restrictedtextfield.selenium.driver.DriverFactory;
import org.loverde.jquery.restrictedtextfield.selenium.util.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public abstract class AbstractTest {

   private static Properties props;

   private static final String PROPERTIES_FILENAME = "gradle.properties";

   public static final String APP_PROP_IE_DRIVER_PATH        = "ieDriverPath",
                              APP_PROP_CHROME_DRIVER_PATH    = "chromeDriverPath",
                              APP_PROP_URL                   = "url",

                              SYSTEM_PROP_IE_DRIVER_PATH     = "webdriver.ie.driver",
                              SYSTEM_PROP_CHROME_DRIVER_PATH = "webdriver.chrome.driver";

   private static WebDriver driver;
   private WebElement field;
   private WebElement body;

   private static Class<?> lastClass;
   private static File logDirectory;

   @BeforeClass
   public static void init() throws IOException {
      props = new Properties();
      props.load( new FileInputStream(PROPERTIES_FILENAME) );

      if( logDirectory == null ) {
         logDirectory = createLogDirectory();
      }
   }

   public AbstractTest() throws Exception {
      final String iePath = props.getProperty( APP_PROP_IE_DRIVER_PATH ),
                   chromePath = props.getProperty( APP_PROP_CHROME_DRIVER_PATH );

      final Class<?> clazz = this.getClass();

      if( !StringUtil.isNothing(iePath) ) {
         System.setProperty( SYSTEM_PROP_IE_DRIVER_PATH, new File(iePath).getAbsolutePath() );
      }

      if( !StringUtil.isNothing(chromePath) ) {
         System.setProperty( SYSTEM_PROP_CHROME_DRIVER_PATH, new File(chromePath).getAbsolutePath() );
      }

      if( clazz != lastClass ) {
         driver = null;

         if( clazz == InternetExplorerTest.class ) {
            driver = DriverFactory.newIeDriver();
         } else if( clazz == FirefoxTest.class ) {
            driver = DriverFactory.newFirefoxDriver();
         } else if( clazz == ChromeTest.class ) {
            driver = DriverFactory.newChromeDriver();
         }

         if( driver == null ) {
            throw new Exception( "No driver for " + this.getClass() );
         }

         driver.get( props.getProperty(APP_PROP_URL) );
         lastClass = clazz;
      }
   }

   @AfterClass
   public static void stopSelenium() throws IOException {
      writeLog();

      if( driver != null ) {
         driver.quit();
      }
   }

   @Before
   public void setUp() {
      ((JavascriptExecutor) driver).executeScript( "setUp();" );
      field = driver.findElement( By.id("field") );
      body = driver.findElement( By.tagName("body") );
   }

   @After
   public void tearDown() {
      ((JavascriptExecutor) driver).executeScript( "tearDown();" );
   }

   // The first three tests verify the firing of events based on input and configuration.

   @Test
   public void invalidInput_ignoreInvalidInputTrue_inputIgnoredEventFires() {
      final String val = "/";

      initField( "invalidInput_ignoreInvalidInputTrue_inputIgnoredEventFires", FieldType.ALPHA, true );
      keypress( val );

      assertTrue( StringUtil.isNothing(getFieldValue()) );
      assertTrue( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );
      assertFalse( validationSuccessEventFired() );

      body.click();

      assertTrue( StringUtil.isNothing(getFieldValue()) );

      // We don't check validationInputIgnoredEventFired() for false because the page is using global
      // variables to keep track of which events fired.  The inputIgnoredEventFired flag is still
      // going to be 'true', even though the event doesn't actually fire on blur.

      assertFalse( validationFailedEventFired() );
      assertTrue( validationSuccessEventFired() );
   }

   @Test
   public void invalidInput_ignoreInvalidInputFalse_validationFailedEventFires() {
      final String val = "/";

      initField( "invalidInput_ignoreInvalidInputFalse_validationFailedEventFires", FieldType.ALPHA, false );
      keypress( val );

      assertEquals( val, getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
      assertFalse( validationSuccessEventFired() );

      body.click();

      assertEquals( val, getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
      assertFalse( validationSuccessEventFired() );
   }

   @Test
   public void validInput_ignoreInvalidInputTrue_validationSuccessEventFires() {
      final String val = "C";

      initField( "validInput_ignoreInvalidInputTrue_validationSuccessEventFires", FieldType.ALPHA, true );
      keypress( val );

      assertEquals( val, getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );
      assertTrue( validationSuccessEventFired() );

      body.click();

      assertEquals( val, getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );
      assertTrue( validationSuccessEventFired() );
   }

   // The remaining tests verify that field types accept their full character sets and reject invalid characters.
   // To be practical, inputs are limited to characters found on a U.S. keyboard.  Testing against every Unicode
   // character is NOT practical.

   // alpha
/*
   @Test
   public void alpha() {
      initField( FieldType.ALPHA, true );
      keypress( Characters.ALL );
      assertEquals( Characters.ALPHA, getFieldValue() );
   }

   @Test
   public void upperAlpha() {
      initField( FieldType.UPPER_ALPHA, true );
      keypress( Characters.ALL );
      assertEquals( Characters.UPPER_ALPHA, getFieldValue() );
   }

   @Test
   public void lowerAlpha() {
      initField( FieldType.LOWER_ALPHA, true );
      keypress( Characters.ALL );
      assertEquals( Characters.LOWER_ALPHA, getFieldValue() );
   }

   @Test
   public void alphaSpace() {
      initField( FieldType.ALPHA_SPACE, true );
      keypress( Characters.ALL );
      assertEquals( Characters.ALPHA_SPACE, getFieldValue() );
   }

   @Test
   public void upperAlphaSpace() {
      initField( FieldType.UPPER_ALPHA_SPACE, true );
      keypress( Characters.ALL );
      assertEquals( Characters.UPPER_ALPHA_SPACE, getFieldValue() );
   }

   @Test
   public void lowerAlphaSpace() {
      initField( FieldType.LOWER_ALPHA_SPACE, true );
      keypress( Characters.ALL );
      assertEquals( Characters.LOWER_ALPHA_SPACE, getFieldValue() );
   }

   // alphanumeric

   @Test
   public void alphanumeric() {
      initField( FieldType.ALPHANUMERIC, true );
      keypress( Characters.ALL );
      assertEquals( Characters.ALPHANUMERIC, getFieldValue() );
   }

   @Test
   public void upperAlphanumeric() {
      initField( FieldType.UPPER_ALPHANUMERIC, true );
      keypress( Characters.ALL );
      assertEquals( Characters.UPPER_ALPHANUMERIC, getFieldValue() );
   }

   @Test
   public void lowerAlphanumeric() {
      initField( FieldType.LOWER_ALPHANUMERIC, true );
      keypress( Characters.ALL );
      assertEquals( Characters.LOWER_ALPHANUMERIC, getFieldValue() );
   }

   @Test
   public void alphanumericSpace() {
      initField( FieldType.ALPHANUMERIC_SPACE, true );
      keypress( Characters.ALL );
      assertEquals( Characters.ALPHANUMERIC_SPACE, getFieldValue() );
   }

   @Test
   public void upperAlphanumericSpace() {
      initField( FieldType.UPPER_ALPHANUMERIC_SPACE, true );
      keypress( Characters.ALL );
      assertEquals( Characters.UPPER_ALPHANUMERIC_SPACE, getFieldValue() );
   }

   @Test
   public void lowerAlphanumericSpace() {
      initField( FieldType.LOWER_ALPHANUMERIC_SPACE, true );
      keypress( Characters.ALL );
      assertEquals( Characters.LOWER_ALPHANUMERIC_SPACE, getFieldValue() );
   }

   // int

   @Test
   public void integer() {
      initField( "integer", FieldType.INT, true );
      keypress( Characters.ALPHANUMERIC );
      assertEquals( Characters.NUMBERS_END_ZERO, getFieldValue() );
   }

   @Test
   public void integer_noDoubleZero() {
      initField( "integer_noDoubleZero", FieldType.INT, true );
      keypress( "00" );
      assertEquals( "0", getFieldValue() );
   }

   @Test
   public void integer_positive_noLeadingZeroBeforeNonZeroNumber() {
      initField( "integer_positive_noLeadingZeroBeforeNonZeroNumber", FieldType.INT, true );
      keypress( "01" );
      assertEquals( "0", getFieldValue() );
   }

   @Test
   public void integer_nevative_noNegativeZero() {
      initField( "integer_nevative_noNegativeZero", FieldType.INT, true );
      keypress( "-0" );
      assertEquals( "-", getFieldValue() );
   }

   @Test
   public void integer_negative() {
      initField( "integer_negative", FieldType.INT, true );
      keypress( "-123" );
      assertEquals( "-123", getFieldValue() );
   }

   @Test
   public void integer_noDoubleNegative() {
      initField( "integer_noDoubleNegative", FieldType.INT, true );
      keypress( "--1" );
      assertEquals( "-1", getFieldValue() );
   }

   @Test
   public void integer_noFloatingPoint() {
      initField( "integer_noFloatingPoint", FieldType.INT, true );
      keypress( "1.23" );
      assertEquals( "123", getFieldValue() );
   }

   // positiveInt

   @Test
   public void positiveInt() {
      initField( "positiveInt", FieldType.POSITIVE_INT, true );
      keypress( Characters.ALPHANUMERIC );
      assertEquals( Characters.NUMBERS_END_ZERO, getFieldValue() );
   }

   @Test
   public void positiveInt_noDoubleZero() {
      initField( "positiveInt_noDoubleZero", FieldType.POSITIVE_INT, true );
      keypress( "00" );
      assertEquals( "0", getFieldValue() );
   }

   @Test
   public void positiveInt_noLeadingZeroBeforeNonZeroNumber() {
      initField( "postiveInt_noLeadingZeroBeforeNonZeroNumber", FieldType.POSITIVE_INT, true );
      keypress( "01" );
      assertEquals( "0", getFieldValue() );
   }

   @Test
   public void positiveInt_noNegative() {
      initField( "positiveInt_noNegative", FieldType.POSITIVE_INT, true );
      keypress( "-1" );
      assertEquals( "1", getFieldValue() );
   }

   @Test
   public void positiveInt_noFloatingPoint() {
      initField( "positiveInt_noFloatingPoint", FieldType.POSITIVE_INT, true );
      keypress( "1.23" );
      assertEquals( "123", getFieldValue() );
   }

   // negativeInt

   @Test
   public void negativeInt() {
      initField( "negativeInt", FieldType.NEGATIVE_INT, true );
      keypress( "-" + Characters.ALPHANUMERIC );
      assertEquals( "-" + Characters.NUMBERS_END_ZERO, getFieldValue() );
   }

   @Test
   public void negativeInt_noPositives() {
      initField( "negativeInt_noPositives", FieldType.NEGATIVE_INT, true );
      keypress( Characters.NUMBERS_END_ZERO );
      assertEquals( "0", getFieldValue() );
   }

   @Test
   public void negativeInt_noNegativeZero() {
      initField( "negativeInt_noDoubleZero", FieldType.NEGATIVE_INT, true );
      keypress( "-0" );
      assertEquals( "-", getFieldValue() );
   }

   @Test
   public void negativeInt_noNegativeSignAfterDigit() {
      initField( "negativeInt_noNegativeSignAfterDigit", FieldType.NEGATIVE_INT, true );
      keypress( "-12-3" );
      assertEquals( "-123", getFieldValue() );
   }

   @Test
   public void negativeInt_noFloatingPoint() {
      initField( "negativeInt_noFloatingPoint", FieldType.NEGATIVE_INT, true );
      keypress( "-1.23" );
      assertEquals( "-123", getFieldValue() );
   }
*/
   // float

   @Test
   public void float_positive_intIsValid() {
      initField( "float_positive_intIsValid", FieldType.FLOAT, true );
      keypress( Characters.NUMBERS_END_ZERO );
      assertEquals( Characters.NUMBERS_END_ZERO, getFieldValue() );
   }

   @Test
   public void float_positive() {
      initField( "float_positive", FieldType.FLOAT, true );
      // number like 123.456
      keypress( Characters.ALPHANUMERIC_SPACE + "." + Characters.NUMBERS_END_ZERO );
      assertEquals( Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO, getFieldValue() );
   }

   @Test
   public void float_positive_zero() {
      initField( "float_positive_zero", FieldType.FLOAT, true );
      keypress( "0" );
      assertEquals( "0", getFieldValue() );
   }

   @Test
   public void float_positive_noLeadingDoubleZero() {
      initField( "float_positive_noLeadingDoubleZero", FieldType.FLOAT, true );
      keypress( "00.0" );
      assertEquals( "0.0", getFieldValue() );
   }

   @Test
   public void float_positive_bunchOfZeros() {
      initField( "float_positive_bunchOfZeros", FieldType.FLOAT, true );
      keypress( "0.00000" );
      assertEquals( "0.00000", getFieldValue() );
   }

   @Test
   public void float_negative() {
      final String val = "-" + Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO;

      initField( "float_negative", FieldType.FLOAT, true );
      keypress( val );
      assertEquals( val, getFieldValue() );
   }

   @Test
   public void float_negative_intIsValid() {
      initField( "float_negative_intIsValid", FieldType.FLOAT, true );
      // number like -123.456
      keypress( "-" + Characters.NUMBERS_END_ZERO );
      assertEquals( "-" + Characters.NUMBERS_END_ZERO, getFieldValue() );
   }

   @Test
   public void float_negative_noLeadingDoubleZero() {
      initField( "float_negative_noLeadingDoubleZero", FieldType.FLOAT, true );
      keypress( "-00.0" );
      assertEquals( "-0.0", getFieldValue() );
   }

   @Test
   public void float_negative_bunchOfZeros() {
      initField( "float_negative_bunchOfZeros", FieldType.FLOAT, true );
      keypress( "-0.00000" );
      assertEquals( "-0.00000", getFieldValue() );
   }

   @Test
   public void float_inputInProgress_negativeSign() {
      initField( "float_inputInProgress_negativeSign", FieldType.FLOAT, true );
      keypress( "-" );

      // While input is in progress, negative sign is valid because it might not be the end of the input

      assertEquals( "-", getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );

      body.click();

      // Once the input is completed, negative sign is invalid

      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
   }

   @Test
   public void float_inputInProgress_negativeSignZero() {
      initField( "float_inputInProgress_negativeSignZero", FieldType.FLOAT, true );
      keypress( "-0" );
      assertEquals( "-0", getFieldValue() );

      // While input is in progress, -0 is valid because it might not be the end of the input

      assertEquals( "-0", getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );

      body.click();

      // Once the input is completed, -0 is invalid

      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
   }

   @Test
   public void float_inputInProgress_negativeSignDot() {
      initField( "float_inputInProgress_negativeSignDot", FieldType.FLOAT, true );
      keypress( "-." );

      // While input is in progress, -. is valid because it might not be the end of the input

      assertEquals( "-.", getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );

      body.click();

      // Once the input is completed, -. is invalid

      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
   }

   @Test
   public void float_inputInProgress_negativeSignZeroDot() {
      initField( "float_inputInProgress_negativeSignZeroDot", FieldType.FLOAT, true );
      keypress( "-0." );

      // While input is in progress, -0. is valid because it might not be the end of the input

      assertEquals( "-0.", getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );

      body.click();

      // Once the input is completed, -0. is invalid

      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
   }

   @Test
   public void float_inputInProgress_negativeSignZeroDotZero() {
      initField( "float_inputInProgress_negativeSignZeroDotZero", FieldType.FLOAT, true );
      keypress( "-0.0" );

      // While input is in progress, -0.0 is valid because it might not be the end of the input

      assertEquals( "-0.0", getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );

      body.click();

      // Once the input is completed, -0.0 is invalid

      assertFalse( inputIgnoredEventFired() );
      assertTrue( validationFailedEventFired() );
   }

   @Test
   public void float_negative_noDoubleNegative() {
      initField( "float_noDoubleNegative", FieldType.FLOAT, true );
      keypress( "--" );
      assertEquals( "-", getFieldValue() );
   }

   @Test
   public void float_negative_noNegativeSignAfterFirstChar() {
      initField( "float_negative_noNegativeSignAfterFirstChar", FieldType.FLOAT, true );
      keypress( "3-" );
      assertEquals( "3", getFieldValue() );
   }

   // Helpers below this line

   /**
    * <p>
    * Rather than sending the entire string into sendKeys, we have to call it one character at a time to prevent the script
    * from accepting invalid input.  By design, jquery-restrictedtextfield.js allows a character into the field before
    * validating it and, if necessary, rolling it back.  The script does this by listening for the "input" event, which is
    * after everything has happened.  The correct way of doing this would have been to listen to keydown or keypress and
    * call event.preventDefault() if validation failed.  This couldn't be done, since event.keyCode cannot reliably be
    * translated to an ASCII character.  Browsers sometimes report a proprietary value for it.  The only reliable way to
    * validate input was to wait until the browser had translated its sometimes-proprietary value and written it into the
    * field.
    * </p>
    *
    * <p>
    * The script makes a backup of the current value when it detects an input (and before it has been inserted into the
    * field), and rolls back to that value if validation fails.  The result is that if a keypress comes in before the
    * script has finished evaluating the previous one, it takes a backup of the field with the not-yet-validated, and
    * thus possibly invalid, data in it.
    * </p>
    *
    * <p>
    * It should be noted that this condition cannot be triggered by mashing keys or by holding a key down.  This only
    * happens under the artificial scenario of Selenium apparently slamming input into the field without a delay between
    * the keystrokes.  Additionally, this is limited to IE.  This issue should never be observed in the real world.
    * </p>
    */
   private void keypress( final String s ) {
      if( StringUtil.isNothing(s) ) throw new IllegalArgumentException( "keypress has no value" );

      final int len = s.length();

      for( int i = 0; i < len; i++ ) {
         field.sendKeys( Character.toString(s.charAt(i)) );
      }
   }

   private void initField( final FieldType fieldType, final boolean ignoreInvalidInput ) {
      if( fieldType == null ) throw new IllegalArgumentException( "fieldType is null" );

      final String command = String.format( "initField(\"%s\", \"%s\", %b);", fieldType.toString(), fieldType.toString(), ignoreInvalidInput );
      ((JavascriptExecutor) driver).executeScript( command );
   }

   private void initField( final String testName, final FieldType fieldType, final boolean ignoreInvalidInput ) {
      if( testName == null ) throw new IllegalArgumentException( "testName is null" );
      if( fieldType == null ) throw new IllegalArgumentException( "fieldType is null" );

      final String command = String.format( "initField(\"%s\", \"%s\", %b);", testName, fieldType.toString(), ignoreInvalidInput );
      ((JavascriptExecutor) driver).executeScript( command );
   }

   private String getFieldValue() {
      final String value = field.getAttribute( "value" );
      return value != null ? value : "";
   }

   private boolean inputIgnoredEventFired() {
      return (Boolean) ((JavascriptExecutor) driver).executeScript( "return didInputIgnoredEventFire();" );
   }

   private boolean validationFailedEventFired() {
      return (Boolean) ((JavascriptExecutor) driver).executeScript( "return didValidationFailedEventFire();" );
   }

   private boolean validationSuccessEventFired() {
      return (Boolean) ((JavascriptExecutor) driver).executeScript( "return didValidationSuccessEventFire();" );
   }

   private static File createLogDirectory() throws IOException {
      final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd__HH-mm-ss" );
      final String path = "build/reports/tests/browser_logs/" + dateFormat.format( new Date() );
      final File dir = new File( path );

      if( !dir.mkdirs() ) {
         throw new IOException( "Could not create log directory " + path );
      }

      return dir;
   }

   private static void writeLog() throws IOException {
      final BufferedWriter writer = new BufferedWriter( new FileWriter(logDirectory.getPath() + "/" + lastClass.getSimpleName() + ".log") );

      try {
      writer.write( driver.findElement(By.id("log")).getText() );
      writer.flush();
      } finally {
         writer.close();
      }
   }
}
