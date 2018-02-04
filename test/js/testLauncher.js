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

   var fieldSelector = "#field";

   var field = null,
       status = null,
       realtimeReadout = null,
       currentTest = null,
       fieldContainer = null,
       saveButton = null;

   var inputIgnoredEvent = false,
       validationFailedEvent = false,
       validationSuccessEvent = false;

   var testNum = 0;


   $( document ).ready( function() {
      status = $( "#status" );
      realtimeReadout = $( "#realtimeReadout" );
      currentTest = $( "#currentTest" );
      fieldContainer = $( "#fieldContainer" );
      saveButton = $( "#saveButton" );
   } );

   function saveReport( xml ) {
      var uriContent = "data:application/octet-stream," + encodeURIComponent( report );
   }

   function resetEventFlags() {
      log( "resetting event flags" );

      inputIgnoredEvent = false;
      validationFailedEvent = false;
      validationSuccessEvent = false;
   }

   function initField( title, fieldType, ignore ) {
      log( "test:  " + title );
      log( "initField:  fieldType[" + fieldType + "], ignore[" + ignore + "]" );

      field.restrictedTextField( { type : fieldType,
                                   preventInvalidInput : ignore,
                                   logger : log } );

      field.on( "inputIgnored", function() {
         log( "inputIgnored event captured" );
         inputIgnoredEvent = true;
      } );

      field.on( "validationFailed", function() {
         log( "validationFailed event captured" );
         validationFailedEvent = true;
      } );

      field.on( "validationSuccess", function() {
         log( "validationSuccess event captured" );
         validationSuccessEvent = true;
      } );
   }

   function simulateInput( input ) {
      for( var i = 0; i < input.length; i++ ) {
         field.trigger( "keydown" );
         field.val( field.val() + input[i] );
         field.trigger( "input" );
      }
   }

   function log( msg ) {
   }

   function writeCurrentTest( title, testNum, totalTests ) {
      currentTest.html( title + "<br/>Test " + testNum + " of " + totalTests );
   }

   function validatePreBlur( params ) {
      var actualValue = field.val();

      equal( actualValue, params.expectedValueBeforeBlur, "Pre-blur:  Ensure field has expected value" );

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
      var actualValue = field.val();

      equal( actualValue, params.expectedValueAfterBlur, "Post-blur:  Ensure field has expected value" );

      if( !params.preventInvalidInput ) {
         notOk( inputIgnoredEvent, "Post-blur:  Test case is configured to allow invalid input, so ensure input ignored event didn't fire" );
      }

      if( params.expectedEventOnBlur === Event.VALIDATION_FAILED ) {
         ok( validationFailedEvent, "Post-blur:  Ensure validation failed event fired" );
         notOk( validationSuccessEvent, "Post-blur:  Ensure validation success event didn't fire" );
      } else if( params.expectedEventOnBlur === Event.VALIDATION_SUCCESS ) {
         ok( validationSuccessEvent, "Post-blur:  Ensure validation success event fired" );
         notOk( validationFailedEvent, "Post-blur:  Ensure validation failed event didn't fire" );
      } else {
         notOk( true, "Test has unknown expected result - should have been caught by validation in testCases.js" );
      }
   }

   function download( filename, text ) {
      var a = $( "<a/>", {
         "href" : "data:text/plain;charset=utf-8," + encodeURIComponent( text ),
         "download" : filename
      } );

      // Odd... jQuery.click() doesn't trigger the link, even if it's appended to the page first.
      // Oh well, regular ol' JS to the rescue.

      var event = document.createEvent( "MouseEvents" );
      event.initEvent( "click", true, true );
      a[0].dispatchEvent( event );
   }

   if( validateTestCases(testCases) ) {
      QUnit.config.hidepassed = true;

      QUnit.testStart( function() {   // Analagous to JUnit's @Before
         log( "setUp" );

         ++testNum;

         if( field != null ) {
            field.remove();
         }

         $( "<input/>", {
            id   : "field",
            type : "text"
         } ).appendTo( fieldContainer );

         field = $( fieldSelector );
         resetEventFlags();
      } );

      QUnit.jUnitDone( function(report) {
         if( report.results.failed === 0 ) {
            status.html( "SUCCESS" );
            status.addClass( "statusSuccess" );
         } else {
            status.html( "FAILED" );
            status.addClass( "statusFail" );
         }

         realtimeReadout.remove();
         $( "#done" ).css( "visibility", "visible" );

         saveButton.on( "click", function() {
            function zeroPad( datePart ) {
               return datePart < 10 ? "0" + datePart : datePart;
            }

            var d = new Date();

            var year  = d.getFullYear(),
                month = zeroPad( d.getMonth() + 1 ),
                day   = zeroPad( d.getDate() ),
                hour  = zeroPad( d.getHours() ),
                min   = zeroPad( d.getMinutes() ),
                sec   = zeroPad( d.getSeconds() );

            var timestamp = year + "-" + month + "-" + day + "_" + hour + "-" + min + "-" + sec;

            download( "RestrictedTextField-TestResult-" + timestamp + ".xml", report.xml );
         } );

         $( "#saveContainer" ).css( "visibility", "visible" );
      } );

      QUnit.cases( testCases ).test( "Test", function(params) {
         initField( params.title, params.fieldType[0], params.preventInvalidInput );
         writeCurrentTest( params.title, testNum, testCases.length );

         simulateInput( params.input );
         validatePreBlur( params );

         log( "setting up blur validation" );
         resetEventFlags();

         field.blur();
         validatePostBlur( params );
      } );
   }
} )();