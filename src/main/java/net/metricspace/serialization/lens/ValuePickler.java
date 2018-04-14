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

import net.metricspace.serialization.lens.encoding.Decoder;
import net.metricspace.serialization.lens.encoding.Encoder;

/**
 * Interface for picklers whose concrete encodings take the form of
 * some kind of value, such as a tree node.
 *
 * @param <A> Abstract representation type.
 * @param <C> Concrete representation type.
 */
public interface ValuePickler<A, C> extends Lens<A, C, C> {
    /**
     * Encode an abstract instance into its concrete form.  This is a
     * wrapper around {@link #encode(Encoder, Object)}
     *
     * @param encoding The {@link Encoder} to use.
     * @param data The abstract data to encode.
     * @param concrete Ignored.
     * @return The encoded concrete form.
     * @throws IOException If an exception occurs during encoding.
     */
    @Override
    public C encode(final Encoder<C> encoding,
                    final A data,
                    final C concrete)
        throws IOException {
        return encode(encoding, data);
    }

    /**
     * Encode an abstract instance into its concrete form.
     *
     * @param encoding The {@link Encoder} to use.
     * @param data The abstract data to encode.
     * @return The encoded concrete form.
     * @throws IOException If an exception occurs during encoding.
     */
    public C encode(final Encoder<C> encoding,
                     final A data)
        throws IOException;
}
