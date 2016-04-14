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

package org.loverde.jquery.restrictedtextfield.selenium;


/**
 * This class' purpose is to re-run {@linkplain AbstractTest} under
 * a unique name so that in a multi-browser test scenario, you know
 * which browser a test failed in.
 */
public class InternetExplorerTest extends AbstractTest {

   public InternetExplorerTest() throws Exception {
      super();
   }
}
