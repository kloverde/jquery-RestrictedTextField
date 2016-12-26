"use strict";

( function() {
   var descrMap = [];

   $( document ).ready( function() {
      var dropdown = document.getElementById( "fieldTypes" );
      var keys = Object.keys( FieldType );
      
      for( var i = 0; i < keys.length; i++ ) {
         var key = keys[i];
         var value = FieldType[key];
         var type = value[0];
         var opt = document.createElement( "option" );

         opt.value = type;
         opt.innerHTML = type;
         dropdown.appendChild( opt );

         descrMap[type] = value[1];
      }

      $( dropdown ).on( "change", function() {
         if( this.value !== "Select Type" ) {
            createField();
         } else {
            $( "#field" ).remove();
            $( "#code" ).html( "" );
         }
      } );

      $( "#preventInvalid" ).on( "change", function() {
         createField();
      } );

      createCustomTypes();
   } );

   function createField() {
      var fieldType = document.getElementById( "fieldTypes" ).value;
      var preventInvalid = $( "#preventInvalid" ).is( ":checked" );
      var fieldContainer = document.getElementById( "fieldContainer" );
      var field = document.createElement( "input" );

      field.type = "text";
      field.id = "field";

      fieldContainer.innerHTML = descrMap[fieldType] + "<br/><br/>";
      fieldContainer.appendChild( field );

      field = $( field );

      field.restrictedTextField( {
         type : fieldType,
         preventInvalidInput : preventInvalid,
         logger : log
      } );

      field.on( "inputIgnored", function(event) {
         var jqThis = $( this );

         jqThis.removeClass( "invalid" );
         jqThis.removeClass( "inputIgnored" );

         flashBorder( jqThis, "inputIgnored", 3, 150 );
      } )
      .on( "validationFailed", function(event) {
         var jqThis = $( this );

         jqThis.removeClass( "inputIgnored" );
         jqThis.addClass( "invalid" );
      } )
      .on( "validationSuccess", function(event) {
         var jqThis = $( this );

         jqThis.removeClass( "inputIgnored" );
         jqThis.removeClass( "invalid" );
      } );

      $( "#code" ).html(
         "<code>\n" +
         "$( \"#field\" ).restrictedTextField( {<br/>\n" +
         "&nbsp;&nbsp;&nbsp;type : \"" + fieldType + "\",<br/>\n" +
         "&nbsp;&nbsp;&nbsp;preventInvalidInput : " + preventInvalid + ",<br/>\n" +
         "&nbsp;&nbsp;&nbsp;logger : log<br/>\n" +
         "} );<br/>\n" +
         "</code>\n"
      );
   }

   function flashBorder( jqField, cssClass, times, delayMillis ) {
      if( times === -2 ) {
         jqField.removeClass( cssClass );
         return;
      }

      if( jqField.hasClass(cssClass) ) {
         jqField.removeClass( cssClass );
      } else {
         jqField.addClass( cssClass );
      }

      setTimeout( function() {
         flashBorder( jqField, cssClass, --times, delayMillis );
      }, delayMillis );
   }

   function log( msg ) {
      console.log( msg );
   }

   function createCustomTypes() {
      var cfg= new $.fn.RestrictedTextFieldConfig();

      cfg.addType( "vowels", /^[aeiou]*$/, null );
      cfg.addType( "additionExpr", /^\d+\+\d+$/, /^\d+\+?$/ );

      $( "#txtVowelsOnly" ).restrictedTextField( {
         type: "vowels",
         logger: log
      } );

      $( "#txtAdditionExpr" ).restrictedTextField( {
         type: "additionExpr",
         logger: log
      } );
   }
} )();
