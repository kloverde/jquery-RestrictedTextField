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
   STRICT_POSITIVE_FLOAT( "strictPositiveFloat" ),
   STRICT_NEGATIVE_FLOAT( "strictNegativeFloat" ),
   MONEY( "money" ),
   POSITIVE_MONEY( "positiveMoney" ),
   NEGATIVE_MONEY( "negativeMoney" ),
   ACCOUNTING_MONEY( "accountingMoney" ),
   NEGATIVE_ACCOUNTING_MONEY( "negativeAccountingMoney" );

   private String type;

   private FieldType( final String type ) {
      this.type = type;
   }

   @Override
   public String toString() {
      return type;
   }
}
