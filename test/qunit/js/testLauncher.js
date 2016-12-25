/*
 * RestrictedTextField
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

"use strict";

( function() {
   "use strict";

   var selector = "#field";

   var log;

   var inputIgnoredEvent,
       validationFailedEvent,
       validationSuccessEvent;

   var testNum = 0;

   $( document ).ready( function() {
      log = document.getElementById( "log" );
   } );

   function setUp() {
      log.innerHTML = "";
      writeLog( "setUp" );

      $( "#field" ).remove();

      $( "<input/>", {
         id   : "field",
         type : "text"
      } ).appendTo( $("#fieldContainer") );

      resetEventFlags();
   }

   function tearDown() {
      writeLog( "tearDown\n" );
   }

   function resetEventFlags() {
      writeLog( "resetting event flags" );

      inputIgnoredEvent = false;
      validationFailedEvent = false;
      validationSuccessEvent = false;
   }

   function initField( testName, fieldType, ignore ) {
      setUp();

      writeLog( "test:  " + testName );
      writeLog( "initField:  fieldType[" + fieldType + "], ignore[" + ignore + "]" );

      var jqField = $( "#field" );

      jqField.restrictedTextField( { type : fieldType,
                                     preventInvalidInput : ignore,
                                     logger : writeLog } );

      jqField.on( "inputIgnored", function() {
         writeLog( "inputIgnored event captured" );
         inputIgnoredEvent = true;
      } );

      jqField.on( "validationFailed", function() {
         writeLog( "validationFailed event caputred" );
         validationFailedEvent = true;
      } );

      jqField.on( "validationSuccess", function() {
         writeLog( "validationSuccess event captured" );
         validationSuccessEvent = true;
      } );
   }

   function simulateInput( input ) {
      var field = $( "#field" );

      for( var i = 0; i < input.length; i++ ) {
         field.trigger( "keydown" );
         field.val( field.val() + input[i] );
         field.trigger( "input" );
      }
   }

   function writeLog( msg ) {
      var node = document.createTextNode( msg + "\n" );
      log.appendChild( node );
   }

   function writeStatus( testName, testNum, totalTests ) {
      $( "#status" ).html( testName + "<br/>" + testNum + " of " + totalTests );
   }

   function validatePreBlur( params ) {
      var actualValue = $( selector ).val();

      equal( params.expectedValueBeforeBlur, actualValue, "Pre-blur:  Ensure field has expected value" );

      if( params.preventInvalidInput ) {
         notOk( validationFailedEvent, "Pre-blur:  Validation failed event should never fire pre-blur when configured to ignore invalid input" );
         notOk( validationSuccessEvent, "Pre-blur:  Validation success event should never fire pre-blur when configured to ignore invalid input" );

         if( params.expectedValueBeforeBlur !== params.input ) {
            ok( inputIgnoredEvent, "Pre-blur:  Test case expects ignored input, so ensure input ignored event fired" );
         } else {
            notOk( inputIgnoredEvent, "Pre-blur:  Test case expects all input is valid, so ensure input ignored event didn't fire" );
         }
      } else {
         notOk( inputIgnoredEvent, "Pre-blur:  Test case is configured to allow invalid input, so ensure input ignored event didn't fire" );
      }
   }

   function validatePostBlur( params ) {
      var actualValue = $( selector ).val();

      equal( params.expectedValueAfterBlur, actualValue, "Post-blur:  Ensure field has expected value" );

      if( !params.preventInvalidInput ) {
         notOk( inputIgnoredEvent, "Post-blur:  Test case is configured to allow invalid input, so ensure input ignored event didn't fire" );
      }

      if( params.expectedEventOnBlur == Event.VALIDATION_FAILED ) {
         ok( validationFailedEvent, "Post-blur:  Ensure validation failed event fired" );
         notOk( validationSuccessEvent, "Post-blur:  Ensure validation success event didn't fire" );
      } else if( params.expectedEventOnBlur == Event.VALIDATION_SUCCESS ) {
         ok( validationSuccessEvent, "Post-blur:  Ensure validation success event fired" );
         notOk( validationFailedEvent, "Post-blur:  Ensure validation failed event didn't fire" );
      }
   }

   if( validateTestCases(testCases) ) {
      QUnit.cases( testCases )
      .test( "Parameterized Test", function(params) {
         equal( params.testName, params.testName, "Test: " + params.testName );  // Hack to get the test name into the report
         ++testNum;

         initField( params.testName, params.fieldType, params.preventInvalidInput );
         writeStatus( params.testName, testNum, testCases.length );

         simulateInput( params.input );

         validatePreBlur( params );

         writeLog( "setting up blur validation" );
         resetEventFlags();

         $( selector ).blur();
         validatePostBlur( params );   
      } );
   }
} )();
