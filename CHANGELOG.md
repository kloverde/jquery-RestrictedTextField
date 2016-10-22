# Release 1.1 (October 22, 2016)

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
