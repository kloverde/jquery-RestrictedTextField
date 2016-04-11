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

package org.loverde.jquery.restrictedtextfield.selenium.driver;

import java.util.logging.Level;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class DriverFactory extends FirefoxDriver {

   /**
    * Firefox keep breaking Selenium by making changes to things.
    * You have to disable stuff to get WebDriver.get() to work.
    * Oh, and this solution stopped working, too, because they
    * made another change.  If you're using Firefox, good luck.
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
}
