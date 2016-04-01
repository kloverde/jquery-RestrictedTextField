# This project is unfinished and is currently in development.  Check back soon.

RestrictedTextField
===================

See LICENSE for this software's licensing terms.

ResrictedTextField is a jQuery plugin which enforces data formats on HTML text boxes.  Data which doesn't match the declared format is rejected.


## Features

* Prevent invalid keystrokes or catch the validation failure event to deal with it as you wish
* Define your own types
* Supports the following types:

1.  Alpha strings
2.  Uppercase alpha strings
3.  Lowercase alpha strings
4.  Alphanumeric strings
5.  Uppercase alphanumeric strings
6.  Lowercase alphanumeric strings
7.  Integers
8.  Positive integers
9.  Negative integers
10.  Floats
11.  Positive floats
12.  Negative floats
13.  Money
14.  Positive money
15.  Negative money
16.  Accounting money notation (negatives expressed by wrapping the value in parentheses)
17.  Positive and negative money, where negative can be expressed both ways (minus sign and accounting notation)


## Configuration

| Property | Description   | Data Type | Valid Values         | Default Value |
| -------- | --------------|---------- |----------------------|---------------|
| `type`   | Text field type | string | alpha, upperAlpha, lowerAlpha, alphanumeric, upperAlphanumeric, lowerAlphanumeric, int, positiveInt, negativeInt, float, positiveFloat, negativeFloat, money, positiveMoney, negativeMoney, accountingMoney, negativeAccountingMoney| null |
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
