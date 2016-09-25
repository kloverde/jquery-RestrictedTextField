/*
 * RestrictedTextField v1.1
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

package org.loverde.jquery.restrictedtextfield.selenium.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.loverde.jquery.restrictedtextfield.selenium.Characters;
import org.loverde.jquery.restrictedtextfield.selenium.Event;
import org.loverde.jquery.restrictedtextfield.selenium.FieldType;
import org.loverde.jquery.restrictedtextfield.selenium.driver.DriverFactory;
import org.loverde.jquery.restrictedtextfield.selenium.util.StringUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.runners.Parameterized;


@RunWith( Parameterized.class )
public abstract class AbstractTest {

   // Begin parameters

   private FieldType fieldType;

   private String testName,
                  input,
                  expectedValue;

   private boolean ignoreInvalidInput;

   private Event expectedEventOnBlur;

   // End parameters

   private static Properties props;

   private static final String PROPERTIES_FILENAME = "gradle.properties";

   public static final String APP_PROP_IE_DRIVER_PATH        = "ieDriverPath",
                              APP_PROP_GECKO_DRIVER_PATH     = "geckoDriverPath",
                              APP_PROP_CHROME_DRIVER_PATH    = "chromeDriverPath",
                              APP_PROP_URL                   = "url";


   private static WebDriver driver;

   private WebElement field, body;

   private static Class<?> lastClass;
   private static File logDirectory;

   public static final List<Object[]> data;


   @Parameters( name = "{1}" )
   public static Collection<Object[]> data() {
      return data;
   }

   static {
      // [x][0] : Field type
      // [x][1] : Test name
      // [x][2] : Flag for ignoring invalid keystrokes (true = enabled)
      // [x][3] : Input
      // [x][4] : Expected final value of text field
      // [x][5] : Expected event triggered on blur
      final Object[][] d = new Object[][] {

         // First set:  ignore invalid input

         { FieldType.ALPHA,                    "ignore_alpha_all",                   true,  Characters.ALL,  Characters.ALPHA,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHA,              "ignore_upperAlpha_all",              true,  Characters.ALL,  Characters.UPPER_ALPHA,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHA,              "ignore_lowerAlpha_all",              true,  Characters.ALL,  Characters.LOWER_ALPHA,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHA_SPACE,              "ignore_alphaSpace_all",              true,  Characters.ALL,  Characters.ALPHA_SPACE,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHA_SPACE,        "ignore_upperAlphaSpace_all",         true,  Characters.ALL,  Characters.UPPER_ALPHA_SPACE,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHA_SPACE,        "ignore_lowerAlphaSpace_all",         true,  Characters.ALL,  Characters.LOWER_ALPHA_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHANUMERIC,             "ignore_alphanumeric_all",            true,  Characters.ALL,  Characters.ALPHANUMERIC,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHANUMERIC,       "ignore_upperAlphanumeric_all",       true,  Characters.ALL,  Characters.UPPER_ALPHANUMERIC,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHANUMERIC,       "ignore_lowerAlphanumeric_all",       true,  Characters.ALL,  Characters.LOWER_ALPHANUMERIC,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHANUMERIC_SPACE,       "ignore_alphanumericSpace_all",       true,  Characters.ALL,  Characters.ALPHANUMERIC_SPACE,        Event.VALIDATION_SUCCESS },
         { FieldType.UPPER_ALPHANUMERIC_SPACE, "ignore_upperAlphanumericSpace_all",  true,  Characters.ALL,  Characters.UPPER_ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },
         { FieldType.LOWER_ALPHANUMERIC_SPACE, "ignore_lowerAlphanumericSpace_all",  true,  Characters.ALL,  Characters.LOWER_ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.INT,                  "ignore_int_all",                         true,  Characters.ALL,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "ignore_int_multipleZero",                true,  "000",   "000",    Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "ignore_int_positive_leadingZero",        true,  "001",   "001",   Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "ignore_int_negative_negativeZero",       true,  "-0",    "-0",    Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "ignore_int_negative",                    true,  "-123",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "ignore_int_doubleNegative",              true,  "--1",   "-1",    Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "ignore_int_floatingPoint",               true,  "1.23",  "123",   Event.VALIDATION_SUCCESS },

         { FieldType.POSITIVE_INT,         "ignore_positiveInt",                     true,  Characters.ALL_EXCEPT_MINUS,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "ignore_positiveInt_multipleZero",        true,  "000",    "000",   Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "ignore_positiveInt_leadingZero",         true,  "001",    "001",  Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "ignore_positiveInt_negative",            true,  "-1",     "1",    Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "ignore_positiveInt_floatingPoint",       true,  "1.23",   "123",  Event.VALIDATION_SUCCESS },

         { FieldType.NEGATIVE_INT,         "ignore_negativeInt",                     true,  "-" + Characters.ALPHANUMERIC,  "-" + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "ignore_negativeInt_positives",           true,  Characters.NUMBERS_END_ZERO,    "0",                                Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "ignore_negativeInt_negativeZero",        true,  "-0",     "-0",    Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "ignore_negativeInt_doubleDash1",         true,  "--123",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "ignore_negativeInt_doubleDash2",         true,  "-12-3",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "ignore_negativeInt_floatingPoint",       true,  "-1.23",  "-123",  Event.VALIDATION_SUCCESS },

         { FieldType.STRICT_INT,           "ignore_strictInt_all",                      true,  Characters.ALL,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "ignore_strictInt_multipleZero",             true,  "000",   "0",     Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "ignore_strictInt_positive_leadingZero",     true,  "001",   "0",     Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "ignore_strictInt_negative_negativeZero",    true,  "-0",    "-",     Event.VALIDATION_FAILED },
         { FieldType.STRICT_INT,           "ignore_strictInt_negative",                 true,  "-123",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "ignore_strictInt_doubleNegative",           true,  "--1",   "-1",    Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "ignore_strictInt_floatingPoint",            true,  "1.23",  "123",   Event.VALIDATION_SUCCESS },

         { FieldType.STRICT_POSITIVE_INT,  "ignore_strictPositiveInt",                  true,  Characters.ALL_EXCEPT_MINUS,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_INT,  "ignore_strictPositiveInt_multipleZero",     true,  "000",    "0",    Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_INT,  "ignore_strictPositiveInt_leadingZero",      true,  "001",    "0",    Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_INT,  "ignore_strictPositiveInt_negative",         true,  "-1",     "1",    Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_INT,  "ignore_strictPositiveInt_floatingPoint",    true,  "1.23",   "123",  Event.VALIDATION_SUCCESS },

         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt",                  true,  "-" + Characters.ALPHANUMERIC,  "-" + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt_positives",        true,  Characters.NUMBERS_END_ZERO,    "0",   Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt_zero",             true,  "0",      "0",     Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt_negativeZero",     true,  "-0",     "-",     Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt_doubleDash1",      true,  "--123",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt_doubleDash2",      true,  "-12-3",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_INT,  "ignore_strictNegativeInt_noFloatingPoint",  true,  "-1.23",  "-123",  Event.VALIDATION_SUCCESS },

         { FieldType.FLOAT,  "ignore_float_all",                  true,  Characters.ALL_EXCEPT_MINUS + "." + Characters.ALL_EXCEPT_MINUS,
                                                                         Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_posInt",               true,  "123",           "123",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_negInt",               true,  "-123",          "-123",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_negFloat",             true,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_zero1",                true,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_zero2",                true,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_zero3",                true,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_leadingDoubleZero1",   true,  "00",            "00",            Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_leadingDoubleZero2",   true,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_extraTrailingZero1",   true,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_extraTrailingZero2",   true,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_extraTrailingZero3",   true,  "12.34500",      "12.34500",      Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_leadingDoubleZeros3",  true,  "-00.0",         "-00.0",         Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_negativeZero1",        true,  "-0",            "-0",            Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_negativeZero2",        true,  "-.0",           "-.0",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_negativeZero3",        true,  "-0.000000",     "-0.000000",     Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_negativeZero4",        true,  "-0.0",          "-0.0",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_dash",                 true,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "ignore_float_doubleDash",           true,  "--",            "-",             Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "ignore_float_dashDot",              true,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "ignore_float_endInDot",             true,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "ignore_float_dashInWrongPlace1",    true,  "1-.2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_dashInWrongPlace2",    true,  "1.-2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_dashInWrongPlace3",    true,  "1.2-",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_dashInWrongPlace4",    true,  "1.-0",          "1.0",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "ignore_float_doubleDot2",           true,  "1..2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_dotsEverywhere",       true,  ".1.2.",         ".12",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "ignore_float_forceBlank",           true,  "a",             "",              Event.VALIDATION_SUCCESS },

         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_all",  true,  Characters.ALL_EXCEPT_MINUS + "." + Characters.ALL_EXCEPT_MINUS,
                                                                          Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_posInt",               true,  "123",           "123",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_negInt",               true,  "-123",          "123",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_negFloat",             true,  "-1023.456789",  "1023.456789",   Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_zero1",                true,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_zero2",                true,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_zero3",                true,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_leadingDoubleZero1",   true,  "00",            "00",            Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_leadingDoubleZero2",   true,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_extraTrailingZero1",   true,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_extraTrailingZero2",   true,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_extraTrailingZero3",   true,  "12.34500",      "12.34500",      Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_leadingDoubleZeros3",  true,  "-00.0",         "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_negativeZero1",        true,  "-0",            "0",             Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_negativeZero2",        true,  "-.0",           ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_negativeZero3",        true,  "-0.000000",     "0.000000",      Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_negativeZero4",        true,  "-0.0",          "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dash",                 true,  "-",             "",              Event.VALIDATION_SUCCESS },
         //{ FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_doubleDash",           true,  "--",            "",              Event.VALIDATION_SUCCESS },  // already testing standalone hyphen in this set
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dashDot",              true,  "-.",            ".",             Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_endInDot",             true,  "-1.",           "1.",            Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dashInWrongPlace1",    true,  "1-.2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dashInWrongPlace2",    true,  "1.-2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dashInWrongPlace3",    true,  "1.2-",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dashInWrongPlace4",    true,  "1.-0",          "1.0",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_doubleDot2",           true,  "1..2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_dotsEverywhere",       true,  ".1.2.",         ".12",           Event.VALIDATION_SUCCESS },
         //{ FieldType.POSITIVE_FLOAT,  "ignore_positiveFloat_forceBlank",           true,  "a",             "",              Event.VALIDATION_SUCCESS },  // already testing blank in this set

         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_all",  true,  "-" + Characters.ALL_EXCEPT_MINUS + "." + Characters.ALL_EXCEPT_MINUS,
                                                                          "-" + Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_posInt",               true,  "123",           "",              Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_negInt",               true,  "-123",          "-123",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_negFloat",             true,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_zero1",                true,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_zero2",                true,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_zero3",                true,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_leadingDoubleZero1",   true,  "00",            "00",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_leadingDoubleZero2",   true,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_extraTrailingZero1",   true,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_extraTrailingZero2",   true,  "12.3450",       ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_extraTrailingZero3",   true,  "-12.34500",     "-12.34500",     Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_leadingDoubleZeros3",  true,  "-00.0",         "-00.0",         Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_negativeZero1",        true,  "-0",            "-0",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_negativeZero2",        true,  "-.0",           "-.0",           Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_negativeZero3",        true,  "-0.000000",     "-0.000000",     Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_negativeZero4",        true,  "-0.0",          "-0.0",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dash",                 true,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_doubleDash",           true,  "--",            "-",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dashDot",              true,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_endInDot",             true,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dashInWrongPlace1",    true,  "1-.2",          "-.2",           Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dashInWrongPlace2",    true,  "1.-2",          ".",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dashInWrongPlace3",    true,  "1.2-",          ".",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dashInWrongPlace4",    true,  "1.-0",          ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_doubleDot2",           true,  "1..2",          ".",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_dotsEverywhere",       true,  ".1.2.",         ".",             Event.VALIDATION_FAILED },
         //{ FieldType.NEGATIVE_FLOAT,  "ignore_negativeFloat_forceBlank",           true,  "a",             "",              Event.VALIDATION_SUCCESS },  // already testing blank in this set

         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_all",                  true,  Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO,
                                                                                      Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_posInt",               true,  "123",           "123",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negInt",               true,  "-123",          "-123",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negFloat",             true,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_zero1",                true,  "0",             "0",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_zero2",                true,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_zero3",                true,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingZero1",         true,  "00",            "00",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingZero2",         true,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingZero3",         true,  "0123.456",      "0123.456",      Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_extraTrailingZero1",   true,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_extraTrailingZero2",   true,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_leadingDoubleZeros3",  true,  "-00.0",         "-00.0",         Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negativeZero1",        true,  "-0",            "-0",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_negativeZero2",        true,  "-0.00",         "-0.00",         Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dash",                 true,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_doubleDash",           true,  "--",            "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashDot",              true,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_endInDot",             true,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace1",    true,  "1-.2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace2",    true,  "1.-2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace3",    true,  "1.2-",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dashInWrongPlace4",    true,  "1.-0",          "1.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_doubleDot2",           true,  "1..2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_dotsEverywhere",       true,  ".1.2.",         ".12",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "ignore_strictFloat_forceBlank",           true,  "a",             "",              Event.VALIDATION_SUCCESS },

         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_all",                  true,  Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_START_ZERO,
                                                                                                       Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_START_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_posInt",               true,  "123",           "123",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_negInt",               true,  "-123",          "123",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_negFloat",             true,  "-1023.456789",  "1023.456789",   Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_zero1",                true,  "0",             "0",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_zero2",                true,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_zero3",                true,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_leadingZero1",         true,  "00",            "00",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_leadingZero2",         true,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_leadingZero3",         true,  "0123.456",      "0123.456",      Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_extraTrailingZero1",   true,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_extraTrailingZero2",   true,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_leadingDoubleZeros3",  true,  "-00.0",         "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_negativeZero",         true,  "-0.000000",     "0.000000",      Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_dash",                 true,  "-",             "",              Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_dashDot",              true,  ".",             ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_doubleDot2",           true,  "1..2",          "1.2",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_dotsEverywhere",       true,  ".1.2.",         ".12",           Event.VALIDATION_SUCCESS },
         //{ FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_forceBlank",           true,  "a",             "",              Event.VALIDATION_SUCCESS },  // already testing blank in this set

         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_all",                  true,  "-" + Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_START_ZERO,
                                                                                                       "-" + Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_START_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_posInt",               true,  "123",           "",              Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_negInt",               true,  "-123",          "-123",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_zero1",                true,  "0",             "0",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_zero2",                true,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_zero3",                true,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_leadingZero1",         true,  "00",            "00",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_leadingZero2",         true,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_leadingZero3",         true,  "0123.456",      "0.",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_extraTrailingZero1",   true,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_extraTrailingZero2",   true,  "12.3450",       ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_leadingDoubleZeros3",  true,  "-00.0",         "-00.0",         Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_negativeZero",         true,  "-0.000000",     "-0.000000",     Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_dash",                 true,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_dashDot",              true,  ".",             ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_doubleDot1",           true,  "..",            ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_doubleDot2",           true,  "-1..2",         "-1.2",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_dotsEverywhere",       true,  "-.1.2.",        "-.12",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "ignore_strictNegativeFloat_letter2",              true,  "1.a",           ".",             Event.VALIDATION_FAILED },
         //{ FieldType.STRICT_POSITIVE_FLOAT,  "ignore_strictPositiveFloat_forceBlank",           true,  "a",             "",              Event.VALIDATION_SUCCESS },  // already testing blank in this set

         // Second set:  don't ignore invalid input

         { FieldType.ALPHA,         "noIgnore_alpha_invalid1",              false,  "!",                     "!",                     Event.VALIDATION_FAILED },
         { FieldType.ALPHA,         "noIgnore_alpha_invalid2",              false,  "1",                     "1",                     Event.VALIDATION_FAILED },
         { FieldType.ALPHA,         "noIgnore_alpha_invalid3",              false,  " ",                     " ",                     Event.VALIDATION_FAILED },
         { FieldType.ALPHA,         "noIgnore_alpha_valid1",                false,  Characters.UPPER_ALPHA,  Characters.UPPER_ALPHA,  Event.VALIDATION_SUCCESS },
         { FieldType.ALPHA,         "noIgnore_alpha_valid2",                false,  Characters.LOWER_ALPHA,  Characters.LOWER_ALPHA,  Event.VALIDATION_SUCCESS },
         { FieldType.ALPHA,         "noIgnore_alpha_valid3",                false,  Characters.ALPHA,        Characters.ALPHA,        Event.VALIDATION_SUCCESS },

         { FieldType.UPPER_ALPHA,   "noIgnore_upperAlpha_invalid1",         false,  "@",                     "@",                     Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA,   "noIgnore_upperAlpha_invalid2",         false,  "2",                     "2",                     Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA,   "noIgnore_upperAlpha_invalid3",         false,  " ",                     " ",                     Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA,   "noIgnore_upperAlpha_invalid4",         false,  "a",                     "a",                     Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA,   "noIgnore_upperAlpha_valid",            false,  Characters.UPPER_ALPHA,  Characters.UPPER_ALPHA,  Event.VALIDATION_SUCCESS },

         { FieldType.LOWER_ALPHA,   "noIgnore_lowerAlpha_invalid1",         false,  "#",                     "#",                     Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA,   "noIgnore_lowerAlpha_invalid2",         false,  "3",                     "3",                     Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA,   "noIgnore_lowerAlpha_invalid3",         false,  " ",                     " ",                     Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA,   "noIgnore_lowerAlpha_invalid4",         false,  Characters.UPPER_ALPHA,  Characters.UPPER_ALPHA,  Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA,   "noIgnore_lowerAlpha_valid",            false,  Characters.LOWER_ALPHA,  Characters.LOWER_ALPHA,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHA_SPACE,   "noIgnore_alphaSpace_invalid1",         false,  "%",                     "%",                     Event.VALIDATION_FAILED },
         { FieldType.ALPHA_SPACE,   "noIgnore_alphaSpace_invalid2",         false,  "5",                     "5",                     Event.VALIDATION_FAILED },
         { FieldType.ALPHA_SPACE,   "noIgnore_alphaSpace_valid",            false,  Characters.ALPHA_SPACE,  Characters.ALPHA_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.UPPER_ALPHA_SPACE,   "noIgnore_upperAlphaSpace_invalid1",    false,  "^",                            "^",                           Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA_SPACE,   "noIgnore_upperAlphaSpace_invalid2",    false,  "6",                            "6",                           Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA_SPACE,   "noIgnore_upperAlphaSpace_invalid3",    false,  "b",                            "b",                           Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHA_SPACE,   "noIgnore_upperAlphaSpace_valid",       false,  Characters.UPPER_ALPHA_SPACE,   Characters.UPPER_ALPHA_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.LOWER_ALPHA_SPACE,   "noIgnore_lowerAlphaSpace_invalid1",    false,  "&",                            "&",                           Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA_SPACE,   "noIgnore_lowerAlphaSpace_invalid2",    false,  "7",                            "7",                           Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA_SPACE,   "noIgnore_lowerAlphaSpace_invalid3",    false,  "C",                            "C",                           Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHA_SPACE,   "noIgnore_lowerAlphaSpace_valid",       false,  Characters.LOWER_ALPHA_SPACE,   Characters.LOWER_ALPHA_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHANUMERIC,        "noIgnore_alphanumeric_invalid1",       false,  "*",                            "*",                           Event.VALIDATION_FAILED },
         { FieldType.ALPHANUMERIC,        "noIgnore_alphanumeric_valid",          false,  Characters.ALPHANUMERIC,        Characters.ALPHANUMERIC,       Event.VALIDATION_SUCCESS },

         { FieldType.UPPER_ALPHANUMERIC,  "noIgnore_upperAlphanumeric_invalid1",  false,  "d",                            "d",                            Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHANUMERIC,  "noIgnore_upperAlphanumeric_invalid2",  false,  "/",                            "/",                            Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHANUMERIC,  "noIgnore_upperAlphanumeric_valid",     false,  Characters.UPPER_ALPHANUMERIC,  Characters.UPPER_ALPHANUMERIC,  Event.VALIDATION_SUCCESS },

         { FieldType.LOWER_ALPHANUMERIC,  "noIgnore_lowerAlphanumeric_invalid1",  false,  "E",                            "E",                            Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHANUMERIC,  "noIgnore_lowerAlphanumeric_invalid2",  false,  "?",                            "?",                            Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHANUMERIC,  "noIgnore_lowerAlphanumeric_valid",     false,  Characters.LOWER_ALPHANUMERIC,  Characters.LOWER_ALPHANUMERIC,  Event.VALIDATION_SUCCESS },

         { FieldType.ALPHANUMERIC_SPACE,  "noIgnore_alphanumericSpace_invalid1",  false,  ";",                            ";",                            Event.VALIDATION_FAILED },
         { FieldType.ALPHANUMERIC_SPACE,  "noIgnore_alphanumericSpace_all",       false,  Characters.ALPHANUMERIC_SPACE,  Characters.ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.UPPER_ALPHANUMERIC_SPACE,  "noIgnore_upperAlphanumericSpace_invalid1",  false,  "f",                 "f",                            Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHANUMERIC_SPACE,  "noIgnore_upperAlphanumericSpace_invalid2",  false,  ":",                 ":",                            Event.VALIDATION_FAILED },
         { FieldType.UPPER_ALPHANUMERIC_SPACE,  "noIgnore_upperAlphanumericSpace_valid",     false,  Characters.UPPER_ALPHANUMERIC_SPACE,  Characters.UPPER_ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.LOWER_ALPHANUMERIC_SPACE,  "noIgnore_lowerAlphanumericSpace_invalid1",  false,  "G",                 "G",                            Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHANUMERIC_SPACE,  "noIgnore_lowerAlphanumericSpace_invalid2",  false,  "'",                 "'",                            Event.VALIDATION_FAILED },
         { FieldType.LOWER_ALPHANUMERIC_SPACE,  "noIgnore_lowerAlphanumericSpace_valid",     false,  Characters.LOWER_ALPHANUMERIC_SPACE,  Characters.LOWER_ALPHANUMERIC_SPACE,  Event.VALIDATION_SUCCESS },

         { FieldType.INT,                  "noIgnore_int",                             false,  Characters.NUMBERS_END_ZERO,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "noIgnore_int_multipleZero",                false,  "000",   "000",   Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "noIgnore_int_positive_leadingZero",        false,  "001",   "001",   Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "noIgnore_int_negative_negativeZero",       false,  "-0",    "-0",    Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "noIgnore_int_negative",                    false,  "-123",  "-123",  Event.VALIDATION_SUCCESS },
         { FieldType.INT,                  "noIgnore_int_doubleNegative",              false,  "--1",   "--1",   Event.VALIDATION_FAILED },
         { FieldType.INT,                  "noIgnore_int_floatingPoint",               false,  "1.23",  "1.23",  Event.VALIDATION_FAILED },
         { FieldType.INT,                  "noIgnore_int_letter",                      false,  "h",     "h",     Event.VALIDATION_FAILED },

         { FieldType.POSITIVE_INT,         "noIgnore_positiveInt",                     false,  Characters.NUMBERS_END_ZERO,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "noIgnore_positiveInt_multipleZero",        false,  "000",    "000",    Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "noIgnore_positiveInt_leadingZero",         false,  "001",    "001",    Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_INT,         "noIgnore_positiveInt_negative",            false,  "-1",     "-1",     Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_INT,         "noIgnore_positiveInt_floatingPoint",       false,  "1.23",   "1.23",   Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_INT,         "noIgnore_positiveInt_letter",              false,  "i",      "i",      Event.VALIDATION_FAILED },

         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt",                     false,  "-" + Characters.NUMBERS_END_ZERO,  "-" + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_multipleZero1",       false,  "000",    "000",    Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_multipleZero2",       false,  "-000",   "-000",   Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_positive",            false,  "1",      "1",      Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_negativeZero",        false,  "-0",     "-0",     Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_doubleDash1",         false,  "--123",  "--123",  Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_doubleDash2",         false,  "-12-3",  "-12-3",  Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_floatingPoint",       false,  "-1.23",  "-1.23",  Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_INT,         "noIgnore_negativeInt_letter",              false,  "j",      "j",      Event.VALIDATION_FAILED },

         { FieldType.STRICT_INT,           "noIgnore_strictInt_positive",                 false,  Characters.NUMBERS_END_ZERO,       Characters.NUMBERS_END_ZERO,        Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_negative",                 false,  "-" + Characters.NUMBERS_END_ZERO, "-" + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_letter",                   false,  "k",                               "k",                                Event.VALIDATION_FAILED },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_doubleZero",               false,  "00",                              "00",                               Event.VALIDATION_FAILED },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_positive_leadingZero",     false,  "01",                              "01",                               Event.VALIDATION_FAILED },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_negative_negativeZero",    false,  "-0",                              "-0",                               Event.VALIDATION_FAILED },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_doubleNegative",           false,  "--1",                             "--1",                              Event.VALIDATION_FAILED },
         { FieldType.STRICT_INT,           "noIgnore_strictInt_floatingPoint",            false,  "1.23",                            "1.23",                             Event.VALIDATION_FAILED },

         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_positive",         false,  Characters.NUMBERS_END_ZERO,  Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_negative",         false,  "-1",                         "-1",                         Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_letter",           false,  "l",                          "l",                          Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_doubleZero",       false,  "00",                         "00",                         Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_leadingZero",      false,  "01",                         "01",                         Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_negativeZero",     false,  "-0",                         "-0",                         Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_INT,  "noIgnore_strictPositiveInt_floatingPoint",    false,  "1.23",                       "1.23",                       Event.VALIDATION_FAILED },

         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_negative",         false,  "-" + Characters.NUMBERS_END_ZERO,  "-" + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_positive",         false,  "1",                                "1",                                Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_letter",           false,  "m",                                "m",                                Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_doubleZero",       false,  "00",                               "00",                               Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_leadingZero",      false,  "-01",                              "-01",                              Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_negativeZero",     false,  "-0",                               "-0",                               Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_floatingPoint",    false,  "-1.23",                            "-1.23",                            Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_doubleDash1",      false,  "--123",                            "--123",                            Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_INT,  "noIgnore_strictNegativeInt_doubleDash2",      false,  "-12-3",                            "-12-3",                            Event.VALIDATION_FAILED },

         { FieldType.FLOAT,  "noIgnore_float_all",                  false,  Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_START_ZERO,
                                                                            Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_START_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_bad",                  false,  "1.2a",          "1.2a",          Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_posInt",               false,  "123",           "123",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_negInt",               false,  "-123",          "-123",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_negFloat",             false,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_zero1",                false,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_zero2",                false,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_zero3",                false,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_leadingDoubleZero1",   false,  "00",            "00",            Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_leadingDoubleZero2",   false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_extraTrailingZero1",   false,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_extraTrailingZero2",   false,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_leadingDoubleZeros3",  false,  "-00.0",         "-00.0",         Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_negativeZero1",        false,  "-0",            "-0",            Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_negativeZero2",        false,  "-.0",           "-.0",           Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_negativeZero3",        false,  "-0.000000",     "-0.000000",     Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_negativeZero4",        false,  "-0.0",          "-0.0",          Event.VALIDATION_SUCCESS },
         { FieldType.FLOAT,  "noIgnore_float_dash",                 false,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_doubleDash",           false,  "--",            "--",            Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_dashDot",              false,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_endInDot",             false,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_dashInWrongPlace1",    false,  "1-.2",          "1-.2",          Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_dashInWrongPlace2",    false,  "1.-2",          "1.-2",          Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_dashInWrongPlace3",    false,  "1.2-",          "1.2-",          Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_doubleDot1",           false,  "..",            "..",            Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_doubleDot2",           false,  "1..2",          "1..2",          Event.VALIDATION_FAILED },
         { FieldType.FLOAT,  "noIgnore_float_dotsEverywhere",       false,  ".1.2.",         ".1.2.",         Event.VALIDATION_FAILED },

         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_all",  false,  Characters.NUMBERS_START_ZERO + "." + Characters.NUMBERS_END_ZERO,
                                                                             Characters.NUMBERS_START_ZERO + "." + Characters.NUMBERS_END_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_bad",                  false,  "1.2a",          "1.2a",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_posInt",               false,  "123",           "123",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_negInt",               false,  "-123",          "-123",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_negFloat",             false,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_zero1",                false,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_zero2",                false,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_zero3",                false,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_leadingDoubleZero1",   false,  "00",            "00",            Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_leadingDoubleZero2",   false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_extraTrailingZero1",   false,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_extraTrailingZero2",   false,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_leadingDoubleZeros3",  false,  "-00.0",         "-00.0",         Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_negativeZero1",        false,  "-0",            "-0",            Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_negativeZero2",        false,  "-.0",           "-.0",           Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_negativeZero3",        false,  "-0.000000",     "-0.000000",     Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_negativeZero4",        false,  "-0.0",          "-0.0",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_dash",                 false,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_doubleDash",           false,  "--",            "--",            Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_dashDot",              false,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_endInDot",             false,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_dashInWrongPlace1",    false,  "1-.2",          "1-.2",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_dashInWrongPlace2",    false,  "1.-2",          "1.-2",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_dashInWrongPlace3",    false,  "1.2-",          "1.2-",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_doubleDot1",           false,  "..",            "..",            Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_doubleDot2",           false,  "1..2",          "1..2",          Event.VALIDATION_FAILED },
         { FieldType.POSITIVE_FLOAT,  "noIgnore_positiveFloat_dotsEverywhere",       false,  ".1.2.",         ".1.2.",         Event.VALIDATION_FAILED },

         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_all",  false,  "-" + Characters.NUMBERS_START_ZERO + "." + Characters.NUMBERS_END_ZERO,
                                                                             "-" + Characters.NUMBERS_START_ZERO + "." + Characters.NUMBERS_END_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_bad",                  false,  "-1.2a",         "-1.2a",         Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_posInt",               false,  "123",           "123",           Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_negInt",               false,  "-123",          "-123",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_negFloat",             false,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_zero1",                false,  "0",             "0",             Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_zero2",                false,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_zero3",                false,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_leadingDoubleZero1",   false,  "00",            "00",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_leadingDoubleZero2",   false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_extraTrailingZero1",   false,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_extraTrailingZero2",   false,  "12.3450",       "12.3450",       Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_leadingDoubleZeros3",  false,  "-00.0",         "-00.0",         Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_negativeZero1",        false,  "-0",            "-0",            Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_negativeZero2",        false,  "-.0",           "-.0",           Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_negativeZero3",        false,  "-0.000000",     "-0.000000",     Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_negativeZero4",        false,  "-0.0",          "-0.0",          Event.VALIDATION_SUCCESS },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_dash",                 false,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_doubleDash",           false,  "--",            "--",            Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_dashDot",              false,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_endInDot",             false,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_dashInWrongPlace1",    false,  "1-.2",          "1-.2",          Event.VALIDATION_FAILED   },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_dashInWrongPlace2",    false,  "1.-2",          "1.-2",          Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_dashInWrongPlace3",    false,  "1.2-",          "1.2-",          Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_doubleDot1",           false,  "..",            "..",            Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_doubleDot2",           false,  "1..2",          "1..2",          Event.VALIDATION_FAILED },
         { FieldType.NEGATIVE_FLOAT,  "noIgnore_negativeFloat_dotsEverywhere",       false,  ".1.2.",         ".1.2.",         Event.VALIDATION_FAILED },

         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_all",  false,  "-" + Characters.NUMBERS_START_ZERO + "." + Characters.NUMBERS_END_ZERO,
                                                                         "-" + Characters.NUMBERS_START_ZERO + "." + Characters.NUMBERS_END_ZERO, Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_bad",                  false,  "1.2a",          "1.2a",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_posInt",               false,  "123",           "123",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_negInt",               false,  "-123",          "-123",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_posFloat",             false,  "1023.456789",   "1023.456789",   Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_negFloat",             false,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_zero1",                false,  "0",             "0",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_zero2",                false,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_leadingDoubleZero1",   false,  "00",            "00",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_leadingDoubleZero2",   false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_extraTrailingZero1",   false,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_extraTrailingZero2",   false,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_leadingDoubleZeros3",  false,  "-00.0",         "-00.0",         Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_negativeZero1",        false,  "-0",            "-0",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_negativeZero2",        false,  "-0.000000",     "-0.000000",     Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_negativeZero3",        false,  "-0.0",          "-0.0",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_dash",                 false,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_doubleDash",           false,  "--",            "--",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_dashDot",              false,  "-.",            "-.",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_endInDot",             false,  "-1.",           "-1.",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_dashInWrongPlace1",    false,  "1-.2",          "1-.2",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_dashInWrongPlace2",    false,  "1.-2",          "1.-2",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_dashInWrongPlace3",    false,  "1.2-",          "1.2-",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_doubleDot1",           false,  "..",            "..",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_doubleDot2",           false,  "1..2",          "1..2",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_FLOAT,  "noIgnore_strictFloat_dotsEverywhere",       false,  ".1.2.",         ".1.2.",         Event.VALIDATION_FAILED },

         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_all",                  false,  Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO,
                                                                                                          Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_bad",                  false,  "1.2a",          "1.2a",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_posInt",               false,  "123",           "123",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_negInt",               false,  "-123",          "-123",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_negFloat",             false,  "-1023.456789",  "-1023.456789",  Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_zero1",                false,  "0",             "0",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_zero2",                false,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_zero3",                false,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_leadingZero1",         false,  "00",            "00",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_leadingZero2",         false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_leadingZero3",         false,  "0123.456",      "0123.456",      Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_extraTrailingZero1",   false,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_extraTrailingZero2",   false,  "12.3450",       "12.3450",       Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_leadingDoubleZeros3",  false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_negativeZero",         false,  "-0.0",          "-0.0",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_dash",                 false,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_dashDot",              false,  ".",             ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_doubleDot1",           false,  "..",            "..",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_doubleDot2",           false,  "1..2",          "1..2",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_POSITIVE_FLOAT,  "noIgnore_strictPositiveFloat_dotsEverywhere",       false,  ".1.2.",         ".1.2.",         Event.VALIDATION_FAILED },

         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_all",                  false,  "-" + Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO,
                                                                                                          "-" + Characters.NUMBERS_END_ZERO + "." + Characters.NUMBERS_END_ZERO,  Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_bad",                  false,  "-1.2a",         "-1.2a",         Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_posInt",               false,  "123",           "123",           Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_negInt",               false,  "-123",          "-123",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_zero1",                false,  "0",             "0",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_zero2",                false,  "0.0",           "0.0",           Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_zero3",                false,  ".0",            ".0",            Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_leadingZero1",         false,  "00",            "00",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_leadingZero2",         false,  "00.0",          "00.0",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_leadingZero3",         false,  "0123.456",      "0123.456",      Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_extraTrailingZero1",   false,  "0.00",          "0.00",          Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_extraTrailingZero2",   false,  "-12.3450",      "-12.3450",      Event.VALIDATION_SUCCESS },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_leadingDoubleZeros3",  false,  "-00.0",         "-00.0",         Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_negativeZero",         false,  "-0.0",          "-0.0",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_dash",                 false,  "-",             "-",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_dashDot",              false,  ".",             ".",             Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_doubleDot1",           false,  "..",            "..",            Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_doubleDot2",           false,  "1..2",          "1..2",          Event.VALIDATION_FAILED },
         { FieldType.STRICT_NEGATIVE_FLOAT,  "noIgnore_strictNegativeFloat_dotsEverywhere",       false,  ".1.2.",         ".1.2.",         Event.VALIDATION_FAILED },
      };

      for( int i = 0; i < d.length; i++ ) {
         final String name = (String) d[i][1];

         for( int j = i + 1; j < d.length; j++ ) {
            final String name2 = (String) d[j][1];

            if( name.equalsIgnoreCase(name2) ) {
               throw new IllegalArgumentException( "Duplicate test name (case-insensitive): " + name );
            }
         }
      }

      data = Arrays.asList( d );
   }

   public AbstractTest( final FieldType fieldType,
                        final String    testName,
                        final boolean   ignoreInvalidInput,
                        final String    input,
                        final String    expectedValue,
                        final Event     expectedEventOnBlur ) throws Exception {

      final Class<?> clazz = this.getClass();

      if( fieldType == null ) throw new IllegalArgumentException( "You must provide a fieldType" );
      if( StringUtil.isNothing(testName) ) throw new IllegalArgumentException( "You must provide a testName" );
      if( StringUtil.isNothing(input) ) throw new IllegalArgumentException( "You must provide an input" );

      this.fieldType = fieldType;
      this.testName = testName;
      this.ignoreInvalidInput = ignoreInvalidInput;
      this.input = input;
      this.expectedValue = expectedValue;
      this.expectedEventOnBlur = expectedEventOnBlur;

      if( clazz != lastClass ) {
         driver = null;
         lastClass = clazz;

         if( clazz == InternetExplorerTest.class ) {
            System.out.println( "Getting IE driver" );

            driver = DriverFactory.newIeDriver();

            if( driver == null ) {
               System.out.println( "IE driver is null" );
               throw new IllegalStateException( "IE driver is null" );
            } else {
               System.out.println( "Got IE driver" );
            }
         } else if( clazz == FirefoxTest.class ) {
            System.out.println( "Getting Firefox driver" );

            driver = DriverFactory.newFirefoxDriver();

            if( driver == null ) {
               System.out.println( "Firefox driver is null" );
               throw new IllegalStateException( "Firefox driver is null" );
            } else {
               System.out.println( "Got Firefox driver" );
            }
         } else if( clazz == ChromeTest.class ) {
            System.out.println( "Getting Chrome driver" );

            driver = DriverFactory.newChromeDriver();

            if( driver == null ) {
               System.out.println( "Chrome driver is null" );
               throw new IllegalStateException( "Chrome driver is null" );
            } else {
               System.out.println( "Got Chrome driver" );
            }
         }

         if( driver == null ) {
            throw new Exception( "No driver for " + this.getClass() );
         }

         driver.get( props.getProperty(APP_PROP_URL) );
      }
   }

   @BeforeClass
   public static void init() throws IOException {
      props = new Properties();
      props.load( new FileInputStream(PROPERTIES_FILENAME) );

      final String iePath = props.getProperty( APP_PROP_IE_DRIVER_PATH );
      final String geckoPath = props.getProperty( APP_PROP_GECKO_DRIVER_PATH );
      final String chromePath = props.getProperty( APP_PROP_CHROME_DRIVER_PATH );

      if( !StringUtil.isNothing(iePath) ) {
         System.setProperty( "webdriver.ie.driver", new File(iePath).getAbsolutePath() );
      }

      if( !StringUtil.isNothing(geckoPath) ) {
         System.setProperty( "webdriver.gecko.driver", new File(geckoPath).getAbsolutePath() );
      }

      if( !StringUtil.isNothing(chromePath) ) {
         System.setProperty( "webdriver.chrome.driver", new File(chromePath).getAbsolutePath() );
      }

      if( logDirectory == null ) {
         logDirectory = createLogDirectory();
         System.out.println("Web browser log directory is " + logDirectory);
      }
   }

   @AfterClass
   public static void stopSelenium() throws IOException {
      writeLog();

      if( driver != null ) {
         driver.quit();
      }
   }

   @Before
   public void setUp() {
      ((JavascriptExecutor) driver).executeScript( "setUp();" );
      field = driver.findElement( By.id("field") );
      body = driver.findElement( By.tagName("body") );
   }

   @After
   public void tearDown() {
      ((JavascriptExecutor) driver).executeScript( "tearDown();" );
   }


   @Test
   public void test() {
      initField();
      keypress( input );

      validatePreBlur();

      resetEventFlags();
      body.click();  // Force blur

      validatePostBlur();
   }

   private void validatePreBlur() {
      final String actualValue = getFieldValue();

      assertEquals( expectedValue, actualValue );

      if( ignoreInvalidInput ) {
         if( !input.equals(actualValue) ) {
            assertTrue( inputIgnoredEventFired() );
         } else {
            assertFalse( inputIgnoredEventFired() );
         }

         assertFalse( validationFailedEventFired() );

         if( actualValue.isEmpty() ) {
            assertFalse( validationSuccessEventFired() );
         } else {
            assertTrue( validationSuccessEventFired() );
         }
      } else {
         assertFalse( inputIgnoredEventFired() );
      }
   }

   private void validatePostBlur() {
      final String actualValue = getFieldValue();

      assertEquals( expectedValue, actualValue );

      if( !ignoreInvalidInput ) {
         assertFalse( inputIgnoredEventFired() );
      }

      if( expectedEventOnBlur == Event.VALIDATION_FAILED ) {
         assertTrue( validationFailedEventFired() );
         assertFalse( validationSuccessEventFired() );
      } else if( expectedEventOnBlur == Event.VALIDATION_SUCCESS ) {
         assertTrue( validationSuccessEventFired() );
         assertFalse( validationFailedEventFired() );
      }
   }

   // Helpers below this line

   /**
    * <p>
    * Rather than sending the entire string into sendKeys, we have to call it one character at a time to prevent the script
    * from accepting invalid input.  By design, jquery-restrictedtextfield.js allows a character into the field before
    * validating it, and, if necessary, rolling it back.  The script does this by listening for the "input" event, which is
    * after everything has happened.  The correct way of doing this would have been to listen to keydown or keypress and
    * call event.preventDefault() if validation failed.  This couldn't be done, since event.keyCode cannot reliably be
    * translated to an ASCII character.  Browsers sometimes report a proprietary value for it.  The only reliable way to
    * validate input was to wait until the browser had translated its sometimes-proprietary value and written it into the
    * field.
    * </p>
    *
    * <p>
    * The script makes a backup of the current value when it detects an input (before the input has been written to the
    * field), and rolls back to that value if validation fails.  The result is that if a keypress comes in before the
    * script has finished evaluating the previous one, it takes a backup of the field with the not-yet-validated, and
    * thus possibly invalid, data in it.  When it does a rollback, it rolls back to an invalid value.
    * </p>
    *
    * <p>
    * It should be noted that this condition cannot be triggered by mashing keys or by holding a key down.  This only
    * happens under the artificial scenario of Selenium apparently slamming input into the field without a delay between
    * the keystrokes.  Additionally, this is limited to IE.  This issue should never be observed in the real world.
    * </p>
    */
   private void keypress( final String s ) {
      if( StringUtil.isNothing(s) ) throw new IllegalArgumentException( "keypress has no value" );

      final int len = s.length();

      for( int i = 0; i < len; i++ ) {
         field.sendKeys( Character.toString(s.charAt(i)) );
      }
   }

   private void initField() {
      if( testName == null ) throw new IllegalArgumentException( "testName is null" );
      if( fieldType == null ) throw new IllegalArgumentException( "fieldType is null" );

      final String command = String.format( "initField(\"%s\", \"%s\", %b);", testName, fieldType.toString(), ignoreInvalidInput );
      ((JavascriptExecutor) driver).executeScript( command );
   }

   private void resetEventFlags() {
      ((JavascriptExecutor) driver).executeScript( "resetEventFlags();" );
   }

   private String getFieldValue() {
      // Unlike other drivers, the Marionette/Gecko driver only returns the value of the "value" attribute
      // in the HTML tag, rather than returning the value of the field.  Obviously that's a problem for
      // the purposes of this application.  That's not how the previous Firefox driver, FirefoxDriver,
      // worked.  GetText() is no help, either.  This behavior was last verified with Gecko driver
      // v0.10.0-win64 on September 10, 2016.
      //
      // final String value = field.getAttribute( "value" );   <--- always null in Firefox

      // Workaround:  Get the value using JavaScript

      final String value = (String) ((JavascriptExecutor) driver).executeScript( "return document.getElementById('field').value;" );
      return value != null ? value : "";
   }

   private boolean inputIgnoredEventFired() {
      return (Boolean) ((JavascriptExecutor) driver).executeScript( "return didInputIgnoredEventFire();" );
   }

   private boolean validationFailedEventFired() {
      return (Boolean) ((JavascriptExecutor) driver).executeScript( "return didValidationFailedEventFire();" );
   }

   private boolean validationSuccessEventFired() {
      return (Boolean) ((JavascriptExecutor) driver).executeScript( "return didValidationSuccessEventFire();" );
   }

   private static File createLogDirectory() throws IOException {
      final String path = "build/reports/tests/browser_logs";
      final File dir = new File( path );

      if( !dir.exists() && !dir.mkdirs() ) {
         throw new IOException( "Could not create log directory " + path );
      }

      return dir;
   }

   private static void writeLog() throws IOException {
      final BufferedWriter writer = new BufferedWriter( new FileWriter(logDirectory.getPath() + "/" + lastClass.getSimpleName() + ".log") );

      if( driver != null && writer != null ) {
         ((JavascriptExecutor) driver).executeScript( "showExitModal();" );

         try {
         writer.write( driver.findElement(By.id("log")).getText() );
         writer.flush();
         } finally {
            writer.close();
         }
      }
   }
}
