/*
 * RestrictedTextField v1.1
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * Copyright (c) 2016, Kurtis LoVerde
 * All rights reserved.
 *
 * Donations:  https://paypal.me/KurtisLoVerde/10
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of the copyright holder nor the names of its
 *        contributors may be used to endorse or promote products derived from
 *        this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.loverde.jquery.restrictedtextfield.selenium.tests;

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
