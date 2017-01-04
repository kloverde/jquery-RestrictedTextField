/*
 * RestrictedTextField v1.3
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

(function( $ ) {
   "use strict";

   /**
    * Developers:  Instantiate this to add your own types
    */
   $.fn.RestrictedTextFieldConfig = function() {
      var dest = $.fn.RestrictedTextFieldConfig.customTypes = $.fn.RestrictedTextFieldConfig.customTypes || [];

      /**
       * Adds a custom type
       *
       * @param id           - The identifier you will use to access the new type
       *
       * @param fullRegex    - RegExp object.  This regular expression describes what a valid value looks like when the user is finished
       *                       entering data, NOT what valid input looks like as it's being entered.  If your type is complex, like a
       *                       phone number, for example, you need to provide a value for partialRegex to ensure that the user will be
       *                       able to continue to type into the field.  If partialRegex is not defined or is null, the text field will
       *                       be validated against this fullRegex on every keystroke.  Notwithstanding any of the foregoing, this
       *                       regular expression is also used to validate the text field on blur. 
       *
       * @param partialRegex - RegExp object.  This regular expression describes what a valid value looks like while the user is entering
       *                       it, NOT what valid input looks like after the user has finished.  If you provide a value for this, this
       *                       regular expression will be used to validate the text field on every keystroke.  This parameter may be
       *                       null, but if your type is complex, you will find it necessary to provide a value.
       */
      this.addType = function( id, fullRegex, partialRegex ) {
         _addType( dest, id, fullRegex, partialRegex );
      }
   };

   function _addType( destination, id, fullRegex, partialRegex ) {
      if( id == undefined || id == null ) throw "id is undefined";
      if( !isClassName(id, "String") ) throw "id must be a string";
      if( id.length < 1 ) throw "id is empty";

      if( fullRegex == undefined || fullRegex == null ) throw "fullRegex is undefined";
      if( !isClassName(fullRegex, "RegExp") ) throw "fullRegex must be a RegExp object";

      if( partialRegex != undefined && partialRegex != null && !isClassName(partialRegex, "RegExp") ) throw "partialRegex must be a RegExp object";

      destination[ id ] = { "fullRegex"    : fullRegex,
                            "partialRegex" : partialRegex };
   }

   $.fn.restrictedTextField = function( options ) {
      var settings = $.extend( {
         type                : null,
         preventInvalidInput : true,
         logger              : undefined
      }, options );

      var EVENT_INPUT_IGNORED      = "inputIgnored",
          EVENT_VALIDATION_FAILED  = "validationFailed",
          EVENT_VALIDATION_SUCCESS = "validationSuccess";

      if( this.prop("tagName").toLowerCase() !== "input" ) {
         throw "RestrictedTextField can only be invoked on an input element";
      }

      if( $.fn.restrictedTextField.types === undefined || $.fn.restrictedTextField.types === null ) {
         init();
      }

      function init() {
         var partialNegativeInt   = /^-\d*$/,

             partialPosNegFloat   = /^-?\d*\.?\d*$/,
             partialPositiveFloat = /^\d*\.?\d*$/,
             partialNegativeFloat = /^0*\.?$|^-\d*\.?\d*$/;

         if( $.fn.restrictedTextField.types === undefined || $.fn.restrictedTextField.types === null ) {
            $.fn.restrictedTextField.types = [];
         }

         var dest = $.fn.restrictedTextField.types = $.fn.restrictedTextField.types || [];

         _addType( dest, "alpha",                   /^[a-zA-Z]+$/            , null );
         _addType( dest, "upperAlpha",              /^[A-Z]+$/               , null );
         _addType( dest, "lowerAlpha",              /^[a-z]+$/               , null );
         _addType( dest, "alphaSpace",              /^[a-zA-Z\ ]+$/          , null );
         _addType( dest, "upperAlphaSpace",         /^[A-Z\ ]+$/             , null );
         _addType( dest, "lowerAlphaSpace",         /^[a-z\ ]+$/             , null );
         _addType( dest, "alphanumeric",            /^[a-zA-Z\d]+$/          , null );
         _addType( dest, "upperAlphanumeric",       /^[A-Z\d]+$/             , null );
         _addType( dest, "lowerAlphanumeric",       /^[a-z\d]+$/             , null );
         _addType( dest, "alphanumericSpace",       /^[a-zA-Z\d\ ]+$/        , null );
         _addType( dest, "upperAlphanumericSpace",  /^[A-Z\d\ ]+$/           , null );
         _addType( dest, "lowerAlphanumericSpace",  /^[a-z\d\ ]+$/           , null );
         _addType( dest, "int",                     /^-?\d+$/                , partialNegativeInt );
         _addType( dest, "positiveInt",             /^\d+$/                  , null );
         _addType( dest, "negativeInt",             /^0+$|^-\d+$/            , partialNegativeInt );
         _addType( dest, "strictInt",               /^0+$|^-?0*[1-9]\d*$/    , partialNegativeInt );
         _addType( dest, "strictPositiveInt",       /^\d+$/                  , null );
         _addType( dest, "strictNegativeInt",       /^0+$|^-0*[1-9]\d*$/     , partialNegativeInt );
         _addType( dest, "float",                   /^-?\d*\.?\d+$/          , partialPosNegFloat );
         _addType( dest, "positiveFloat",           /^\d*\.?\d+$/            , partialPositiveFloat );
         _addType( dest, "negativeFloat",           /^-?0+$|^-?0*\.?0+$|^-\d*\.?\d+$/ , partialNegativeFloat );
         _addType( dest, "strictFloat",             /^0*\.0+$|^-?0*\.\d*[1-9]$|^-?\d*\.\d*[1-9]\d*$|^-?0*[1-9]\d*?\d*\.\d+$/  , partialPosNegFloat );
         _addType( dest, "strictPositiveFloat",     /^0*\.0+$|^0*\.\d*[1-9]$|^\d*\.\d*[1-9]\d*$|^\d*[1-9]*\d*\.\d+$/     , partialPositiveFloat );
         _addType( dest, "strictNegativeFloat",     /^0*\.0+$|^-0*\.\d*[1-9]$|^-\d*\.\d*[1-9]\d*$|^-[^0]\d*?\d*\.\d+$/   , partialNegativeFloat );
         _addType( dest, "money",                   /^-?\d+\.\d{2}$/                   ,  /^-?\d*\.?\d{0,2}$/ );
         _addType( dest, "positiveMoney",           /^\d+\.\d{2}$/                     ,  /^\d*\.?\d{0,2}$/ );
         _addType( dest, "negativeMoney",           /^0\.00$|^-\d+\.\d{2}$/            ,  /^0*\.?0?$|^-\d*\.?\d{0,2}$/ );
         _addType( dest, "accountingMoney",         /^\d*\.?\d{2}$|^\(\d*\.?\d{2}\)$/  ,  /^\(?0*\.?0{0,2}$|^[\.\d]$|^\.$|^\d*\.\d{0,2}$|^\(\d*\.?$|^\(\d*\.\d{1,2}?\)?$/ );
         _addType( dest, "negativeAccountingMoney", /^0\.00$|^\(\d*\.?\d{2}\)$/        ,  /^\(?0*\.?0{0,2}$|^\(\d*\.?$|^\((\d*\.\d{1,2}?)\)?$/ );

         // Credit card regular expressions were written according to the formats described by https://en.wikipedia.org/wiki/Payment_card_number as of December 2016.
         // Note:  these formats evolve over time.

         // Prefix: 34, 37; Lengths: 15
         _addType( dest, "americanExpress",  /^3[47]\d{13}$/   ,  /^3[47]?$|^3[47]\d{0,13}$/ );

         // Prefix: 4; Lengths: 13, 16, 19
         _addType( dest, "visa",  /^4(\d{12}|\d{15}|\d{18})$/  ,  /^4\d{0,18}$/ );

         // Prefix: 51-55, 2221-2720; Lengths: 16
         _addType( dest,
                   "masterCard",
                   /^5[12345]\d{14}$|^(222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)\d{12}$/  ,
                   joinRegex(
                              // Prefix 51-55, plus padding out to 16 digits
                              /^5([12345]?|[12345]\d{0,14})$/ ,

                              // Prefix 2221-2720 breakdown:

                                 // 22-27
                                 /^2[2-7]?$/ ,

                                 // 222-272
                                 /^(22[2-9]|2[3-6][0-9]|27[0-2])$/ ,

                                 // 2221-2720 plus padding out to 16 digits
                                 /^(222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)\d{0,12}$/
                   )
         );

         // Prefix: 6011, 622126-622925, 644-649, 65; Lengths: 16, 19
         _addType( dest,
                   "discover",
                   /^6011(\d{12}|\d{15})$|^65(\d{14}|\d{17})$|^64[4-9](\d{13}|\d{16})$|^(62212[6-9]|6221[3-9][0-9]|622[2-8][0-9]{2}|6229[01][0-9]|62292[0-5])(\d{10}|\d{13})$/ ,
                   joinRegex(
                              // Prefix 6011
                              /^60?$|^601?$|^6011?$|^6011\d{0,15}$/ ,

                              // Prefix 622126-622925 breakdown:

                                 // 6 handled by the regex for the '65' prefix

                                 // 62, 622
                                 /^622?$/ ,

                                 // 6221-6229
                                 /^622[1-9]$/ ,

                                 // 62212-62292
                                 /^(6221[2-9]|622[2-8][0-9]|6229[0-2])$/ ,

                                 // 622126-622925 plus padding out to 16 or 19 digits
                                 /^(62212[6-9]|6221[3-9][0-9]|622[2-8][0-9]{2}|6229[01][0-9]|62292[0-5])(\d{0,10}|\d{0,13})$/ ,

                              // Prefix 644-649
                              /^64?$|^64[4-9]\d{0,16}$/ ,

                              // Prefix 65
                              /^6(5?|5\d{0,17})$/
                   )
         );

         // All credit card types
         _addType( dest, "creditCard", joinTypes(true, "americanExpress", "visa", "masterCard", "discover"), joinTypes(false, "americanExpress", "visa", "masterCard", "discover") );

         // Not as stringent as a formal credit card type - just performs Luhn validation.  Use this if you need Luhn validation for
         // a non-credit card number, or if you just don't like the idea of using the credit card types.  After all, you can just
         // leave it to your payment card processor to reject an invalid credit card number, while still using Luhn validation to
         // reject a number that has no possibility of being valid.  This is the safest option, but the choice is yours.
         _addType( dest, "luhnNumber",  /^\d+$/  ,  null );

         _addType( dest, "usZip",        /^\d{5}(\-\d{1,4})?$/  , /^(\d{1,5}|\d{5}\-|\d{5}\-\d{1,4})$/ );
         _addType( dest, "usZip5",       /^\d{5}$/              , /^\d{1,5}$/ );
         _addType( dest, "usZipSuffix",  /^\d{1,4}$/            , null );
      }

      var regexes = $.fn.restrictedTextField.types[ settings.type ];

      if( isNothing(regexes) ) {
         if( $.fn.RestrictedTextFieldConfig.customTypes !== undefined && $.fn.RestrictedTextFieldConfig.customTypes !== null ) {
            regexes = $.fn.RestrictedTextFieldConfig.customTypes[ settings.type ];
         }

         if( isNothing(regexes) ) {
            throw "Invalid type: " + settings.type;
         }
      }

      /**
       * Combines multiple regular expression types into one
       *
       * @param [0] : boolean - true for fullRegex, false for partialRegex
       * @param vararg of regular expression IDs
       *
       * @return A single regular expression satisfying all specified types
       */
      function joinTypes() {
         if( arguments.length < 3 ) throw "Minimum 3 arguments: boolean, id, id";

         var isFullRegex = arguments[0];
         var src = $.fn.restrictedTextField.types;
         var regex = new RegExp( isFullRegex ? src[arguments[1]].fullRegex : src[arguments[1]].partialRegex );

         for( var i = 2; i < arguments.length; i++ ) {
            regex = joinRegex( regex, (isFullRegex ? src[arguments[i]].fullRegex : src[arguments[i]].partialRegex) );
         }

         return regex;
      }

      /**
       * Combines multiple regular expressions into one
       *
       * @param vararg of regular expression objects
       *
       * @return A single regular expression satisfying all provided regular expressions
       */
      function joinRegex() {
         if( arguments.length < 2 ) throw "Minimum 2 RegExp arguments";

         var regex = new RegExp( arguments[0] );

         for( var i = 1; i < arguments.length; i++ ) {
            regex = new RegExp( regex.source + "|" + arguments[i].source );
         }

         return regex;
      }

      return this.each( function() {
         var jqThis = $( this );
         var valueBeforeCommit = "";

         jqThis.on( "keydown paste", function(event) {
            valueBeforeCommit = this.value;
         } );

         jqThis.on( "input", function(event) {
            log( "caught input - new value: " + this.value );

            var jqThis = $( this );

            var passesPartialRegex = false,
                passesFullRegex    = false;

            var responseEvent = settings.preventInvalidInput ? null : EVENT_VALIDATION_FAILED;

            if( this.value.length === 0 ) {
               passesPartialRegex = true;
               passesFullRegex = true;

               if( !settings.preventInvalidInput ) {
                  responseEvent = EVENT_VALIDATION_SUCCESS;
               }
            } else {
               passesFullRegex = regexes.fullRegex.test( this.value );

               if( regexes.partialRegex !== undefined && regexes.partialRegex !== null ) {
                  passesPartialRegex = regexes.partialRegex.test( this.value );
               }

               if( passesFullRegex ) {
                  log( "passes full regex" );
                  
                  if( !settings.preventInvalidInput ) {
                     responseEvent = EVENT_VALIDATION_SUCCESS;
                  }
               } else {
                  log( "fails full regex" );

                  if( passesPartialRegex ) {
                     log( "passes partial regex" );

                     if( !settings.preventInvalidInput ) {
                        responseEvent = EVENT_VALIDATION_SUCCESS;
                     }
                  } else {
                     log( "fails partial regex" );

                     if( !settings.preventInvalidInput ) {
                        responseEvent = EVENT_VALIDATION_FAILED;
                     }
                  }
               }

               if( !passesPartialRegex && !passesFullRegex ) {
                  if( settings.preventInvalidInput ) {
                     responseEvent = EVENT_INPUT_IGNORED;
                     jqThis.val( valueBeforeCommit );
                     log( "reverted field to \"" + valueBeforeCommit + "\"" );
                  }
               }
            }

            if( responseEvent != null ) {
               log( "triggering " + responseEvent + " event " );
               jqThis.trigger( responseEvent );
            }
         } );

         jqThis.on( "blur", function() {
            var type = settings.type;
            var passesFullRegex = false;
            var responseEvent = EVENT_VALIDATION_FAILED;

            if( ((type === "money" || type === "positiveMoney" || type === "negativeMoney") && !isNaN(this.value)) ||
                ((type === "accountingMoney" || type ==="negativeAccountingMoney") && !isNaN(this.value.replace(/^\((.*)\)$/, "$1"))) ) {
               var formatted = formatMoney( this.value );

               passesFullRegex = regexes.fullRegex.test( formatted );

               if( passesFullRegex ) {
                  this.value = formatted;
                  log( "blur:  formatted " + settings.type + " field to " + formatted );
               }
            } else if( type === "americanExpress" || type === "visa" || type === "masterCard" || type === "discover" || type === "creditCard" || type === "luhnNumber" ) {
               passesFullRegex = regexes.fullRegex.test( this.value );

               if( passesFullRegex ) {
                  passesFullRegex = luhnCheck( this.value );
                  log( passesFullRegex ? "passes Luhn check" : "fails Luhn check" );
               }
            } else {
               passesFullRegex = regexes.fullRegex.test( this.value );
            }
            
            if( this.value.length === 0 || passesFullRegex ) {
               responseEvent = EVENT_VALIDATION_SUCCESS;
            }

            log( "triggering " + responseEvent + " event " );

            jqThis.trigger( responseEvent );
         } );

         function formatMoney( value ) {
            if( isNothing(value) ) return value;

            var formatted = value;

            value = trimLeadingZero( value );

            var len         = value.length;
            var sign        = value[0] === "-" ? "-" : "";
            var openParen   = value[0] === "(" ? "(" : "";
            var closeParen  = value[ len - 1 ] === ")" ? ")" : ""; 
            var decimalIdx  = value.indexOf( "." );
            var integerPart = value.substring( openParen === "" && sign === "" ? 0 : 1,
                                               decimalIdx > 0 ? decimalIdx -1 : (closeParen === ")" ? len - 1 : len) );
            var decimalPart = "00";

            if( decimalIdx > -1 ) {
               integerPart = value.substring( openParen === "" && sign === "" ? 0 : 1, decimalIdx );
               decimalPart = value.substring( decimalIdx + 1, closeParen === "" ? len : len - 1 );
            }

            if( (integerPart === "" || parseInt(integerPart) === 0) && (parseInt(decimalPart) === 0) ) {
               formatted = "0.00";
            } else {
               formatted = openParen
                         + sign
                         + (integerPart === "" ? "0" : integerPart)
                         + "."
                         + decimalPart
                         + (decimalPart.length === 1 ? "0" : "")
                         + closeParen;
            }

            return formatted;

            function trimLeadingZero( str ) {
               return str.replace( /^(-?\(?)0*(\d+\.\d+)/, "$1$2" );
            }
         }
      } );

      function luhnCheck( numStr ) {
         var luhnNums = [];
         var sum = 0;
         var checkDigit = 0;

         if( isNaN(numStr) ) throw "Value [" + numStr + "] is not numeric";

         var doubleMe = true;

         for( var i = numStr.length - 2; i >= 0; i-- ) {
            var num = parseInt( numStr[i] );

            if( doubleMe ) {
               var x2 = num * 2;
               luhnNums[i] = x2 > 9 ? x2 - 9 : x2;
            } else {
               luhnNums[i] = num;
            }

            sum += luhnNums[i];
            doubleMe = !doubleMe;
         }

         checkDigit = (sum * 9) % 10;
         sum += checkDigit;

         return sum % 10 === 0 && parseInt( numStr[numStr.length - 1] ) === checkDigit;
      }

      function log( msg ) {
         if( settings.logger && typeof settings.logger === "function" ) {
            settings.logger( "jquery.restrictedtextfield.js:  " + msg );
         }
      }
   };

   function isNothing( value ) {
      return value === undefined || value === null || value.length < 1;
   }

   function isClassName( obj, className ) {
      var name = Object.prototype.toString.call( obj ).slice( 8, -1 );
      return obj !== undefined && obj !== null && name.toUpperCase() === className.toUpperCase();
   }
}( jQuery ));
