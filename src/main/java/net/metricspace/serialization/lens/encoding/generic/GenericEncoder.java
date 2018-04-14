/* Copyright (c) 2018, Eric L. McCorkle. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * only, as published by the Free Software Foundation.  This
 * particular file is subject to the "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
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
package net.metricspace.serialization.lens.encoding.generic;

import java.io.IOException;
import java.lang.Iterable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Iterator;
import java.util.Optional;

import net.metricspace.serialization.elements.Element;
import net.metricspace.serialization.elements.Elements;
import net.metricspace.serialization.lens.Lens;
import net.metricspace.serialization.lens.encoding.Encoder;

/**
 * An {@link Encoder} scheme based on generic datatype idioms.  This
 * is derived loosely from ASN.1, but should be useful for almost any
 * encoding that does not need a specific format.
 *
 * @param <C> Type of concrete (encoded) data.
 * @see net.metricspace.serialization.lens.Lens
 */
public interface GenericEncoder<C> extends Encoder<C> {
    /**
     * Encode a Unit type value (that is, a value with no content).
     *
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public C encodeUnit(final C concrete)
        throws IOException;

    /**
     * Encode a boolean value.
     *
     * @param b The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public C encodeBool(final boolean b,
                        final C concrete)
        throws IOException;

    /**
     * Encode an unbounded integer value.
     *
     * @param n The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public C encodeInteger(final long n,
                           final C concrete)
        throws IOException;

    /**
     * Encode a bounded integer value.
     *
     * @param min The lower bound.
     * @param max The upper bound.
     * @param n The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the integer value is out of bounds.
     */
    public C encodeInteger(final long min,
                           final long max,
                           final long n,
                           final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode an entire byte array as a variable-length bytestring.
     *
     * @param bytes Byte array to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public default C encodeBytes(final byte[] bytes,
                                 final C concrete)
        throws IOException {
        return encodeBytes(bytes, 0, bytes.length, concrete);
    }

    /**
     * Encode a portion of a byte array as a variable-length bytestring.
     *
     * @param bytes Byte array to encode.
     * @param offset Offset at which to start.
     * @param len Number of bytes to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public C encodeBytes(final byte[] bytes,
                         final int offset,
                         final int len,
                         final C concrete)
        throws IOException;

    /**
     * Encode an entire byte array as a fixed-length bytestring.
     *
     * @param len The fixed length of the bytestring.
     * @param bytes Byte array to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the byte array has the wrong length.
     */
    public default C encodeBytes(final int len,
                                 final byte[] bytes,
                                 final C concrete)
        throws IOException {
        return encodeBytes(len, bytes, 0, concrete);
    }

    /**
     * Encode a portion of a byte array as a fixed-length bytestring.
     *
     * @param len The fixed length of the bytestring.
     * @param bytes Byte array to encode.
     * @param offset Offset at which to start.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If the byte array has the wrong length.
     */
    public C encodeBytes(final int len,
                         final byte[] bytes,
                         final int offset,
                         final C concrete)
        throws IOException;

    /**
     * Encode an entire array of elements as a variable-length sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param seq Elements to encode,
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public default <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                                     final A[] seq,
                                     final C concrete)
        throws IOException {
        return encodeSeqOf(encode, seq, 0, seq.length, concrete);
    }

    /**
     * Encode an entire array of elements as a variable-length sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param seq Elements to encode,
     * @param offset Offset in the array at which to start.
     * @param len Number of elements to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                             final A[] seq,
                             final int offset,
                             final int len,
                             final C concrete)
        throws IOException;

    /**
     * Encode values from an {@link Iterable} as a variable-length
     * sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param it {@link Iterable} structure from which to add values.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public default <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                                     final Iterable<A> it,
                                     final C concrete)
        throws IOException {
        return encodeSeqOf(encode, it.iterator(), concrete);
    }

    /**
     * Encode values from an {@link Iterator} as a variable-length
     * sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param it {@link Iterator} from which to add values.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                             final Iterator<A> it,
                             final C concrete)
        throws IOException;

    /**
     * Encode values from a {@link Supplier} as a variable-length
     * sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param it {@link Supplier} from which to add values.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                             final Supplier<A> it,
                             final C concrete)
        throws IOException;

    /**
     * Encode elements from an array as a fixed-length sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param len Length of the fixed-length sequence.
     * @param seq Elements to encode,
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there aren't enough elements.
     */
    public default <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                                     final int len,
                                     final A[] seq,
                                     final C concrete)
        throws IOException, IllegalArgumentException {
        return encodeSeqOf(encode, len, seq, 0, concrete);
    }

