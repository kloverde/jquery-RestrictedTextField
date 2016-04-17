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
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.InputVerifier;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.loverde.jquery.restrictedtextfield.selenium.Characters;
import org.loverde.jquery.restrictedtextfield.selenium.Event;
import org.loverde.jquery.restrictedtextfield.selenium.FieldType;
import org.loverde.jquery.restrictedtextfield.selenium.driver.DriverFactory;
import org.loverde.jquery.restrictedtextfield.selenium.util.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.runners.Parameterized;


@RunWith( Parameterized.class )
public abstract class AbstractTest {

   // Begin parameters

   private FieldType fieldType;

   private String testName,
                  input,
                  expectedValue;

   private boolean ignoreInvalidInput;

   private Event expectedEventOnBlur;

   // End parameters

   private static Properties props;

   private static final String PROPERTIES_FILENAME = "gradle.properties";

   public static final String APP_PROP_IE_DRIVER_PATH        = "ieDriverPath",
                              APP_PROP_CHROME_DRIVER_PATH    = "chromeDriverPath",
                              APP_PROP_URL                   = "url",

                              SYSTEM_PROP_IE_DRIVER_PATH     = "webdriver.ie.driver",
                              SYSTEM_PROP_CHROME_DRIVER_PATH = "webdriver.chrome.driver";

   private static String iePath,
                         chromePath;

   private static WebDriver driver;

   private WebElement field,
                      body;

   private static Class<?> lastClass;
   private static File logDirectory;

   public static final List<Object[]> data;


   @Parameters( name = "{1}" )
   public static Collection<Object[]> data() {
      return data;
   }

   static {
      // [x][0] : Field type
      // [x][1] : Test name
      // [x][2] : Flag for ignoring invalid keystrokes (true = enabled)
      // [x][3] : Input
      // [x][4] : Expected final value of text field
      // [x][5] : Expected event triggered on blur
      final Object[][] d = new Object[][] {
         { FieldType.ALPHA,                    "ignore_alpha_all",                   true,  Characters.ALL,  Characters.ALPHA,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHA,              "ignore_upperAlpha_all",              true,  Characters.ALL,  Characters.UPPER_ALPHA,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHA,              "ignore_lowerAlpha_all",              true,  Characters.ALL,  Characters.LOWER_ALPHA,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHA_SPACE,              "ignore_alphaSpace_all",              true,  Characters.ALL,  Characters.ALPHA_SPACE,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHA_SPACE,        "ignore_upperAlphaSpace_all",         true,  Characters.ALL,  Characters.UPPER_ALPHA_SPACE,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHA_SPACE,        "ignore_lowerAlphaSpace_all",         true,  Characters.ALL,  Characters.LOWER_ALPHA_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHANUMERIC,             "ignore_alphanumeric_all",            true,  Characters.ALL,  Characters.ALPHANUMERIC,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHANUMERIC,       "ignore_upperAlphanumeric_all",       true,  Characters.ALL,  Characters.UPPER_ALPHANUMERIC,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHANUMERIC,       "ignore_lowerAlphanumeric_all",       true,  Characters.ALL,  Characters.LOWER_ALPHANUMERIC,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHANUMERIC_SPACE,       "ignore_alphanumericSpace_all",       true,  Characters.ALL,  Characters.ALPHANUMERIC_SPACE,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHANUMERIC_SPACE, "ignore_upperAlphanumericSpace_all",  true,  Characters.ALL,  Characters.UPPER_ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHANUMERIC_SPACE, "ignore_lowerAlphanumericSpace_all",  true,  Characters.ALL,  Characters.LOWER_ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.INT,           "ignore_integer_all",                      true,  Characters.ALL,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.INT,           "ignore_integer_noDoubleZero",             true,  "00",    "0",     Event.VALIDATION_SUCCESS },
         { FieldType.INT,           "ignore_integer_positive_noLeadingZero",   true,  "01",    "0",     Event.VALIDATION_SUCCESS },
         { FieldType.INT,           "ignore_integer_nevative_noNegativeZero",  true,  "-0",    "-",     Event.VALIDATION_FAILED },
         { FieldType.INT,           "ignore_integer_nevative",                 true,  "-123",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.INT,           "ignore_integer_noDoubleNegative",         true,  "--1",   "-1",    Event.VALIDATION_SUCCESS },
         { FieldType.INT,           "ignore_integer_noFloatingPoint",          true,  "1.23",  "123",   Event.VALIDATION_SUCCESS },

         { FieldType.POSITIVE_INT,  "ignore_positiveInt",                  true,  Characters.ALL_EXCEPT_MINUS,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,  "ignore_positiveInt_noDoubleZero",     true,  "00",     "0",    Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,  "ignore_positiveInt_noLeadingZero",    true,  "01",     "0",    Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,  "ignore_positiveInt_noNegative",       true,  "-1",     "1",    Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,  "ignore_positiveInt_noFloatingPoint",  true,  "-1.23",  "123",  Event.VALIDATION_SUCCESS },

         { FieldType.NEGATIVE_INT,  "ignore_negativeInt",                  true,  "-" + Characters.ALPHANUMERIC,  "-" + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,  "ignore_negativeInt_noPositives",      true,  Characters.NUMBERS_END_ZERO,    "0",   Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,  "ignore_negativeInt_noNegativeZero",   true,  "-0",     "-",     Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_INT,  "ignore_negativeInt_noNegativeZero",   true,  "-12-3",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,  "ignore_negativeInt_noNegativeZero",   true,  "-1.23",  "-123",  Event.VALIDATION_SUCCESS },

         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_all",                  true,  Characters.ALL_EXCEPT_MINUS,  Characters.NUMBERS_END_ZERO + ".",  Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_posInt",               true,  "123",           "123",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negInt",               true,  "-123",          "-123",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_posFloat",             true,  "1023.456789",   "1023.456789",   Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negFloat",             true,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_zero",                 true,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingDoubleZero1",   true,  "00",            "0",             Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingDoubleZero2",   true,  "00.0",          "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_extraTrailingZero",    true,  "0.00",          "0.00",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingDoubleZeros3",  true,  "-00.0",         "-0.0",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negativeZero1",        true,  "-0",            "-0",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negativeZero2",        true,  "-0.000000",     "-0.000000",     Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negativeZero3",        true,  "-0.0",          "-0.0",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dash",                 true,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_doubleDash",           true,  "--",            "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashDot",              true,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_endInDot",             true,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace1",    true,  "1-.2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace2",    true,  "1.-2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace3",    true,  "1.-2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace4",    true,  "1.2-",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_doubleDot2",           true,  "1..2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dotsEverywhere",       true,  ".1.2.",         ".12",           Event.VALIDATION_SUCCESS }
      };

      data = Arrays.asList( d );
   }

