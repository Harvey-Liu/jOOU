/**
 * Copyright (c) 2011-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOU" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.joou;

import java.math.BigInteger;

/**
 * The <code>unsigned long</code> type
 *
 * @author Lukas Eder
 * @author Jens Nerche
 * @author Ivan Sokolov
 */
public final class ULong extends UNumber implements Comparable<ULong> {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = -6821055240959745390L;

    /**
     * A constant holding the minimum value an <code>unsigned long</code> can
     * have, 0.
     */
    public static final BigInteger MIN_VALUE        = BigInteger.ZERO;
    /**
     * A constant holding the maximum value an <code>unsigned long</code> can
     * have, 2<sup>64</sup>-1.
     */
    public static final BigInteger MAX_VALUE        = new BigInteger("18446744073709551615");

    /**
     * A constant holding the maximum value + 1 an <code>signed long</code> can
     * have, 2<sup>63</sup>.
     */
    public static final BigInteger MAX_VALUE_LONG   = new BigInteger("9223372036854775808");

    /**
     * A constant holding the minimum value an <code>unsigned long</code> can
     * have as ULong, 0.
     */
    public static final ULong      MIN              = valueOf(MIN_VALUE.longValue());

    /**
     * A constant holding the maximum value + 1 an <code>signed long</code> can
     * have as ULong, 2<sup>63</sup>.
     */
    public static final ULong      MAX              = valueOf(MAX_VALUE);

    /**
     * The value modelling the content of this <code>unsigned long</code>
     */
    private final long             value;

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned long</code>.
     */
    public static ULong valueOf(String value) throws NumberFormatException {
        return new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code> by masking it with
     * <code>0xFFFFFFFFFFFFFFFF</code> i.e. <code>(long) -1</code> becomes
     * <code>(uint) 18446744073709551615</code>
     */
    public static ULong valueOf(long value) {
        return new ULong(value);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned long</code>
     */
    public static ULong valueOf(BigInteger value) throws NumberFormatException {
        return new ULong(value);
    }

    public static int compare(long x, long y) {
        return Long.compare(x + Long.MIN_VALUE, y + Long.MIN_VALUE);
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> is not in the range
     *             of an <code>unsigned long</code>
     */
    private ULong(BigInteger value) throws NumberFormatException {
        if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
            throw new NumberFormatException();
        }
        else {
            this.value = value.longValue();
        }
    }

    /**
     * Create an <code>unsigned long</code> by masking it with
     * <code>0xFFFFFFFFFFFFFFFF</code> i.e. <code>(long) -1</code> becomes
     * <code>(uint) 18446744073709551615</code>
     */
    private ULong(long value) {
        this.value = value;
    }

    /**
     * Create an <code>unsigned long</code>
     *
     * @throws NumberFormatException If <code>value</code> does not contain a
     *             parsable <code>unsigned long</code>.
     */
    private ULong(String value) throws NumberFormatException {
	    if (value == null) {
		    throw new NumberFormatException("null");
	    }

	    int length = value.length();

	    if (length == 0) {
		    throw new NumberFormatException("Empty input string");
	    }

	    if (value.charAt(0) == '-') {
		    throw new NumberFormatException(
			    String.format("Illegal leading minus sign on unsigned string %s", value));
	    }

	    if (length <= 18) {
		    this.value = Long.parseLong(value, 10);
		    return;
	    }

	    final long first = Long.parseLong(value.substring(0, length - 1), 10);
	    final int second = Character.digit(value.charAt(length - 1), 10);
	    if (second < 0) {
		    throw new NumberFormatException("Bad digit at end of " + value);
	    }
	    long result = first * 10 + second;
	    if (compare(result, first) < 0) {
		    throw new NumberFormatException(
			    String.format("String value %s exceeds range of unsigned long", value));
	    }

        this.value = result;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        if (value < 0) {
            return ((float) (value & Long.MAX_VALUE)) + Long.MAX_VALUE;
        }
        else {
            return (float) value;
        }
    }

    @Override
    public double doubleValue() {
        if (value < 0){
            return ((double) (value & Long.MAX_VALUE)) + Long.MAX_VALUE;
        }
        else {
            return (double) value;
        }
    }

    @Override
    public int hashCode() {
        return Long.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ULong) {
            return value == ((ULong) obj).value;
        }

        return false;
    }

    @Override
    public String toString() {
        if (value >= 0) {
            return Long.toString(value);
        }
        else {
            return BigInteger.valueOf(value & Long.MAX_VALUE).add(MAX_VALUE_LONG).toString();
        }
    }

    @Override
    public int compareTo(ULong o) {
        return compare(value, o.value);
    }

    public ULong add(ULong val) throws NumberFormatException {
        if (value < 0 && val.value < 0) {
            throw new NumberFormatException();
        }
	    final long result = value + val.value;
	    if ((value < 0 || val.value < 0) && result >= 0) {
		    throw new NumberFormatException();
	    }
	    return valueOf(result);
    }

    public ULong add(int val) throws NumberFormatException {
        if (val < 0) {
            return subtract(Math.abs(val));
        }
	    final long result = value + val;
	    if (value < 0 && result >= 0) {
		    throw new NumberFormatException();
	    }
	    return valueOf(result);
    }

    public ULong subtract(final ULong val) {
	    if (this.compareTo(val) < 0) {
		    throw new NumberFormatException();
	    }
	    final long result = value - val.value;
	    if (value < 0 && result >= 0) {
		    throw new NumberFormatException();
	    }
	    return valueOf(result);
    }

    public ULong subtract(final int val) {
	    if (val < 0) {
		    return add(-val);
	    }
	    if (compare(value, val) < 0) {
		    throw new NumberFormatException();
	    }
	    final long result = value - val;
	    if (value < 0 && result >= 0) {
		    throw new NumberFormatException();
	    }
	    return valueOf(result);
    }

}
