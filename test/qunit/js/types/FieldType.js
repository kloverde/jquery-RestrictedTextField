/*
 * RestrictedTextField
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * Copyright (c) 2016, Kurtis LoVerde
 * All rights reserved.
 *
 * Donations:  https://paypal.me/KurtisLoVerde/10
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of the copyright holder nor the names of its
 *        contributors may be used to endorse or promote products derived from
 *        this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

"use strict";

var FieldType = {
   ALPHA                     : ["alpha", "Uppercase/lowercase letters"],
   UPPER_ALPHA               : ["upperAlpha", "Uppercase letters"],
   LOWER_ALPHA               : ["lowerAlpha", "Lowercase letters"],
   ALPHA_SPACE               : ["alphaSpace", "Uppercase/lowercase letters, space"],
   UPPER_ALPHA_SPACE         : ["upperAlphaSpace", "Uppercase letters, space"],
   LOWER_ALPHA_SPACE         : ["lowerAlphaSpace", "Lowercase letters, space"],
   ALPHANUMERIC              : ["alphanumeric", "Uppercase/lowercase letters, integers"],
   UPPER_ALPHANUMERIC        : ["upperAlphanumeric", "Uppercase letters, integers"],
   LOWER_ALPHANUMERIC        : ["lowerAlphanumeric", "Lowercase letters, integers"],
   ALPHANUMERIC_SPACE        : ["alphanumericSpace", "Uppercase/lowercase letters, integers, space"],
   UPPER_ALPHANUMERIC_SPACE  : ["upperAlphanumericSpace", "Uppercase letters, integers, space"],
   LOWER_ALPHANUMERIC_SPACE  : ["lowerAlphanumericSpace", "Lowercase letters, integers, space"],
   INT                       : ["int", "Positive/negative integers, zero.  Zero can be negative."],
   POSITIVE_INT              : ["positiveInt", "Positive integers, zero"],
   NEGATIVE_INT              : ["negativeInt", "Negative integers, zero.  Zero can be negative."],
   STRICT_INT                : ["strictInt", "Positive/negative integers, zero.  Zero cannot be negative."],
   STRICT_POSITIVE_INT       : ["strictPositiveInt", "Positive integers, zero"],
   STRICT_NEGATIVE_INT       : ["strictNegativeInt", "Negative integers, zero.  Zero cannot be negative."],
   FLOAT                     : ["float", "Positive/negative floats, positive/negative integers, zero.  Zero can be negative."],
   POSITIVE_FLOAT            : ["positiveFloat", "Positive floats, positive integers, zero"],
   NEGATIVE_FLOAT            : ["negativeFloat", "Negative floats, negative integers, zero.  Zero can be negative."],
   STRICT_FLOAT              : ["strictFloat", "Positive/negative floats, zero as a float.  Zero cannot be negative."],
   STRICT_POSITIVE_FLOAT     : ["strictPositiveFloat", "Positive floats, zero as a float"],
   STRICT_NEGATIVE_FLOAT     : ["strictNegativeFloat", "Negative floats, zero as a float.  Zero cannot be negative."],
   MONEY                     : ["money", "Positive/negative integer followed by a period, followed by 1-2 integers"],
   POSITIVE_MONEY            : ["positiveMoney", "Positive integer amount followed by a period, followed by 1-2 integers"],
   NEGATIVE_MONEY            : ["negativeMoney", "Negative integer amount followed by a period, followed by 1-2 integers"],
   ACCOUNTING_MONEY          : ["accountingMoney", "For positive amounts, see the positiveMoney type.  For negative amounts, the value is wrapped in parentheses instead of using a negative sign."],
   NEGATIVE_ACCOUNTING_MONEY : ["negativeAccountingMoney", "An opening parenthesis followed by an integer amount, followed by a period, followed by 1-2 integers, followed by a closing parenthesis"],
   AMERICAN_EXPRESS          : ["americanExpress", "American Express credit card numbers"],
   VISA                      : ["visa", "VISA credit card numbers conforming to https://en.wikipedia.org/wiki/Payment_card_number (December, 2016) and https://en.wikipedia.org/wiki/Luhn_algorithm"],
   MASTERCARD                : ["masterCard", "MasterCard credit card numnbers conforming to https://en.wikipedia.org/wiki/Payment_card_number (December, 2016) and https://en.wikipedia.org/wiki/Luhn_algorithm"],
   DISCOVER                  : ["discover", "Discover credit card numbers conforming to https://en.wikipedia.org/wiki/Payment_card_number (December, 2016) and https://en.wikipedia.org/wiki/Luhn_algorithm"],
   CREDIT_CARD               : ["creditCard", "Combines all of the individual credit card types provided by RestrictedTextField"],
   LUHN_NUMBER               : ["luhnNumber", "Any value which validates according to the Luhn algorithm (see https://en.wikipedia.org/wiki/Luhn_algorithm)"],
   US_ZIP                    : ["usZip", "5-9 digit United States zip code.  If more than 5, has a hyphen after the fifth digit, followed by 1-4 digits."],
   US_ZIP5                   : ["usZip5", "5-digit United States zip code"],
   US_ZIP_SUFFIX             : ["usZipSuffix", "1-4 digit United States zip code suffix"],
};
