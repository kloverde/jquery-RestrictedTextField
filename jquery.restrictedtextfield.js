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

      var ALPHA           = "alpha",
          UPPER_ALPHA     = "upperAlpha",
          LOWER_ALPHA     = "lowerAlpha",
          ALPHANUMERIC    = "alphanumeric",
          UPPER_ALPHANUM  = "upperAlphanumeric",
          LOWER_ALPHANUM  = "lowerAlphanumeric",
          INT             = "int",
          POSITIVE_INT    = "positiveInt",
          NEGATIVE_INT    = "negativeInt",
          FLOAT           = "float",
          POSITIVE_FLOAT  = "positiveFloat",
          NEGATIVE_FLOAT  = "negativeFloat";

      var EVENT_VALIDATION_FAILURE = "validationFailure",
          EVENT_VALIDATION_SUCCESS = "validationSuccess",
          EVENT_INPUT_IN_PROGRESS  = "inputInProgress";

      $.fn.restrictedTextField.types = $.fn.restrictedTextField.types || [];
      if( $.fn.restrictedTextField.types[ ALPHA ]          == undefined ) $.fn.restrictedTextField.types[ ALPHA ]          = /^[a-zA-Z]*$/;
      if( $.fn.restrictedTextField.types[ UPPER_ALPHA ]    == undefined ) $.fn.restrictedTextField.types[ UPPER_ALPHA ]    = /^[A-Z]*$/;
      if( $.fn.restrictedTextField.types[ LOWER_ALPHA ]    == undefined ) $.fn.restrictedTextField.types[ LOWER_ALPHA ]    = /^[a-z]*$/;
      if( $.fn.restrictedTextField.types[ ALPHANUMERIC ]   == undefined ) $.fn.restrictedTextField.types[ ALPHANUMERIC ]   = /^[a-zA-Z\d]*$/;
      if( $.fn.restrictedTextField.types[ UPPER_ALPHANUM ] == undefined ) $.fn.restrictedTextField.types[ UPPER_ALPHANUM ] = /^[A-Z\d]*$/;
      if( $.fn.restrictedTextField.types[ LOWER_ALPHANUM ] == undefined ) $.fn.restrictedTextField.types[ LOWER_ALPHANUM ] = /^[a-z\d]*$/;
      if( $.fn.restrictedTextField.types[ INT ]            == undefined ) $.fn.restrictedTextField.types[ INT ]            = /^0$|^-?[1-9][0-9]*$/;
      if( $.fn.restrictedTextField.types[ POSITIVE_INT ]   == undefined ) $.fn.restrictedTextField.types[ POSITIVE_INT ]   = /^0$|^[1-9][0-9]*$/;
      if( $.fn.restrictedTextField.types[ NEGATIVE_INT ]   == undefined ) $.fn.restrictedTextField.types[ NEGATIVE_INT ]   = /^0$|^-[1-9][0-9]*$/;
      if( $.fn.restrictedTextField.types[ FLOAT ]          == undefined ) $.fn.restrictedTextField.types[ FLOAT ]          = /^-?[0-9]*\.?[0-9]+$/;    // TODO:  allows 00 and -00 
      if( $.fn.restrictedTextField.types[ POSITIVE_FLOAT ] == undefined ) $.fn.restrictedTextField.types[ POSITIVE_FLOAT ] = /^[0-9]*\.?[0-9]+$/;
      if( $.fn.restrictedTextField.types[ NEGATIVE_FLOAT ] == undefined ) $.fn.restrictedTextField.types[ NEGATIVE_FLOAT ] = /^-[0-9]*\.?[0-9]+$/;

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
         console.log( "processFinishedInput - " + settings.type + ":  " + regex ); 
         if( !regex.test(jqField.val()) ) {
            console.log( "fails" );
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

      return this.each( function() {
         var jqThis = $( this );
         var valueBeforeCommit = "";

         jqThis.on( "keydown paste", function() {
            valueBeforeCommit = this.value;
         } );

         jqThis.on( "input", function() {
            var jqThis = $( this );

            // The user is entering data, but has yet to reach the minimum number of characters needed to satisfy
            // the regular expression in $.fn.restrictedTextField.types[].  We can't check against that yet
            // because it would result in the user being unable to enter anything.  Until we've reached the
            // minimum number of required characters, allow the user to input any character which would satisfy
            // the regular expression.  If we've reached the minimum required number of characters, perform the
            // validation against the regular expression in $.fn.restrictedTextField.types[].

            var handled = false;

            if( settings.type === INT || settings.type === NEGATIVE_INT ) {
               if( this.value.length === 1 && this.value[0] === "-" ) {
                  jqThis.trigger( EVENT_INPUT_IN_PROGRESS );
                  handled = true;
               } else {
                  processFinishedInput( jqThis, valueBeforeCommit );
                  handled = true;
               }
            } else if( settings.type === FLOAT || settings.type === POSITIVE_FLOAT || settings.type === NEGATIVE_FLOAT ) {
               var floatRegex = null;

               if( settings.type === FLOAT ) {
                  floatRegex = /^[-\.\d]$|^-\.$|^-?\d*\.$/;   // [-], [.], [digit], [-.], [+/-digits.]
               } else if( settings.type === POSITIVE_FLOAT ) {
                  floatRegex = /^[\.\d]$|^\.$|^\d*\.$/;
               } else if( settings.type === NEGATIVE_FLOAT ) {
                  floatRegex = /^0\.?$|^-\.?$|^-\d*\.$/;
               }

               if( floatRegex.test(this.value) ) {
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
