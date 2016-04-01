# This project is unfinished and is currently in development.  Check back soon.

RestrictedTextField
===================

See LICENSE for this software's licensing terms.

ResrictedTextField is a jQuery plugin which ...does what it sounds like.


## Features

* Supports the following input formats:

      1.  Alpha strings
      2.  Uppercase alpha strings
      3.  Lowercase alpha strings
      4.  Integers
      5.  Positive integers
      6.  Negative integers
      7.  Floats
      8.  Positive floats
      9.  Negative floats

* Fires user-catchable events on valid/invalid input


## Configuration

| Property | Description   | Data Type | Valid Values         | Default Value |
| -------- | --------------|---------- |----------------------|---------------|
| `type`   | Text field type | string | alpha, upperAlpha, lowerAlpha, int, positiveInt, negativeInt, float, positiveFloat, negativeFloat | null |
| 1preventInvalidInput` | When enabled, 


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
