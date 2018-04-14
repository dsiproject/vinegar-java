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
package net.metricspace.serialization.lens;

import java.io.IOException;
import java.lang.IllegalArgumentException;

import net.metricspace.serialization.lens.encoding.Decoder;
import net.metricspace.serialization.lens.encoding.Encoder;

/**
 * Composable encoding-decoding pairs between an abstract and a
 * concrete representation.  Lenses here are based loosely off of work
 * by Benjamin Pierce and others, and on the research efforts that
 * have followed from their initial work.  Picklers can be seen as a
 * degenerate form of Pierce lenses.
 * <p>
 * This interface is intended as a kind of "master interface" for both
 * Pierce lenses and picklers.  Its interface exactly matches the
 * definition in the original lens papers, though this is due to
 * necessities that arise from potentially wide varieties of uses.
 * For example, some kinds of picklers will want to write out encoded
 * values to a stream; others (such as XML picklers) might want to
 * return a tree node.  Proper Pierce lenses, of course,
 * <i>require</i> the interface presented here.
 * <p>
 * This interface makes no formal guarantees about the laws observed
 * by its implementors.
 *
 * @param <A> Abstract representation type.
 * @param <CO> Concrete output representation type.
 * @param <CI> Concrete input representation type.
 */
public interface Lens<A, CO, CI> {
    /**
     * Encode an abstract instance into its concrete form.  The
     * concrete form may or may not be added to the {@code concrete}
     * parameter, depending on the specific implementation.
     *
     * @param encoding The {@link Encoder} to use.
     * @param data The abstract data to encode.
     * @param concrete An existing concrete form taken as a parameter.
     * @return The encoded concrete form.
     * @throws IOException If an exception occurs during encoding.
     */
    public CO encode(final Encoder<CO> encoding,
                     final A data,
                     final CO concrete)
        throws IOException;

    /**
     * Encode an abstract instance into its concrete form.  As opposed
     * to the three-argument form {@link #encode(Encoder, Object,
     * Object)}, this form generates a "fresh" encoding as opposed to
     * adding to an existing encoding.
     *
     * @param encoding The {@link Encoder} to use.
     * @param data The abstract data to encode.
     * @return The encoded concrete form.
     * @throws IOException If an exception occurs during encoding.
     */
    public CO encode(final Encoder<CO> encoding,
                     final A data)
        throws IOException;

    /**
     * Decode a concrete encoding into an abstract data value.
     *
     * @param encoding The {@link Decoder} to use.
     * @param encoded The encoded concrete value.
     * @return The decoded abstract value.
     * @throws IOException If an exception occurs during decoding.
     */
    public A decode(final Decoder<CI> encoding,
                    final CI encoded)
        throws IOException;
}
