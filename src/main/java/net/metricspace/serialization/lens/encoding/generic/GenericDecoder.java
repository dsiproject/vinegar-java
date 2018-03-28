/* Copyright (c) 2018, Eric L. McCorkle. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.metricspace.serialization.lens.encoding;

import java.io.IOException;
import java.lang.Iterable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Optional;

import net.metricspace.serialization.elements.Element;
import net.metricspace.serialization.elements.Elements;
import net.metricspace.serialization.lens.Lens;
import net.metricspace.serialization.lens.encoding.Decoder;

/**
 * Common superclass for encoding schemes.  Subclasses of this provide
 * an interface for encoding and decoding data, which is used by
 * {@link net.metricspace.serialization.lens.Lens}s to actually perform
 * the encoding.
 *
 * @param <C> Type of concrete (encoded) data.
 * @see net.metricspace.serialization.lens.Lens
 */
public interface GenericDecoder<C> extends Decoder<C> {
    /**
     * Decode a unit type value.
     *
     * @param concrete The concrete encoding.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a unit value.
     */
    public void decodeUnit(final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a boolean value.
     *
     * @param concrete The concrete encoding.
     * @return The boolean value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a boolean.
     */
    public boolean decodeBool(final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode an unbounded integer value.
     *
     * @param concrete The concrete encoding.
     * @return The integer value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe an integer.
     */
    public long decodeInteger(final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a bounded integer value.
     *
     * @param min The lower bound.
     * @param max The upper bound.
     * @param concrete The concrete encoding.
     * @return The integer value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe an integer.
     */
    public long decodeInteger(final long min,
                              final long max,
                              final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a variable-length byte string.
     *
     * @param getarr A function to supply an array.
     * @param concrete The concrete encoding.
     * @return The byte string.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a variable length
     *                                  bytestring.
     */
    public byte[] decodeBytes(final Function<Integer, byte[]> getarr,
                              final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a fixed-length byte string.
     *
     * @param len The fixed byte string length.
     * @param buf The buffer into which to read the byte string.
     * @param concrete The concrete encoding.
     * @return The byte string.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a fixed length
     *                                  bytestring.
     */
    public byte[] decodeBytes(final int len,
                              final byte[] buf,
                              final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a variable-length uniform sequence.
     *
     * @param <A> The element type.
     * @param lenfun A {@link Consumer} that receives the length of
     *               the sequence.
     * @param elemfun A {@link Consumer} that receives each element in
     *                order.
     * @param decode A {@link Lens} to decode elements.
     * @param concrete The concrete encoding.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a variable length
     *                                  bytestring.
     */
    public <A> A[] decodeSeqOf(final Consumer<Integer> lenfun,
                               final Consumer<A> elemfun,
                               final Lens<A, C> decode,
                               final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a fixed-length uniform sequence.
     *
     * @param <A> The element type.
     * @param len The length of the sequence.
     * @param elemfun A {@link Consumer} that receives each element in
     *                order.
     * @param decode A {@link Lens} to decode elements.
     * @param concrete The concrete encoding.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a variable length
     *                                  bytestring.
     */
    public <A> A[] decodeSeqOf(final int len,
                               final Consumer<A> elemfun,
                               final Lens<A, C> decode,
                               final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode (and consume) a variable-length uniform sequence.
     *
     * @param <A> The element type.
     * @param lenfun A {@link Consumer} that receives the length of
     *               the sequence.
     * @param elemfun A {@link Consumer} that receives each element in
     *                order.
     * @param decode A {@link Lens} to decode elements.
     * @param concrete The concrete encoding.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a uniform set.
     */
    public <A> A[] decodeSetOf(final Consumer<Integer> lenfun,
                               final Consumer<A> elemfun,
                               final Lens<A, C> decode,
                               final C concrete)
        throws IOException, IllegalArgumentException;


    /**
     * Decode a value with a default.
     *
     * @param <A> The value type.
     * @param decode A lens to decode the value.
     * @param defval The default value.
     * @param concrete The concrete encoding.
     * @return The decoded value.
     * @throws IOException If an IO error occurs.
     */
    public <A> A decodeWithDefault(final Lens<A, C> decode,
                                   final A defval,
                                   final C concrete)
        throws IOException;

    /**
     * Decode an optional value.
     *
     * @param <A> The value type.
     * @param decode A lens to decode the value.
     * @param concrete The concrete encoding.
     * @return The decoded value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe an optional value.
     */
    public <A> Optional<A> decodeOptional(final Lens<A, C> decode,
                                          final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a choice type value.
     *
     * @param <A> The value type.
     * @param elems The {@link Elements} for the options.
     * @param concrete The concrete encoding.
     * @return The decoded value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe an optional value.
     */
    public <A> A decodeChoice(final Elements<Lens<A, C>> elems,
                              final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a structure (non-uniform sequence) value.
     *
     * @param <A> The structure type.
     * @param elems The {@link Elements} descriptor for the fields.
     * @param concrete The concrete encoding.
     * @return The decoded value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a structure value.
     */
    public <A> A decodeStructure(final Elements<Lens<Object, C>> elems,
                                 final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Decode a non-uniform set.
     *
     * @param <A> The set type.
     * @param elems The {@link Elements} descriptor for the fields.
     * @param concrete The concrete encoding.
     * @return The decoded value.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the content does not
     *                                  describe a non-uniform set.
     */
    public <A> C decodeSet(final Elements<Lens<Optional<Object>, C>> elems,
                           final C concrete)
        throws IOException, IllegalArgumentException;
}
