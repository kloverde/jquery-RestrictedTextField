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

package org.loverde.jquery.restrictedtextfield.selenium;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.AllTests;


@RunWith( AllTests.class )
public class SeleniumTestSuite {

    public static TestSuite suite() throws FileNotFoundException, IOException {
        final TestSuite suite = new TestSuite();
        final Properties props = new Properties();
        final Set<Class<? extends AbstractTest>> set = new HashSet<Class<? extends AbstractTest>>();

        props.load( new FileInputStream("gradle.properties") );

        for( String s : props.getProperty("browsers").split(",") ) {
           s = s.trim();

           if( s.equalsIgnoreCase("ie") ) {
              set.add( InternetExplorerTest.class );
           } else if( s.equalsIgnoreCase("firefox") ) {
              set.add( FirefoxTest.class );
           } else if( s.equalsIgnoreCase("chrome") ) {
              set.add( ChromeTest.class );
           }
        }

        for( final Class<? extends AbstractTest> c : set ) {
           suite.addTest( new JUnit4TestAdapter(c) );
        }

        return suite;
     }
}