    /**
     * Encode elements from an array as a fixed-length sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param len Length of the fixed-length sequence.
     * @param seq Elements to encode,
     * @param offset Offset in the array at which to start.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there aren't enough elements.
     */
    public <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                             final int len,
                             final A[] seq,
                             final int offset,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode elements from an {@link Iterable} as a fixed-length
     * sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param len The fixed length of the sequence.
     * @param it {@link Iterable} structure from which to encode element.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there aren't enough elements.
     */
    public default <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                                     final int len,
                                     final Iterable<A> it,
                                     final C concrete)
        throws IOException, IllegalArgumentException {
        return encodeSeqOf(encode, len, it.iterator(), concrete);
    }

    /**
     * Encode elements from an {@link Iterator} as a fixed-length
     * sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param len The fixed length of the sequence.
     * @param it {@link Iterator} from which to encode element.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there aren't enough elements.
     */
    public <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                             final int len,
                             final Iterator<A> it,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode elements from a {@link Supplier} as a fixed-length
     * sequence.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param len The fixed length of the sequence.
     * @param it {@link Supplier} from which to encode element.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there aren't enough elements.
     */
    public <A> C encodeSeqOf(final Lens<A, C, ?> encode,
                             final int len,
                             final Supplier<A> it,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode an entire array of elements as a uniform set.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param seq Elements to encode,
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there are duplicate elements.
     */
    public <A> C encodeSetOf(final Lens<A, C, ?> encode,
                             final A[] seq,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode a portion of an array of elements as a uniform set.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param seq Elements to encode,
     * @param offset Offset at which to start.
     * @param len Number of bytes to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     * @throws IllegalArgumentException If there are duplicate elements.
     */
    public <A> C encodeSetOf(final Lens<A, C, ?> encode,
                             final A[] seq,
                             final int offset,
                             final int len,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode values from an {@link Iterable} as a uniform set.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param it {@link Iterable} structure from which to add values.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public default <A> C encodeSetOf(final Lens<A, C, ?> encode,
                                     final Iterable<A> it,
                                     final C concrete)
        throws IOException, IllegalArgumentException {
        return encodeSetOf(encode, it.iterator(), concrete);
    }

    /**
     * Encode values from an {@link Iterator} as a uniform set.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param it {@link Iterator} from which to add values.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeSetOf(final Lens<A, C, ?> encode,
                             final Iterator<A> it,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode values from an {@link Supplier} as a uniform set.
     *
     * @param <A> Type of elements.
     * @param encode {@link Lens} to use for encoding element.
     * @param it {@link Supplier} from which to add values.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeSetOf(final Lens<A, C, ?> encode,
                             final Supplier<A> it,
                             final C concrete)
        throws IOException, IllegalArgumentException;

    /**
     * Encode a value which has a designated default value.  In most
     * encodings, this means that the default value doesn't get
     * encoded.
     *
     * @param <A> The type of the element.
     * @param encode {@link Lens} to use for encoding values.
     * @param defval The default value.
     * @param val The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeWithDefault(final Lens<A, C, ?> encode,
                                   final A defval,
                                   final A val,
                                   final C concrete)
        throws IOException;

    /**
     * Encode an optional value, which may be present or not.
     *
     * @param <A> The type of the element.
     * @param encode {@link Lens} to use for encoding values.
     * @param val The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeOptional(final Lens<A, C, ?> encode,
                                final Optional<A> val,
                                final C concrete)
        throws IOException;

    /**
     * Encode a choice value, which is one of a number of options.
     *
     * @param <A> The type of the choice value.
     * @param elements The {@link Elements} descriptor.
     * @param kind The {@link Element} tag for the choice option.
     * @param val The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeChoice(final Elements<Lens<Object, C, ?>> elements,
                              final Element<Lens<A, C, ?>> kind,
                              final A val,
                              final C concrete)
        throws IOException;

    /**
     * Encode an object as a structure (non-uniform sequence).
     *
     * @param <A> The type of the object to encode.
     * @param elems The {@link Elements} descriptor for the fields.
     * @param val The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeStructure(final Elements<Lens<Object, C, ?>> elems,
                                 final A val,
                                 final C concrete)
        throws IOException;

    /**
     * Encode an object as a non-uniform set.
     *
     * @param <A> The type of the object to encode.
     * @param elems The {@link Elements} descriptor for the fields.
     * @param val The value to encode.
     * @param concrete The concrete representation into which to add
     *                 the encoded value.
     * @return The modified concrete representation.
     * @throws IOException If an IO error occurs.
     */
    public <A> C encodeSet(final Elements<Lens<Optional<Object>, C, ?>> elems,
                           final A val,
                           final C concrete)
        throws IOException;
}
