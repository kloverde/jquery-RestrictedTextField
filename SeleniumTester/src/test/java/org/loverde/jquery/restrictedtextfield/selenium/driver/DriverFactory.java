/*
 * RestrictedTextField
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * This software is licensed under the 3-clause BSD license.
 *
 * Copyright (c) 2016 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/7
 */

package org.loverde.jquery.restrictedtextfield.selenium.driver;

import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class DriverFactory extends FirefoxDriver {

   /**
    * Firefox keeps breaking Selenium by making changes to things.  You have to set
    * options in the profile in order for WebDriver.get() to work.  It seems to be
    * common wisdom that your Firefox installation should lag behind a few versions
    * if using it with Selenium.
    */
   public static final FirefoxDriver newFirefoxDriver() {
      final FirefoxDriver driver;
      final FirefoxProfile profile = new FirefoxProfile();

      profile.setPreference( "browser.startup.homepage_override.mstone", "ignore" );
      profile.setPreference( "startup.homepage_welcome_url.additional",  "about:blank" );
      //profile.setPreference( "xpinstall.signatures.required", false );
      //profile.setPreference( "toolkit.telemetry.reportingpolicy.firstRun", false );

      driver = new FirefoxDriver( profile );
      driver.setLogLevel( Level.ALL );

      return driver;
   }

   public static final InternetExplorerDriver newIeDriver() {
      final InternetExplorerDriver driver = new InternetExplorerDriver();

      driver.setLogLevel( Level.ALL );

      return driver;
   }

   public static final ChromeDriver newChromeDriver() {
      final ChromeDriver driver = new ChromeDriver();

      driver.setLogLevel( Level.ALL );

      return driver;
   }
}
