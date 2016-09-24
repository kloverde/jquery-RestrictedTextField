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

   $.fn.restrictedTextField = function( options ) {
      var settings = $.extend( {
         type                : null,
         preventInvalidInput : true,
         logger              : undefined
      }, options );

      var EVENT_INPUT_IGNORED      = "inputIgnored",
          EVENT_VALIDATION_FAILED  = "validationFailed",
          EVENT_VALIDATION_SUCCESS = "validationSuccess";

      if( $.fn.restrictedTextField.types === undefined || $.fn.restrictedTextField.types === null ) {
         init();
      }

      function init() {
         // This first block of variables is a collection of regular expressions for incomplete input.

         var negativeInt   = /^-$/,
             posNegFloat   = /^-?0?\.?$|^-?\d*\.\d*$/,
             positiveFloat = /^\d*\.\d*$/,
             negativeFloat = /^-?0*\.?$|^-\.?$|^-\d*\.$/;

         if( $.fn.restrictedTextField.types === undefined || $.fn.restrictedTextField.types === null ) {
            $.fn.restrictedTextField.types = [];
         }

         var dest = $.fn.restrictedTextField.types = $.fn.restrictedTextField.types || [];

         _addType( dest, "alpha",                   /^[a-zA-Z]*$/            , null );  // Uppercase and lowercase letters
         _addType( dest, "upperAlpha",              /^[A-Z]*$/               , null );  // Uppercase letters
         _addType( dest, "lowerAlpha",              /^[a-z]*$/               , null );  // Lowercase letters
         _addType( dest, "alphaSpace",              /^[a-zA-Z\ ]*$/          , null );  // Uppercase letters, lowercase letters, space
         _addType( dest, "upperAlphaSpace",         /^[A-Z\ ]*$/             , null );  // Uppercase letters and space
         _addType( dest, "lowerAlphaSpace",         /^[a-z\ ]*$/             , null );  // Lowercase letters and space
         _addType( dest, "alphanumeric",            /^[a-zA-Z\d]*$/          , null );  // Uppercase letters, lowercase letters, digits 0-9
         _addType( dest, "upperAlphanumeric",       /^[A-Z\d]*$/             , null );  // Uppercase letters and digits 0-9
         _addType( dest, "lowerAlphanumeric",       /^[a-z\d]*$/             , null );  // Lowercase letters and digits 0-9
         _addType( dest, "alphanumericSpace",       /^[a-zA-Z\d\ ]*$/        , null );  // Uppercase letters, lowercase letters, digits 0-9, space
         _addType( dest, "upperAlphanumericSpace",  /^[A-Z\d\ ]*$/           , null );  // Uppercase letters, digits 0-9, space
         _addType( dest, "lowerAlphanumericSpace",  /^[a-z\d\ ]*$/           , null );  // Lowercase letters, digits 0-9, space
         _addType( dest, "int",                     /^-?\d*$/                , negativeInt );  // 0, positive integers, negative integers
         _addType( dest, "positiveInt",             /^\d*$/                  , null );         // 0, positive integers
         _addType( dest, "negativeInt",             /^0*$|^-\d*$/            , negativeInt );  // 0, negative integers
         _addType( dest, "strictInt",               /^0$|^-?[1-9]\d*$/       , negativeInt );
         _addType( dest, "strictPositiveInt",       /^0$|^[1-9]\d*$/         , null );
         _addType( dest, "strictNegativeInt",       /^0$|^-[1-9]\d*$/        , negativeInt );
         _addType( dest, "float",                   /^-?\d*\.?\d+$/          , posNegFloat );    // 0, .0, 0.0, /positive/negative floating-point numbers, with or without a value to the left of the decimal point; positive and negative integers
         _addType( dest, "positiveFloat",           /^\d*\.?\d+$/            , positiveFloat );  // Positive floating-point numbers and positive integers
         _addType( dest, "negativeFloat",           /^-?0+$|^-?0*\.?0+$|^-\d*\.?\d+$/ , negativeFloat );  // Negative floating-point numbers and negative integers
         _addType( dest, "strictFloat",             /^\d$|^\d?\.(0|\d*[1-9])$|^-0?\.(\d*[1-9])$|^-?[1-9]\d*\.(\d*[1-9])$|^-?[1-9]+\d*$/  , posNegFloat );
         _addType( dest, "strictPositiveFloat",     /^0$|^0?\.(0|\d*[1-9])$|^[1-9]\d*\.(\d*[1-9])$|^[1-9]+\d*$/  , positiveFloat );
         _addType( dest, "strictNegativeFloat",     /^0(\.0)?$|^-0?\.(\d*[1-9])$|^-?[1-9]\d*\.(\d*[1-9])$|^-?[1-9]+\d*$/  , negativeFloat );
         _addType( dest, "money",                   /^-?\d*\.?\d{1,2}$/      , posNegFloat );    // Positive and negative floating-point numbers with one or two numbers after the decimal point, plus positive and negative integers
         _addType( dest, "positiveMoney",           /^\d*\.?\d{1,2}$/        , positiveFloat );  // Positive floating-point numbers with one or two numbers after the decimal point, and positive integers
         _addType( dest, "negativeMoney",           /^-\d*\.?\d{1,2}$/       , negativeFloat );  // Negative floating-point numbers with one or two numbers after the decimal point, plus negative integers

         // Positive floating-point numbers with one or two numbers after the decimal point;
         // Positive integers;
         // Negative floating-point numbers with one or two numbers after the decimal point, where sign is denoted by
         // wrapping the value in parentheses rather than using a minus sign (sometimes referred to as "accounting notation")
         _addType( dest, "accountingMoney", /^\d*\.?\d{1,2}$|^\(\d*\.?\d{1,2}\)$/  ,  /^[\.\d]$|^\.$|^\d*\.$|^\(\d*\.?$|^\(\d*\.\d{1,2}?$/ );

         _addType( dest, "negativeAccountingMoney", /^\(\d*\.?\d{1,2}\)$/  ,  /^\(\d*\.?$|^\(\d*\.\d{1,2}?$/ );  // The negative-only version of accountingMoney
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

            var responseEvent = EVENT_VALIDATION_FAILED;

            if( this.value.length === 0 ) {
               passesPartialRegex = true;
               passesFullRegex = true;
               responseEvent = EVENT_VALIDATION_SUCCESS;
            } else {
               passesFullRegex = regexes.fullRegex.test( this.value );

               if( regexes.partialRegex !== undefined && regexes.partialRegex !== null ) {
                  passesPartialRegex = regexes.partialRegex.test( this.value );
               }

               if( passesFullRegex ) {
                  log( "passes full regex" );
                  responseEvent = EVENT_VALIDATION_SUCCESS;
               } else {
                  log( "fails full regex" );

                  if( passesPartialRegex ) {
                     log( "passes partial regex" );
                     responseEvent = EVENT_VALIDATION_SUCCESS;
                  } else {
                     log( "fails partial regex" );
                     responseEvent = EVENT_VALIDATION_FAILED;
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

            log( "triggering " + responseEvent + " event " );

            jqThis.trigger( responseEvent );
         } );

         jqThis.on( "blur", function() {
            applyFormatting( this );

            var responseEvent = EVENT_VALIDATION_FAILED;

            if( this.value.length === 0 || regexes.fullRegex.test(this.value) ) {
               responseEvent = EVENT_VALIDATION_SUCCESS;
            }

            log( "triggering " + responseEvent + " event " );

            jqThis.trigger( responseEvent );
         } );

         function applyFormatting( domField ) {
            if( isNothing(domField.value) ) return;

            var t = settings.type;

            if( t === "money" || t === "positiveMoney" || t === "negativeMoney" || t === "accountingMoney" || t ==="negativeAccountingMoney" ) {
               var len         = domField.value.length;
               var openParen   = domField.value[0] === "(" ? "(" : "";
               var closeParen  = domField.value[ len - 1 ] === ")" ? ")" : ""; 
               var decimalIdx  = domField.value.indexOf( "." );
               var integerPart = domField.value.substring( openParen === "" ? 0 : 1,
                                                           decimalIdx > 0 ? decimalIdx -1 : (closeParen === ")" ? len - 1 : len) );
               var decimalPart = "00";

               if( decimalIdx > -1 ) {
                  integerPart = domField.value.substring( openParen === "" ? 0 : 1, decimalIdx );
                  decimalPart = domField.value.substring( decimalIdx + 1, closeParen === "" ? len : len - 1 );
               }

               domField.value = openParen + integerPart + "." + decimalPart + (decimalPart.length === 1 ? "0" : "") + closeParen;
            }
         }
      } );

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
}( jQuery ));
