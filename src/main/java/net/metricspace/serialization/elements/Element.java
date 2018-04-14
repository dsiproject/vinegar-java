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
package net.metricspace.serialization.elements;

/**
 * Typed element tag.  These are used as keys in a
 * heterogeneously-typed map of elements to values.
 *
 * @param <T> The type of data of this element.
 */
public class Element<T> {
    /**
     * The name of the {@code Element}.
     */
    private final String name;

    /**
     * Initialize an {@code Element} with its name.
     *
     * @param name The element's name.
     */
    public Element(final String name) {
        this.name = name;
    }

    /**
     * Equality test against another {@code Element}.
     *
     * @param other The {@code Element} against which to compare.
     * @return Whether the {@link #name}s are equal.
     */
    public boolean equals(final Element<T> other) {
        return name.equals(other.name);
    }

    /**
     * Get the element's human-readable name.
     *
     * @return The element's name.
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object other) {
        if (other instanceof Element) {
            return equals((Element<T>)other);
        } else {
            return false;
        }
    }
}
