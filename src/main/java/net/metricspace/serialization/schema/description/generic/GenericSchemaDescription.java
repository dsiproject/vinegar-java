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
package net.metricspace.serialization.schema.description.generic;

import net.metricspace.serialization.elements.Elements;
import net.metricspace.serialization.schema.SchemaDescribable;
import net.metricspace.serialization.schema.description.SchemaDescription;

public interface GenericSchemaDescription<S>
    extends SchemaDescription<S> {
    /**
     * Get a schema descriptor for a unitary value.
     *
     * @param A schema descriptor for a unitary value.
     */
    public S unitDesc();

    /**
     * Get a schema descriptor for a boolean type.
     *
     * @return A schema descriptor for a boolean type.
     */
    public S booleanDesc();

    /**
     * Get a schema descriptor for an unbounded integer type.
     *
     * @return A schema descriptor for an unbounded integer type.
     */
    public S integerDesc();

    /**
     * Get a schema descriptor for a bounded integer type.
     *
     * @param min The lower bound.
     * @param max The upper bound.
     * @return A schema descriptor for a bounded integer type.
     */
    public S integerDesc(final long min,
                         final long max);

    /**
     * Get a schema descriptor for a variable-length byte string.
     *
     * @return A schema descriptor for a variable-length byte string.
     */
    public S bytesDesc();

    /**
     * Get a schema descriptor for a fixed-length byte string.
     *
     * @param len The length of the byte string
     * @return A schema descriptor for a fixed-length byte string.
     */
    public S bytesDesc(final long len);

    /**
     * Get a schema descriptor for a variable-length ascii string.
     *
     * @return A schema descriptor for a variable-length ascii string.
     */
    public S asciiDesc();

    /**
     * Get a schema descriptor for a fixed-length ascii string.
     *
     * @param len The length of the byte string
     * @return A schema descriptor for a fixed-length ascii string.
     */
    public S asciiDesc(final long len);

    /**
     * Get a schema descriptor for a variable-length UTF-8 string.
     *
     * @return A schema descriptor for a variable-length UTF-8 string.
     */
    public S utf8Desc();

    /**
     * Get a schema descriptor for a fixed-length UTF-8 string.
     *
     * @param len The length of the byte string
     * @return A schema descriptor for a fixed-length UTF-8 string.
     */
    public S utf8Desc(final long len);

    /**
     * Get a schema descriptor for a variable-length uniform sequence.
     *
     * @param desc The {@link SchemaDescribable} instance for elements.
     * @return A schema descriptor for a variable-length uniform sequence.
     */
    public S seqOfDesc(final SchemaDescribable desc);

    /**
     * Get a schema descriptor for a fixed-length uniform sequence.
     *
     * @param desc The {@link SchemaDescribable} instance for elements.
     * @param len The length of the sequence.
     * @return A schema descriptor for a fixed-length uniform sequence.
     */
    public S seqOfDesc(final SchemaDescribable desc,
                       final int len);

    /**
     * Get a schema descriptor for a uniform set.
     *
     * @param desc The {@link SchemaDescribable} instance for elements.
     * @return A schema descriptor for a uniform set.
     */
    public S setOfDesc(final SchemaDescribable desc);

    /**
     * Get a schema descriptor for an optional type.
     *
     * @param desc The {@link SchemaDescribable} instance for elements.
     * @return A schema descriptor for an optional type.
     */
    public S optionalDesc(final SchemaDescribable desc);

    /**
     * Get a schema descriptor for a choice type, consisting of
     * multiple possible choices.
     *
     * @param options A description of the options.
     * @return A schema descriptor for a choice type.
     */
    public S choiceDesc(final Elements<SchemaDescribable> options);

    /**
     * Get a schema descriptor for a structure (non-uniform sequence)
     * type.
     *
     * @param elems A description of the fields.
     * @return A schema descriptor for a structure type.
     */
    public S structureDesc(final Elements<SchemaDescribable> elems);

    /**
     * Get a schema descriptor for a non-uniform set type.
     *
     * @param elems A description of the fields.
     * @return A schema descriptor for a non-uniform set type.
     */
    public S setDesc(final Elements<SchemaDescribable> elems);
}
