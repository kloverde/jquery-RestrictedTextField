RestrictedTextField v1.1
=========================

See LICENSE for this software's licensing terms.

ResrictedTextField is a jQuery plugin which enforces data formats on HTML text boxes.  Data which doesn't match the declared format is rejected.


## Features

* Prevent invalid keystrokes or catch a validation failure event to handle it as you wish

* Features 23 built-in types:

1.  Alpha strings
2.  Uppercase alpha strings
3.  Lowercase alpha strings
4.  Alpha strings plus space
5.  Uppercase alpha strings plus space
6.  Lowercase alpha strings plus space
7.  Alphanumeric strings
8.  Uppercase alphanumeric strings
9.  Lowercase alphanumeric strings
10.  Alphanumeric strings plus space
11.  Uppercase alphanumeric strings plus space
12.  Lowercase alphanumeric strings plus space
13.  Integers
14.  Positive integers
15.  Negative integers
16.  Floats
17.  Positive floats
18.  Negative floats
19.  Money
20.  Positive money
21.  Negative money
22.  Accounting money notation (negatives expressed by wrapping the value in parentheses)
23.  Negative accounting money

* Endlessly extendible:  define your own types

* Money types automatically format on blur to end in a decimal point and two digits


## Configuration

| Property | Description   | Data Type | Valid Values         | Default Value |
| -------- | --------------|---------- |----------------------|---------------|
| `type`   | Text field type.  This is a required setting. | string | alpha, upperAlpha, lowerAlpha, alphaSpace, upperAlphaSpace, lowerAlphaSpace, alphanumeric, upperAlphanumeric, lowerAlphanumeric, alphanumericSpace, upperAlphanumericSpace, lowerAlphanumericSpace, int, positiveInt, negativeInt, float, positiveFloat, negativeFloat, money, positiveMoney, negativeMoney, accountingMoney, negativeAccountingMoney| null |
| `preventInvalidInput` | When enabled, invalid keystrokes are ignored (the value of the text field is not updated).  When disabled, invalid keystrokes are not ignored. | boolean | true/false | true |
| `logger` | A optional callback function for logging.  If you want to enable logging, provide a function and then do whatever you wish with the message. | function | A function accepting the log message as a string argument | undefined |


## Events

These events are fired based on the state of the text field.

| Event name        | Description                                                     |
| ------------------| ----------------------------------------------------------------|
| inputIgnored      | Fires when an invalid keystroke is ignored.  `preventInvalidInput` must be enabled for this event to fire. |
| validationFailed  | Fires when an invalid keystroke is made when `preventInvalidInput` is disabled.  Also fires if validation performed on blur() fails. |
| validationSuccess | Fires when the user removes invalid data when `preventInvalidInput` is disabled.  Also fires if validation performed on blur() passes. |


#### Example


```html
<input type="text" id="field"/>
```
```javascript
$( "#field" ).restrictedTextField( {
   type : "alpha"
} );
```

See the included HTML file for a complete demo.


# Browser Compatibility

* Firefox 45.0.1:  Compatible
* Chrome 49.0.2623.108 m:  Compatible
* Edge:  Compatible
* IE 11:  Compatible
* IE 10:  Compatible
* IE 9:  Compatible
* IE 8:  INCOMPATIBLE


# Running Unit Tests

[Selenium](http://www.seleniumhq.org) is used for unit testing in order to generate true keypresses in a text field.  A JavaScript-based framework (such as QUnit, etc.) would have made life much easier, but synthetic JavaScript events don't carry out the actual actions associated with them.  In other words, simulating a keypress fires the correct events, but doesn't result in actual text being written to a text field.  This is a security restriction of JavaScript.  As a result, this project uses Selenium and JUnit.

The easiest way to get this running is to install the following dependencies:

1. [Node.js](https://nodejs.org/en)
2. [Gradle](http://gradle.org)
3. [Java SDK](http://www.oracle.com/technetwork/java/javase/index.html) version 7 or later
4. [BuildScripts](https://github.com/kloverde/BuildScripts)

Now, from a command prompt:

1.  `cd` into the `jquery-RestrictedTextField/SeleniumJUnitClient` directory
2.  Modify the `buildScriptsDir` property in `gradle.properties` to reflect the location of BuildScripts on your filesystem
3.  `cd` to the `jquery-RestrictedTextField` directory
4.  Type `npm install`
5.  Type `grunt test`


## Thanks

Do you like this library?  Want to toss a few bucks my way to say thanks?  I accept donations at https://paypal.me/KurtisLoVerde/7.  Thank you for your support!
