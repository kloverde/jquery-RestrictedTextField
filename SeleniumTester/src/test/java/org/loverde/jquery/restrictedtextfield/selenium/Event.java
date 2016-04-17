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


public enum Event {
   VALIDATION_FAILED( "validationFailed" ),
   VALIDATION_SUCCESS( "validationSuccess" );

   private String type;

   private Event( final String type ) {
      this.type = type;
   }

   @Override
   public String toString() {
      return type;
   }
}
