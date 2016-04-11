/*
 * RestrictedTextField v1.1
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * This software is licensed under the 3-clause BSD license.
 *
 * Copyright (c) 2016 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/7
 */

package org.loverde.jquery.restrictedtextfield.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loverde.jquery.restrictedtextfield.selenium.driver.DriverFactory;
import org.loverde.jquery.restrictedtextfield.selenium.util.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class SeleniumJUnitClient {

   private static Properties props;

   private static final String PROPERTIES_FILENAME = "application.properties";

   public static final String APP_PROP_KEY_IE_DRIVER_PATH    = "ieDriverPath",
                              APP_PROP_KEY_URL               = "url",
                              SYSTEM_PROP_KEY_IE_DRIVER_PATH = "webdriver.ie.driver";

   private static WebDriver driver;
   private WebElement field;
   private WebElement body;


   @BeforeClass
   public static void init() {
      final File exe;

      props = new Properties();

      try {
         props.load( new FileInputStream(PROPERTIES_FILENAME) );
      } catch( final IOException e ) {
         e.printStackTrace();
         System.exit( -1 );
      }

      exe = new File( props.getProperty(APP_PROP_KEY_IE_DRIVER_PATH) );
      System.setProperty( SYSTEM_PROP_KEY_IE_DRIVER_PATH, exe.getAbsolutePath() );

      driver = DriverFactory.newIeDriver();
      driver.get( props.getProperty(APP_PROP_KEY_URL) );
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

      // Unlike the @Tests immediately below, we don't check validationInputIgnoredEventFired()
      // for false because the page is using global variables to keep track of which events fired.
      // The inputIgnoredEventFired flag is still going to be 'true', even though the event
      // doesn't actually fire on blur.

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

   private void keypress( final String s ) {
      if( StringUtil.isNothing(s) ) throw new IllegalArgumentException( "keypress has no value" );
      field.sendKeys( s );
   }

   private void initField( final String testName, final FieldType fieldType, final boolean ignoreInvalidInput ) {
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
}