   public AbstractTest( final FieldType fieldType,
                        final String    testName,
                        final boolean   ignoreInvalidInput,
                        final String    input,
                        final String    expectedValue,
                        final Event     expectedEventOnBlur ) throws Exception {

      final Class<?> clazz = this.getClass();

      if( fieldType == null ) throw new IllegalArgumentException( "You must provide a fieldType" );
      if( StringUtil.isNothing(testName) ) throw new IllegalArgumentException( "You must provide a testName" );
      if( StringUtil.isNothing(input) ) throw new IllegalArgumentException( "You must provide an input" );

      this.fieldType = fieldType;
      this.testName = testName;
      this.ignoreInvalidInput = ignoreInvalidInput;
      this.input = input;
      this.expectedValue = expectedValue;
      this.expectedEventOnBlur = expectedEventOnBlur;

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

   @BeforeClass
   public static void init() throws IOException {
      props = new Properties();
      props.load( new FileInputStream(PROPERTIES_FILENAME) );

      iePath = props.getProperty( APP_PROP_IE_DRIVER_PATH );
      chromePath = props.getProperty( APP_PROP_CHROME_DRIVER_PATH );

      if( logDirectory == null ) {
         logDirectory = createLogDirectory();
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


   @Test
   public void test() {
      initField( testName, fieldType, ignoreInvalidInput );

      keypress( input );

      validatePreBlur();

      resetEventFlags();
      body.click();  // Force blur

      validatePostBlur();
   }

   private void validatePreBlur() {
      final String actualValue = getFieldValue();

      assertEquals( expectedValue, actualValue );

      if( ignoreInvalidInput ) {
         if( !input.equals(actualValue) ) {
            assertTrue( inputIgnoredEventFired() );
         } else {
            assertFalse( inputIgnoredEventFired() );
         }

         assertFalse( validationFailedEventFired() );

         if( actualValue.isEmpty() ) {
            assertFalse( validationSuccessEventFired() );
         } else {
            assertTrue( validationSuccessEventFired() );
         }
      } else {
         assertFalse( inputIgnoredEventFired() );

         if( !input.equalsIgnoreCase(actualValue) ) {
            assertTrue( validationFailedEventFired() );
            assertFalse( validationSuccessEventFired() );
         } else {
            assertFalse( validationFailedEventFired() );
            assertTrue( validationSuccessEventFired() );
         }
      }
   }

   private void validatePostBlur() {
      final String actualValue = getFieldValue();

      assertEquals( expectedValue, actualValue );

      if( !ignoreInvalidInput ) {
         assertFalse( inputIgnoredEventFired() );
      }

      if( expectedEventOnBlur == Event.VALIDATION_FAILED ) {
         assertTrue( validationFailedEventFired() );
         assertFalse( validationSuccessEventFired() );
      } else if( expectedEventOnBlur == Event.VALIDATION_SUCCESS ) {
         assertTrue( validationSuccessEventFired() );
         assertFalse( validationFailedEventFired() );
      }
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
    * The script makes a backup of the current value when it detects an input (before the input has been written to the
    * field), and rolls back to that value if validation fails.  The result is that if a keypress comes in before the
    * script has finished evaluating the previous one, it takes a backup of the field with the not-yet-validated, and
    * thus possibly invalid, data in it.  When it does a rollback, it rolls back to an invalid value.
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

   private void initField( final String testName, final FieldType fieldType, final boolean ignoreInvalidInput ) {
      if( testName == null ) throw new IllegalArgumentException( "testName is null" );
      if( fieldType == null ) throw new IllegalArgumentException( "fieldType is null" );

      final String command = String.format( "initField(\"%s\", \"%s\", %b);", testName, fieldType.toString(), ignoreInvalidInput );
      ((JavascriptExecutor) driver).executeScript( command );
   }

   private void resetEventFlags() {
      ((JavascriptExecutor) driver).executeScript( "resetEventFlags();" );
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
