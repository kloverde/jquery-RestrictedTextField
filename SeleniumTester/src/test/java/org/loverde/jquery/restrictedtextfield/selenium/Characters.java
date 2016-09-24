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

package org.loverde.jquery.restrictedtextfield.selenium;


public class Characters {

   public static final String UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                              LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz",
                              ALPHA = UPPER_ALPHA + LOWER_ALPHA,

                              ALPHA_SPACE = ALPHA + " ",
                              UPPER_ALPHA_SPACE = UPPER_ALPHA + " ",
                              LOWER_ALPHA_SPACE = LOWER_ALPHA + " ",

                              NUMBERS_NO_ZERO = "123456789",
                              NUMBERS_START_ZERO = "0" + NUMBERS_NO_ZERO,
                              NUMBERS_END_ZERO = NUMBERS_NO_ZERO + "0",

                              ALPHANUMERIC = ALPHA + NUMBERS_END_ZERO,
                              UPPER_ALPHANUMERIC = UPPER_ALPHA + NUMBERS_END_ZERO,
                              LOWER_ALPHANUMERIC = LOWER_ALPHA + NUMBERS_END_ZERO,

                              ALPHANUMERIC_SPACE = ALPHANUMERIC + " ",
                              UPPER_ALPHANUMERIC_SPACE = UPPER_ALPHANUMERIC + " ",
                              LOWER_ALPHANUMERIC_SPACE = LOWER_ALPHANUMERIC + " ",

                              SYMBOLS_EXCEPT_MINUS = "`~!@#$%^&*()_=+]}[{;:'\"/?.>,<",
                              SYMBOLS = SYMBOLS_EXCEPT_MINUS + "-",

                              ALL = ALPHANUMERIC_SPACE + SYMBOLS,
                              ALL_EXCEPT_MINUS = ALPHANUMERIC_SPACE + SYMBOLS_EXCEPT_MINUS;
}
