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
import java.io.InputStream;
import java.io.OutputStream;

import net.metricspace.serialization.lens.encoding.Decoder;
import net.metricspace.serialization.lens.encoding.Encoder;

/**
 * A {@link Lens} subclass representing picklers that encode to and
 * decode from some kind of stream.
 *
 * @param <A> The abstract value type.
 * @param <CO> The output stream type.
 * @param <CI> The input stream type.
 */
public interface StreamPickler<A, CO extends OutputStream,
                               CI extends InputStream>
    extends Lens<A, CO, CI> {
    /**
     * Encode the abstract value to the given stream.  This is used to
     * implement the required interface function {@link
     * #encode(Encoder, Object, OutputStream)}.
     *
     * @param encoding The {@link Encoder} to use.
     * @param data The abstract data to encode.
     * @param stream Stream to which to write the encoded form.
     * @throws IOException If an exception occurs during encoding.
     */
    public void encodeToStream(final Encoder<CO> encoding,
                               final A data,
                               final CO stream)
        throws IOException;

    /**
     * Encode the abstract value to the given stream.  This is simply
     * a wrapper around {@link #encodeToStream(Encoder, Object,
     * OutputStream)}, which returns the stream.
     *
     * @param encoding The {@link Encoder} to use.
     * @param data The abstract data to encode.
     * @param stream Stream to which to write the encoded form.
     * @return The {@code stream} parameter.
     * @throws IOException If an exception occurs during encoding.
     */
    @Override
    public default CO encode(final Encoder<CO> encoding,
                             final A data,
                             final CO stream)
        throws IOException {
        encodeToStream(encoding, data, stream);

        return stream;
    }
}
