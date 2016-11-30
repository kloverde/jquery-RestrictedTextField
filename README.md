RestrictedTextField v1.2 (UPCOMING RELEASE)
===========================================

## You have stumbled upon a development branch.  Have a look around if you want, but only use code that has been posted on the Releases page.

See LICENSE for this software's licensing terms.

A jQuery plugin which uses regular expressions to validate and control input to HTML text fields.  Using 34 built-in types or types you define yourself, it allows you to prevent invalid keystrokes or to allow them into the field for later validation.  Fields are always validated on blur.


## Features

* Prevent invalid keystrokes or catch a validation failure event to handle it as you wish
* Has 34 built-in types
* Extendible:  define your own types
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

Credit Card Numbers (enforces number prefixes, card number lengths and performs Luhn validation):

30.  American Express
31.  VISA 
32.  MasterCard
33.  Discover
34.  "Credit Card" type (covers all of the individual card types above)


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


## Configuration

| Property | Description   | Data Type | Valid Values         | Default Value |
| -------- | --------------|---------- |----------------------|---------------|
| `type`   | Text field type.  This is a required setting. | string | alpha, upperAlpha, lowerAlpha, alphaSpace, upperAlphaSpace, lowerAlphaSpace, alphanumeric, upperAlphanumeric, lowerAlphanumeric, alphanumericSpace, upperAlphanumericSpace, lowerAlphanumericSpace, int, positiveInt, negativeInt, strictInt, strictPositiveInt, strictNegativeInt, float, positiveFloat, negativeFloat, strictFloat, money, positiveMoney, negativeMoney, accountingMoney, negativeAccountingMoney| null |
| `preventInvalidInput` | When enabled, invalid keystrokes are ignored (the value of the text field is not updated).  When disabled, invalid keystrokes are not ignored. | boolean | true/false | true |
| `logger` | An optional callback function for logging.  If you want to enable logging, provide a function and then do whatever you wish with the message. | function | A function accepting the log message as a string argument | undefined |


## Events

These events are fired based on the state of the text field.

| Event name        | Description                                                     |
| ------------------| ----------------------------------------------------------------|
| inputIgnored      | Fires when an invalid keystroke is ignored.  `preventInvalidInput` must be enabled for this event to fire. |
| validationFailed  | Fires when an invalid keystroke is made when `preventInvalidInput` is disabled.  Also fires if validation performed on blur() fails. |
| validationSuccess | Fires when the user removes invalid data when `preventInvalidInput` is disabled.  Also fires if validation performed on blur() succeeds. |


#### Example


```html
<input type="text" id="field"/>
```
```javascript
$( "#field" ).restrictedTextField( {
   type : "alpha"
} );
```

See `demo/demo.html` for complete examples.


# Desktop Browser Compatibility

This plugin has only been tested on desktop browsers.  Mobile testing might happen in the future.  As for desktop browsers, many other versions of Firefox and Chrome, whether older or newer than what's listed here, are certainly compatible.  Browsers not listed here might work fine, but have not been tested.

* Firefox 49.0.2:  Compatible
* Chrome 54.0.2840.99 m:  Compatible
* Edge:  Compatible
* IE 11:  Compatible
* IE 10:  Compatible
* IE 9:  Compatible
* IE 8:  INCOMPATIBLE


# Running the Unit Tests

[Selenium](http://www.seleniumhq.org) is used for unit testing in order to generate true keypresses in a text field.  A JavaScript-based framework (such as QUnit, etc.) would have made life simpler, but synthetic JavaScript events don't carry out the actual actions associated with them.  In other words, simulating a keypress fires the correct events, but doesn't result in actual text being written to a text field.  This is a security restriction of JavaScript.  As a result, this project uses Selenium and JUnit.

1.  Install the following build dependencies:
  * [Node.js](https://nodejs.org/en)
  * [Gradle](http://gradle.org)
  * [Java JDK](http://www.oracle.com/technetwork/java/javase/index.html) version 8 or later
  * [BuildScripts](https://github.com/kloverde/BuildScripts)
2.  Modify the `buildScriptsDir` property in `SeleniumTester/gradle.properties` to reflect the location of BuildScripts on your filesystem
3.  Download the appropriate browser drivers for your system at [seleniumhq.org](http://www.seleniumhq.org).  Once you've downloaded them, update `geckoDriverPath`, `edgeDriverPath`, `ieDriverPath` and `chromeDriverPath` in `SeleniumTester/gradle.properties` with their paths.
4.  If you're testing in IE, set Protected Mode to the same value in all zones (it doesn't matter whether it's set to enabled or disabled, just that it's the same for all).  See [here](http://jimevansmusic.blogspot.com/2012/08/youre-doing-it-wrong-protected-mode-and.html) for more information.  If that page disappears from the Web, see [the Wayback Machine's copy](http://web.archive.org/web/20151026094711/http://jimevansmusic.blogspot.com/2012/08/youre-doing-it-wrong-protected-mode-and.html).
5.  Set your browsers' zoom levels to 100%.  If you don't, Selenium will throw an exception, at least for IE.
6.  Update the `browsers` property in `SeleniumTester/gradle.properties` to reflect which browsers you'll be testing with.  This is explained further by documentation found in the properties file.
7.  Now, from a command prompt:
  1.  `cd` to the project root (`jquery-RestrictedTextField`)
  2.  Type `npm install`
  3.  Type `npm test`


#### UNIT TESTS GOTCHAS:

* #### The Selenium project's 64-bit IE driver is broken for IE 10 and 11, and according to a Selenium contributor, is unfixable.  See [here](https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/5116) and [here](http://jimevansmusic.blogspot.com/2014/09/screenshots-sendkeys-and-sixty-four.html).  If you're testing on a 64-bit version of Windows with IE 10 or 11, use the 32-bit driver instead.  Its performance is still quite poor, but is far better than the 64-bit driver.

* #### If you're using Windows and if you haven't used Selenium before, Windows Firewall will pop up an alert asking whether to allow driver servers to listen for connections.  You need to grant these permissions.  During this time, one or more of the unit tests will fail.  Simply re-run the tests after the permission has been granted.

* #### If you're running the unit tests in Firefox, you must use Firefox 48 or later.  Starting with Firefox 48, Selenium is required to use the Marionette/Gecko driver, and my code is written to initialize that particular driver.  Although RestrictedTextField itself is supported on older Firefox releases, the unit tests are not.

* #### If you're running the unit tests in Firefox, the last known Gecko driver which worked was v0.10.0.  Sometime after that, something happened in the Gecko driver which messes up the tests' ability to trigger blur events, which results in practically every test failing.  Version 0.11.1 (November, 2016) is confirmed to cause this problem.  No later version of the Gecko driver has been tested.

* #### It's critical that you do not interact with the machine in any way while tests are running.  Being that this plugin is event-driven, any action that could cause an event to be fired unexpectedly could cause tests to fail.  This includes human actions, but also includes applications popping up notifications or asserting themselves in other ways.  To the extent possible, you should take steps to minimize this possibility.

* #### The tests fail in Edge if Edge doesn't have focus.  Sometimes Edge opens behind another window (for example, the console which you might have used to invoke the tests).


## Thanks

Do you like this software?  Want to toss a few bucks my way to say thanks?  I accept donations at https://paypal.me/KurtisLoVerde/10.  Thank you for your support!
