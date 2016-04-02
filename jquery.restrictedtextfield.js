/**
 * RestrictedTextField v1.0
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

   /**
    * Developers:  Instantiate this to add your own types
    */  
   $.fn.RestrictedTextFieldConfig = function() {
      var dest = $.fn.RestrictedTextFieldConfig.customTypes = $.fn.RestrictedTextFieldConfig.customTypes || [];

      return {
         /**
          * Adds a custom type
          *
          * @param id           - The identifier you will use to access the new type
          *
          * @param fullRegex    - RegExp object.  This regular expression describes what a valid value looks like when the user is finished
          *                       entering data, NOT what valid input looks like as it's being entered.  If your type is complex, like a
          *                       phone number for example, you need to provide a value for partialRegex to ensure that your user will be
          *                       able to continue to type into the field.  If partialRegex is not defined or is null, the text field will
          *                       be validated against this regular expression on every keystroke.  Notwithstanding any of the foregoing,
          *                       this regular expression is also used to validate the text field on blur. 
          *
          * @param partialRegex - RegExp object.  This regular expression describes what a valid value looks like while the user is entering
          *                       it, NOT what valid input looks like after the user has finished.  If you provide a value for this, this
          *                       regular expression will be used to validate the text field on every keystroke.  This parameter may be
          *                       null, but if your type is complex, you will find it necessary to provide a value.
          */
         addType : function addType( id, fullRegex, partialRegex ) {
                      _addType( dest, id, fullRegex, partialRegex );
                   }
      };
   };

   $.fn.restrictedTextField = function( options ) {
      var settings = $.extend( {
         type                : null,
         preventInvalidInput : true
      }, options );

      var EVENT_VALIDATION_FAILURE = "validationFailure",
          EVENT_VALIDATION_SUCCESS = "validationSuccess",
          EVENT_INPUT_IN_PROGRESS  = "inputInProgress";

      if( $.fn.restrictedTextField.types == undefined || $.fn.restrictedTextField.types == null ) {
         init();
      }

      function init() {
         var negativeInt   = /^-$/,
             posNegFloat = /^[-\.\d]$|^-\.$|^-?\d*\.$/,
             positiveFloat = /^[\.\d]$|^\.$|^\d*\.$/,
             negativeFloat = /^0\.?$|^-\.?$|^-\d*\.$/;

         if( $.fn.restrictedTextField.types == undefined || $.fn.restrictedTextField.types == null ) {
            $.fn.restrictedTextField.types = [];
         }

         var dest = $.fn.restrictedTextField.types = $.fn.restrictedTextField.types || [];

         _addType( dest, "alpha",             /^[a-zA-Z]*$/        , null );  // Uppercase and lowercase letters
         _addType( dest, "upperAlpha",        /^[A-Z]*$/           , null );  // Uppercase letters
         _addType( dest, "lowerAlpha",        /^[a-z]*$/           , null );  // Lowercase letters
         _addType( dest, "alphanumeric",      /^[a-zA-Z\d]*$/      , null );  // Uppercase letters, lowercase letters, digits 0-9
         _addType( dest, "upperAlphanumeric", /^[A-Z\d]*$/         , null );  // Uppercase letters and digits 0-9
         _addType( dest, "lowerAlphanumeric", /^[a-z\d]*$/         , null );  // Lowercase letters and digits 0-9
         _addType( dest, "int",               /^0$|^-?[1-9]\d*$/   , negativeInt );    // Positive and negative integers
         _addType( dest, "positiveInt",       /^0$|^[1-9]\d*$/     , null );           // Positive integers
         _addType( dest, "negativeInt",       /^0$|^-[1-9]\d*$/    , negativeInt );    // Negative integers
         _addType( dest, "float",             /^-?\d*\.?\d+$/      , posNegFloat );    // Positive and negative floating-point numbers, plus positive and negative integers
         _addType( dest, "positiveFloat",     /^\d*\.?\d+$/        , positiveFloat );  // Positive floating-point numbers and positive integers
         _addType( dest, "negativeFloat",     /^-\d*\.?\d+$/       , negativeFloat );  // Negative floating-point numbers and negative integers
         _addType( dest, "money",             /^-?\d*\.?\d{1,2}$/  , posNegFloat );    // Positive and negative floating-point numbers with one or two numbers after the decimal point, plus positive and negative integers
         _addType( dest, "positiveMoney",     /^\d*\.?\d{1,2}$/    , positiveFloat );  // Positive floating-point numbers with one or two numbers after the decimal point, and positive integers
         _addType( dest, "negativeMoney",     /^-\d*\.?\d{1,2}$/   , negativeFloat );  // Negative floating-point numbers with one or two numbers after the decimal point, plus negative integers

         // Positive floating-point numbers with one or two numbers after the decimal point;
         // Positive integers;
         // Negative floating-point numbers with one or two numbers after the decimal point, where sign is denoted by
         // wrapping the value in parentheses rather than using a minus sign (sometimes referred to as "accounting notation")
         _addType( dest, "accountingMoney", /^\d*\.?\d{1,2}$|^\(\d*\.?\d{1,2}\)$/  ,  /^[\.\d]$|^\.$|^\d*\.$|^\(\d*\.?$|^\(\d*\.\d{1,2}?$/ );

         _addType( dest, "negativeAccountingMoney", /^\(\d*\.?\d{1,2}\)$/  ,  /^\(\d*\.?$|^\(\d*\.\d{1,2}?$/ );  // The negative-only version of accountingMoney
      }

      var regexes = $.fn.restrictedTextField.types[ settings.type ];

      if( isNothing(regexes) ) {
         if( $.fn.RestrictedTextFieldConfig.customTypes != undefined && $.fn.RestrictedTextFieldConfig.customTypes != null ) {
            regexes = $.fn.RestrictedTextFieldConfig.customTypes[ settings.type ];
         }

         if( isNothing(regexes) ) {
            throw "Invalid type: " + settings.type;
         }
      }

      function processFinishedInput( jqField, valueBeforeCommit ) {
         var val = jqField.val();

         if( val.length === 0 ) {
            console.log( "triggering success" );

            jqField.trigger( EVENT_VALIDATION_SUCCESS );
         } else if( val.length > 0 ) {
            console.log( "processFinishedInput - " + settings.type + ":  " + regexes.fullRegex ); 

            if( !regexes.fullRegex.test(val) ) {
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

            if( regexes.partialRegex != undefined && regexes.partialRegex != null ) {
               if( regexes.partialRegex.test(this.value) ) {
                  jqThis.trigger( EVENT_INPUT_IN_PROGRESS );
               } else {
                  processFinishedInput( jqThis, valueBeforeCommit );
               }
            } else {
               processFinishedInput( jqThis, valueBeforeCommit );
            }
         } );

         jqThis.on( "blur", function() {
            processFinishedInput( jqThis, jqThis.val() );
         } );
      } );
   };

   function isNothing( value ) {
      return value == undefined || value == null || value.length < 1;
   }

   function _addType( destination, id, fullRegex, partialRegex ) {
      if( id == undefined || id == null ) throw "id is undefined";
      if( !typeof(id) === "string" ) throw "id should be a string";
      if( id.length < 1 ) throw "id is empty";

      if( fullRegex == undefined || fullRegex == null ) throw "fullRegex is undefined";
      if( fullRegex != undefined && fullRegex != null && typeof(fullRegex) !== "object" || fullRegex.constructor.name !== "RegExp" ) throw "fullRegex should be a regular expression object";

      if( partialRegex != undefined && partialRegex != null && (typeof(partialRegex) !== "object" || partialRegex.constructor.name !== "RegExp") ) throw "partialRegex should be a regular expression object";

      destination[ id ] = { "fullRegex"    : fullRegex,
                            "partialRegex" : partialRegex };
   }
}( jQuery ));
