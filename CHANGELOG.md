# Release 1.4 (UPCOMING RELEASE)

* Added support for the HTML5 `pattern` attribute.  Check [caniuse.com](http://caniuse.com/#search=pattern) for browser support.
* Added the ability to save the QUnit test report to a file


# Release 1.3 (January 3, 2017)

* Added zip code types


# Release 1.2 (December 28, 2016)

* Now confirmed to work on mobile!  See README.md for a list of tested browsers.
* Change to event firing:  validation success & validation failure events were firing pre-blur with `preventInvalidInput` enabled.  This was contrary to the documentation, which stated that these events do not fire in that scenario.  The documentation reflected the intended behavior; the events no longer fire in this scenario.
* Added credit card types (AMEX, VISA, MasterCard, Discover), plus a credit card type encompassing each of these types
* Added a [Luhn](https://en.wikipedia.org/wiki/Luhn_algorithm) type
* Updated and redesigned the demo page
* The unit tests are no longer implemented with Java/Selenium/JUnit; they've been re-implemented in JavaScript/QUnit.  This dramatically improves test performance and greatly simplifies running tests (just open the HTML file).


# Release 1.1.1 (November 28, 2016)

* The demo page wasn't working due to an incorrect path to jquery.js.  This has been fixed.  Although the source code, including the JavaScript files themselves, have been updated to say 1.1.1 in their internal code comments, there is no difference in these files from release 1.1.  Only demo.html has been changed.


# Release 1.1 (November 15, 2016)

* Fixes for money types
* All money types automatically format on blur:  values will be updated, if necessary, to always end in a period followed by two digits
* Fixed issue with the int types which considered a negative sign by itself to be a valid value
* Relaxed the int and float types/subtypes, and added strict versions of each.  The relaxed versions accept leading/trailing zeros and negative zero; the strict versions do not.
* Added a logging callback function as a configurable option
* Throws an exception if invoked on something other than an input element
* Added Selenium unit tests


# Release 1.0.1 (April 2, 2016)

* Minor under-the-hood improvement.  No change to functionality.


## Release 1.0 (April 2, 2016)

* First release
