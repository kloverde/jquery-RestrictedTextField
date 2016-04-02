# This project is unfinished and is currently under development to rectify compatibility issues with IE.  Check back soon.

RestrictedTextField v1.0
========================

See LICENSE for this software's licensing terms.

ResrictedTextField is a jQuery plugin which enforces data formats on HTML text boxes.  Data which doesn't match the declared format is rejected.


## Features

* Prevent invalid keystrokes or catch a validation failure event to handle it as you wish
* Endlessly extendible:  Define your own types
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
| `preventInvalidInput` | When enabled, invalid keystrokes are prevented.  When disabled, invalid keystrokes are not prevented.  In either case, the events `validationFailure`, `validationSuccess` and `inputInProgress` will fire. | boolean | true/false | true |


#### Example


```html
<input type="text" id="field"/>
```
```javascript
$( "#field" ).restrictedTextField( {
   type: "alpha"
} );
```

See the included HTML file for a complete demo.


## Thanks

Do you like this library?  Want to toss a few bucks my way to say thanks?  I accept donations at https://paypal.me/KurtisLoVerde/5.  Thank you for your support!
