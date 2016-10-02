# Release 1.1 (UPCOMING RELEASE)

* Fixes for money types
* All money types automatically format on blur:  values will be updated, if necessary, to always end in a period followed by two digits
* Fixed issue with the int types which considered a negative by itself to be a valid value
* Relaxed the int and float types/subtypes, and added strict versions of each.  The relaxed versions accept leading/trailing zeros and negative zero; the strict versions do not.
* Added a logging callback function as a configurable option
* Added Selenium unit tests


# Release 1.0.1 (April 2, 2016)

* Minor under-the-hood improvement.  No change to functionality.


## Release 1.0 (April 2, 2016)

* First release
