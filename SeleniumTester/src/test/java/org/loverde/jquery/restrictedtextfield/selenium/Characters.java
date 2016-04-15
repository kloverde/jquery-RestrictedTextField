/*
 * RestrictedTextField
 * https://www.github.com/kloverde/jquery-RestrictedTextField
 *
 * This software is licensed under the 3-clause BSD license.
 *
 * Copyright (c) 2016 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/10
 */

package org.loverde.jquery.restrictedtextfield.selenium;


public class Characters {

   public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
                              UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                              LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz",

                              ALPHA_SPACE = ALPHA + " ",
                              UPPER_ALPHA_SPACE = UPPER_ALPHA + " ",
                              LOWER_ALPHA_SPACE = LOWER_ALPHA + " ",

                              NUMBERS = "01234567890",

                              ALPHANUMERIC = ALPHA + NUMBERS,
                              UPPER_ALPHANUMERIC = UPPER_ALPHA + NUMBERS,
                              LOWER_ALPHANUMERIC = LOWER_ALPHA + NUMBERS,

                              ALPHANUMERIC_SPACE = ALPHANUMERIC + " ",
                              UPPER_ALPHANUMERIC_SPACE = UPPER_ALPHANUMERIC + " ",
                              LOWER_ALPHANUMERIC_SPACE = LOWER_ALPHANUMERIC + " ",

                              SYMBOLS = "`~!@#$%^&*()-_=+]}[{;:'\"/?.>,<",

                              ALL = ALPHANUMERIC_SPACE + SYMBOLS;
}
