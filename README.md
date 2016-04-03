RestrictedTextField v1.0.1
==========================

See LICENSE for this software's licensing terms.

ResrictedTextField is a jQuery plugin which enforces data formats on HTML text boxes.  Data which doesn't match the declared format is rejected.


## Features

* Prevent invalid keystrokes or catch a validation failure event to handle it as you wish

* Endlessly extendible:  define your own types
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


## Configuration

| Property | Description   | Data Type | Valid Values         | Default Value |
| -------- | --------------|---------- |----------------------|---------------|
| `type`   | Text field type | string | alpha, upperAlpha, lowerAlpha, alphaSpace, upperAlphaSpace, lowerAlphaSpace, alphanumeric, upperAlphanumeric, lowerAlphanumeric, alphanumericSpace, upperAlphanumericSpace, lowerAlphanumericSpace, int, positiveInt, negativeInt, float, positiveFloat, negativeFloat, money, positiveMoney, negativeMoney, accountingMoney, negativeAccountingMoney| null |
| `preventInvalidInput` | When enabled, invalid keystrokes are ignored (the value of the text field is not updated).  When disabled, invalid keystrokes are not ignored. | boolean | true/false | true |


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


## Thanks

Do you like this library?  Want to toss a few bucks my way to say thanks?  I accept donations at https://paypal.me/KurtisLoVerde/5.  Thank you for your support!
