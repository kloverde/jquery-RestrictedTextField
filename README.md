RestrictedTextField v1.4
========================

See LICENSE for this software's licensing terms.

RestrictedTextField is a jQuery plugin which uses regular expressions to validate and control input to HTML text fields.  RestrictedTextField has 38 built-in types and also allows you to define your own types.  Validation is performed on keystroke, paste and blur.

You might not need this plugin for your purposes - the HTML5 `pattern` attribute provides most of the same functionality.  If, however, you must support browsers that don't support `pattern`, or if you want to make use of RestrictedTextField's additional features, then perhaps it might be a good fit for you.

The biggest difference between this plugin and `pattern` is that RestrictedTextField allows you to (optionally) suppress invalid keystrokes in real time, rather than allow them into the field to fail validation later.  If you do want to allow invalid characters in, however, you have that option.

If you're only interested in `pattern`-based validation, you might still find RestrictedTextField useful.  It provides the ability to disable its own validation implementation and use `pattern` exclusively.  In this scenario, you'd just be using RestrictedTextField as a repository of pre-written regular expressions.  Although you could just copy the regular expressions from the source code, you might find it easier on code maintenance to not be dropping multiple copies of the same regular expressions into your project.


## Features

* Discard invalid keystrokes or catch a validation failure event to handle it as you wish
* Has 38 built-in types
* Extensible:  define your own types
* Supports (but does not rely on) the HTML 5 `pattern` attribute - can be enabled during initialization
* Money types automatically format on blur to end in a decimal point and two digits


## Built-in Types

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
16.  Strict integers
17.  Strict positive integers
18.  Strict negative integers
19.  Floats
20.  Positive floats
21.  Negative floats
22.  Strict floats
23.  Strict positive floats
24.  Strict negative floats
25.  Money
26.  Positive money
27.  Negative money
28.  Accounting money notation (negatives expressed by wrapping the value in parentheses)
29.  Negative accounting money
30.  American Express
31.  VISA 
32.  MasterCard
33.  Discover
34.  Credit Card (combines all of the individual credit card types above)
35.  [Luhn](https://en.wikipedia.org/wiki/Luhn_algorithm) numbers - note:  if used with `pattern` mode enabled, successful validation is essentially meaningless.
36.  US zip code (5 digits)
37.  US zip code suffix (1-4 digits)
38.  US zip code (5 digits with optional suffix)


## What's the difference between integer/strict integer, float/strict float?

### Integer types:

* Zero can be negative

### Strict integer types:

* Zero cannot be negative

### Float types:

* Integers are valid
* Zero is valid (0, 0.0, .0, 0000.000, etc.)
* Zero can be negative

### Strict float types:

* Values must be floating-point
* Zero is valid (0.0, .0, 0000.000, etc.)
* Zero cannot be negative


## Credit Card types

Each credit card type enforces prefixes and length as specified by https://en.wikipedia.org/wiki/Payment_card_number in December, 2016.  [Luhn validation](https://en.wikipedia.org/wiki/Luhn_algorithm) is also performed.

If you prefer not to use strictly-enforced credit card types (is Wikipedia correct, and is RestrictedTextField up-to-date?), you can use the Luhn type instead.  It performs basic mathematical validation, but that will only tell you that a credit card number is invalid, not that it IS valid.  Full validation would then be shifted to your payment card processor in the form of a rejected transaction -- or, if they provide an API for validation, you could use that, too.


## Configuration

| Property | Description   | Data Type | Valid Values         | Default Value |
| -------- | --------------|---------- |----------------------|---------------|
| `type`   | Text field type.  This is a required setting. | string | alpha, upperAlpha, lowerAlpha, alphaSpace, upperAlphaSpace, lowerAlphaSpace, alphanumeric, upperAlphanumeric, lowerAlphanumeric, alphanumericSpace, upperAlphanumericSpace, lowerAlphanumericSpace, int, positiveInt, negativeInt, strictInt, strictPositiveInt, strictNegativeInt, float, positiveFloat, negativeFloat, strictFloat, strictPositiveFloat, strictNegativeFloat, money, positiveMoney, negativeMoney, accountingMoney, negativeAccountingMoney, americanExpress, visa, masterCard, discover, creditCard, luhnNumber, usZip, usZip5, usZipSuffix | null |
| `preventInvalidInput` | When enabled, invalid keystrokes are ignored (the value of the text field is not updated).  When disabled, invalid keystrokes are not ignored. | boolean | true/false | true |
| `logger` | An optional callback function for logging.  If you want to enable logging, provide a function and then do whatever you wish with the message. | function | A function accepting the log message as a string argument | undefined |
| `usePatternAttr` | When enabled, sets the HTML5 `pattern` attribute on the input field.  Check [caniuse.com](http://caniuse.com/#search=pattern) for browser support.  When pattern mode is enabled, RestrictedTextField does not fire its own validation events, nor does it perform Luhn validation on credit card types or the Luhn type.  Pattern mode is meant to be pure HTML5 behavior.  The only part of RestrictedTextField functionality that remains active is the automatic formatting of money types.| boolean | true/false | false |


## Events

These events are fired based on the state of the text field, but only if `usePatternAttr` is set to false.

| Event name        | Description                                                     |
| ------------------| ----------------------------------------------------------------|
| inputIgnored      | Fires when an invalid keystroke is ignored.  `preventInvalidInput` must be enabled for this event to fire. |
| validationFailed  | If `preventInvalidInput` is disabled, fires when an invalid keystroke is made.  Also fires if validation performed on blur fails. |
| validationSuccess | If `preventInvalidInput` is disabled, fires when the user removes invalid data.  Also fires if validation performed on blur succeeds. |


#### Example


```html
<input type="text" id="field"/>
```
```javascript
$( "#field" ).restrictedTextField( {
   type : "alpha"
} );
```

See `demo/demo.html` for detailed examples.


# Desktop Browser Compatibility

Many other versions of Firefox and Chrome, whether older or newer than what's listed here, are certainly compatible.  Browsers not listed here might work fine, but have not been tested.  If jQuery is supported by the browser, RestrictedTextField should also work.  If you'd like to contribute compatibility information, send it to the e-mail address listed on my GitHub profile and I'll post it here.

* Firefox 49.0.2
* Pale Moon 26.4.1
* Chrome 54.0.2840.99 m
* Edge
* IE 11
* IE 10
* IE 9
* IE 8:  INCOMPATIBLE


# Mobile Browser Compatibility (Android)

Many other versions of the listed browsers, whether older or newer that what's listed here, are certainly compatible.  Browsers not listed here might work fine, but have not been tested.  If jQuery is supported by the browser, RestrictedTextField should also work.  If you'd like to contribute compatibility information, send it to the e-mail address listed on my GitHub profile and I'll post it here.

* Firefox 50.1.0
* Pale Moon 25.9.6
* Chrome 55.0.2883.91
* Opera 4.3 / 41.1.2246.111645 - listed in the Play store as 4.3; reports as 41.1.2246.111645 from within Opera
