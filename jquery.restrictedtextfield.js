/*
 * RestrictedTextField
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * This software is licensed under the 3-clause BSD license.
 *
 * Copyright (c) 2016 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/5
 */

(function( $ ) {
   "use strict";

   $.fn.restrictedTextField = function( options ) {

      var // Uppercase and lowercase letters
          ALPHA            = "alpha",

          // Uppercase letters
          UPPER_ALPHA      = "upperAlpha",

          // Lowercase letters
          LOWER_ALPHA      = "lowerAlpha",

          // Uppercase letters, lowercase letters, digits 0-9
          ALPHANUMERIC     = "alphanumeric",

          // Uppercase letters and digits 0-9
          UPPER_ALPHANUM   = "upperAlphanumeric",

          // Lowercase letters and digits 0-9
          LOWER_ALPHANUM   = "lowerAlphanumeric",

          // Positive and negative integers
          INT              = "int",

          // Positive integers
          POSITIVE_INT     = "positiveInt",

          // Negative integers
          NEGATIVE_INT     = "negativeInt",

          // Positive and negative floating-point numbers, plus positive and negative integers
          FLOAT            = "float",

          // Positive floating-point numbers and positive integers
          POSITIVE_FLOAT   = "positiveFloat",

          // Negative floating-point numbers and negative integers
          NEGATIVE_FLOAT   = "negativeFloat",

          // Positive and negative floating-point numbers with one or two numbers after the decimal point, plus positive and negative integers
          MONEY            = "money",

          // Positive floating-point numbers with one or two numbers after the decimal point, and positive integers
          POSITIVE_MONEY   = "positiveMoney",

          // Negative floating-point numbers with one or two numbers after the decimal point, plus negative integers
          NEGATIVE_MONEY   = "negativeMoney",

          // Positive floating-point numbers with one or two numbers after the decimal point;
          // Positive integers;
          // Negative floating-point numbers with one or two numbers after the decimal point, where sign is denoted by
          // wrapping the value in parentheses rather than using a minus sign (sometimes referred to as "accounting notation")
          ACCOUNTING_MONEY = "accountingMoney",

          // The negative-only version of ACCOUNTING_MONEY
          NEGATIVE_ACCOUNTING_MONEY = "negativeAccountingMoney";

      var EVENT_VALIDATION_FAILURE = "validationFailure",
          EVENT_VALIDATION_SUCCESS = "validationSuccess",
          EVENT_INPUT_IN_PROGRESS  = "inputInProgress";

      $.fn.restrictedTextField.types = $.fn.restrictedTextField.types || [];
      if( $.fn.restrictedTextField.types[ ALPHA ]            == undefined ) $.fn.restrictedTextField.types[ ALPHA ]            = /^[a-zA-Z]*$/;
      if( $.fn.restrictedTextField.types[ UPPER_ALPHA ]      == undefined ) $.fn.restrictedTextField.types[ UPPER_ALPHA ]      = /^[A-Z]*$/;
      if( $.fn.restrictedTextField.types[ LOWER_ALPHA ]      == undefined ) $.fn.restrictedTextField.types[ LOWER_ALPHA ]      = /^[a-z]*$/;
      if( $.fn.restrictedTextField.types[ ALPHANUMERIC ]     == undefined ) $.fn.restrictedTextField.types[ ALPHANUMERIC ]     = /^[a-zA-Z\d]*$/;
      if( $.fn.restrictedTextField.types[ UPPER_ALPHANUM ]   == undefined ) $.fn.restrictedTextField.types[ UPPER_ALPHANUM ]   = /^[A-Z\d]*$/;
      if( $.fn.restrictedTextField.types[ LOWER_ALPHANUM ]   == undefined ) $.fn.restrictedTextField.types[ LOWER_ALPHANUM ]   = /^[a-z\d]*$/;
      if( $.fn.restrictedTextField.types[ INT ]              == undefined ) $.fn.restrictedTextField.types[ INT ]              = /^0$|^-?[1-9]\d*$/;
      if( $.fn.restrictedTextField.types[ POSITIVE_INT ]     == undefined ) $.fn.restrictedTextField.types[ POSITIVE_INT ]     = /^0$|^[1-9]\d*$/;
      if( $.fn.restrictedTextField.types[ NEGATIVE_INT ]     == undefined ) $.fn.restrictedTextField.types[ NEGATIVE_INT ]     = /^0$|^-[1-9]\d*$/;
      if( $.fn.restrictedTextField.types[ FLOAT ]            == undefined ) $.fn.restrictedTextField.types[ FLOAT ]            = /^-?\d*\.?\d+$/;
      if( $.fn.restrictedTextField.types[ POSITIVE_FLOAT ]   == undefined ) $.fn.restrictedTextField.types[ POSITIVE_FLOAT ]   = /^\d*\.?\d+$/;
      if( $.fn.restrictedTextField.types[ NEGATIVE_FLOAT ]   == undefined ) $.fn.restrictedTextField.types[ NEGATIVE_FLOAT ]   = /^-\d*\.?\d+$/;
      if( $.fn.restrictedTextField.types[ MONEY ]            == undefined ) $.fn.restrictedTextField.types[ MONEY ]            = /^-?\d*\.?\d{1,2}$/;
      if( $.fn.restrictedTextField.types[ POSITIVE_MONEY ]   == undefined ) $.fn.restrictedTextField.types[ POSITIVE_MONEY ]   = /^\d*\.?\d{1,2}$/;
      if( $.fn.restrictedTextField.types[ NEGATIVE_MONEY ]   == undefined ) $.fn.restrictedTextField.types[ NEGATIVE_MONEY ]   = /^-\d*\.?\d{1,2}$/;
      if( $.fn.restrictedTextField.types[ ACCOUNTING_MONEY ] == undefined ) $.fn.restrictedTextField.types[ ACCOUNTING_MONEY ] = /^\d*\.?\d{1,2}$|^\(\d*\.?\d{1,2}\)$/;
      if( $.fn.restrictedTextField.types[ NEGATIVE_ACCOUNTING_MONEY ] == undefined ) $.fn.restrictedTextField.types[ NEGATIVE_ACCOUNTING_MONEY ] = /^\(\d*\.?\d{1,2}\)$/;

      var settings = $.extend( {
          type                : null,
          preventInvalidInput : true
      }, options );

      var regex = $.fn.restrictedTextField.types[ settings.type ];

      if( isNothing(regex) ) {
         throw "Invalid type: " + settings.type;
      }

      function isNothing( value ) {
         return value == undefined || value == null || value.length < 1;
      }

      function processFinishedInput( jqField, valueBeforeCommit ) {
         var val = jqField.val();

         if( val.length === 0 ) {
            console.log( "triggering success" );
            jqField.trigger( EVENT_VALIDATION_SUCCESS );
         } else if( val.length > 0 ) {
            console.log( "processFinishedInput - " + settings.type + ":  " + regex ); 

            if( !regex.test(val) ) {
               console.log( "failed validation" );

               if( settings.preventInvalidInput ) {
                  console.log( "reverting" );
                  jqField.val( valueBeforeCommit );
               }

               console.log( "triggering fail event" );
               jqField.trigger( EVENT_VALIDATION_FAILURE );
            } else {
               console.log( "triggering success" );
               jqField.trigger( EVENT_VALIDATION_SUCCESS );
            }
         }
      }

      return this.each( function() {
         var jqThis = $( this );
         var valueBeforeCommit = "";

         jqThis.on( "keydown paste", function() {
            valueBeforeCommit = this.value;
         } );

         jqThis.on( "input", function() {
            var jqThis = $( this );

            // The user is entering data, but has yet to reach the minimum number of characters needed to satisfy
            // the regular expression in $.fn.restrictedTextField.types.  We can't check against that yet because
            // it would result in the user being unable to enter anything.  Until we've reached the minimum number
            // of required characters, allow the user to input any character which would satisfy the regular
            // expression.  If we've reached the minimum required number of characters, perform the validation
            // against the regular expression in $.fn.restrictedTextField.types.

            var handled = false;

            if( settings.type === INT || settings.type === NEGATIVE_INT ) {
               if( this.value.length === 1 && this.value[0] === "-" ) {
                  jqThis.trigger( EVENT_INPUT_IN_PROGRESS );
                  handled = true;
               } else {
                  processFinishedInput( jqThis, valueBeforeCommit );
                  handled = true;
               }
            } else if( settings.type === FLOAT || settings.type === POSITIVE_FLOAT || settings.type === NEGATIVE_FLOAT ||
                       settings.type === MONEY || settings.type === POSITIVE_MONEY || settings.type === NEGATIVE_MONEY ||
                       settings.type === ACCOUNTING_MONEY || settings.type === NEGATIVE_ACCOUNTING_MONEY ) {
               var unfinishedInputRegex = null;

               if( settings.type === FLOAT || settings.type === MONEY ) {
                  unfinishedInputRegex = /^[-\.\d]$|^-\.$|^-?\d*\.$/;
               } else if( settings.type === POSITIVE_FLOAT || settings.type === POSITIVE_MONEY ||
                          (settings.type === ACCOUNTING_MONEY && !isNaN(this.value) && this.value >= 0) ) {
                  unfinishedInputRegex = /^[\.\d]$|^\.$|^\d*\.$/;
               } else if( settings.type === NEGATIVE_FLOAT || settings.type === NEGATIVE_MONEY ) {
                  unfinishedInputRegex = /^0\.?$|^-\.?$|^-\d*\.$/;
               } else if( settings.type === ACCOUNTING_MONEY || settings.type === NEGATIVE_ACCOUNTING_MONEY ) {
                  unfinishedInputRegex = /^\(\d*\.?$|^\(\d*\.\d{1,2}?$/;
               }

               if( unfinishedInputRegex.test(this.value) ) {
                  jqThis.trigger( EVENT_INPUT_IN_PROGRESS );
                  handled = true;
               } else {
                  processFinishedInput( jqThis, valueBeforeCommit );
                  handled = true;
               }
            }

            if( !handled ) {
               processFinishedInput( jqThis, valueBeforeCommit );
               handled = true;
            }
         } );

         jqThis.on( "blur", function() {
            processFinishedInput( jqThis, jqThis.val() );
         } );
      } );
   };
}( jQuery ));
