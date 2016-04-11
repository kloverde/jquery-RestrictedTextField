/**
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
   }

   @After
   public void tearDown() {
      ((JavascriptExecutor) driver).executeScript( "tearDown();" );
   }

   @Test
   public void alpha_ignoreInvalid_success() {
      final String val = "C";

      initField( FieldType.ALPHA, true );
      keypress( val );

      assertEquals( val, getFieldValue() );
      assertFalse( inputIgnoredEventFired() );
      assertFalse( validationFailedEventFired() );
      assertTrue( validationSuccessEventFired() );
   }

   private void keypress( final String s ) {
      if( StringUtil.isNothing(s) ) throw new IllegalArgumentException( "keypress has no value" );
      field.sendKeys( s );
   }

   private void initField( final FieldType fieldType, final boolean ignoreInvalidInput ) {
      if( fieldType == null ) throw new IllegalArgumentException( "fieldType is null" );

      final String command = String.format( "initField(\"%s\", %b);", fieldType.toString(), ignoreInvalidInput );
      ((JavascriptExecutor) driver).executeScript( command );
   }

   private String getFieldValue() {
      return field.getAttribute( "value" );
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
