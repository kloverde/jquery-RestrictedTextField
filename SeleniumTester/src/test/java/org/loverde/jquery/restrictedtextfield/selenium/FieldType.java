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


public enum FieldType {
   ALPHA( "alpha" ),
   UPPER_ALPHA( "upperAlpha" ),
   LOWER_ALPHA( "lowerAlpha" ),
   ALPHA_SPACE( "alphaSpace" ),
   UPPER_ALPHA_SPACE( "upperAlphaSpace" ),
   LOWER_ALPHA_SPACE( "lowerAlphaSpace" ),
   ALPHANUMERIC( "alphanumeric" ),
   UPPER_ALPHANUMERIC( "upperAlphanumeric" ),
   LOWER_ALPHANUMERIC( "lowerAlphanumeric" ),
   ALPHANUMERIC_SPACE( "alphanumericSpace" ),
   UPPER_ALPHANUMERIC_SPACE( "upperAlphanumericSpace" ),
   LOWER_ALPHANUMERIC_SPACE( "lowerAlphanumericSpace" ),
   INT( "int" ),
   POSITIVE_INT( "positiveInt" ),
   NEGATIVE_INT( "negativeInt" ),
   STRICT_INT( "strictInt" ),
   STRICT_POSITIVE_INT( "strictPositiveInt" ),
   STRICT_NEGATIVE_INT( "strictNegativeInt" ),
   FLOAT( "float" ),
   POSITIVE_FLOAT( "positiveFloat" ),
   NEGATIVE_FLOAT( "negativeFloat" ),
   STRICT_FLOAT( "strictFloat" ),
   MONEY( "money" ),
   POSITIVE_MONEY( "positiveMoney" ),
   NEGATIVE_MONEY( "negativeMoney" );

   private String type;

   private FieldType( final String type ) {
      this.type = type;
   }

   @Override
   public String toString() {
      return type;
   }
}
