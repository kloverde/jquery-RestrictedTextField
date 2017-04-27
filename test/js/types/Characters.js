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

var Characters = {};

Characters.UPPER_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
Characters.LOWER_ALPHA = "abcdefghijklmnopqrstuvwxyz";
Characters.ALPHA = Characters.UPPER_ALPHA + Characters.LOWER_ALPHA;

Characters.ALPHA_SPACE = Characters.ALPHA + " ";
Characters.UPPER_ALPHA_SPACE = Characters.UPPER_ALPHA + " ";
Characters.LOWER_ALPHA_SPACE = Characters.LOWER_ALPHA + " ";

Characters.NUMBERS_NO_ZERO = "123456789";
Characters.NUMBERS_START_ZERO = "0" + Characters.NUMBERS_NO_ZERO;
Characters.NUMBERS_END_ZERO = Characters.NUMBERS_NO_ZERO + "0";

Characters.ALPHANUMERIC = Characters.ALPHA + Characters.NUMBERS_END_ZERO;
Characters.UPPER_ALPHANUMERIC = Characters.UPPER_ALPHA + Characters.NUMBERS_END_ZERO;
Characters.LOWER_ALPHANUMERIC = Characters.LOWER_ALPHA + Characters.NUMBERS_END_ZERO;

Characters.ALPHANUMERIC_SPACE = Characters.ALPHANUMERIC + " ";
Characters.UPPER_ALPHANUMERIC_SPACE = Characters.UPPER_ALPHANUMERIC + " ";
Characters.LOWER_ALPHANUMERIC_SPACE = Characters.LOWER_ALPHANUMERIC + " ";

Characters.SYMBOLS_EXCEPT_PARENS = "`~!@#$%^&*-_=+]}[{;:'\"/?.>,<";
Characters.SYMBOLS_EXCEPT_MINUS = "`~!@#$%^&*()_=+]}[{;:'\"/?.>,<";
Characters.SYMBOLS = Characters.SYMBOLS_EXCEPT_MINUS + "-";

Characters.ALL = Characters.ALPHANUMERIC_SPACE + Characters.SYMBOLS;
Characters.ALL_EXCEPT_MINUS = Characters.ALPHANUMERIC_SPACE + Characters.SYMBOLS_EXCEPT_MINUS;
