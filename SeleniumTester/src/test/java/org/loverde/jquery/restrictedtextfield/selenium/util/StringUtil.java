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

package org.loverde.jquery.restrictedtextfield.selenium.util;


public class StringUtil {
   public static boolean isNothing( final String s ) {
      return s == null || s.length() < 1;
   }

   public static String trim( final String s ) {
      return s == null ? s : s.trim();
   }
}