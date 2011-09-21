/*
 * Copyright (c) 2011, Jonathan Keatley. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package spantable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SpanTest {
    private Span span1;
    private Span span2;
    private Span span3;
    private Span span4;

    @Before
    public void init() {
        span1 = new Span(8, 14, 2, 5);
        span2 = new Span(3, 10, 4, 11, 2, 2);
        span3 = new Span(8, 14, 8, 14, 2, 5);
        span4 = new Span(9, 15, 1, 4);
    }

    @Test
    public void testProperties() {
        Assert.assertEquals(8, span1.getRow());
        Assert.assertEquals(14, span1.getColumn());
        Assert.assertEquals(2, span1.getHeight());
        Assert.assertEquals(5, span1.getWidth());
        Assert.assertEquals(8, span1.getActiveRow());
        Assert.assertEquals(14, span1.getActiveColumn());
    }

    @Test
    public void testIsDefined() {
        Assert.assertTrue(span1.isDefined(8, 14));
        Assert.assertTrue(span1.isDefined(9, 14));
        Assert.assertTrue(span1.isDefined(8, 18));
        Assert.assertTrue(span1.isDefined(9, 18));
        Assert.assertFalse(span1.isDefined(7, 14));
        Assert.assertFalse(span1.isDefined(10, 14));
        Assert.assertFalse(span1.isDefined(7, 18));
        Assert.assertFalse(span1.isDefined(10, 18));
        Assert.assertFalse(span1.isDefined(8, 13));
        Assert.assertFalse(span1.isDefined(8, 19));
        Assert.assertFalse(span1.isDefined(9, 13));
        Assert.assertFalse(span1.isDefined(9, 19));
    }

    @Test
    public void testIsActive() {
        Assert.assertTrue(span1.isActive(8, 14));
        Assert.assertFalse(span1.isActive(9, 15));
        Assert.assertTrue(span2.isActive(4, 11));
        Assert.assertFalse(span2.isActive(3, 10));
    }

    @Test
    public void testIntersects() {
        Assert.assertTrue(span1.intersects(span4));
        Assert.assertFalse(span1.intersects(span2));
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(span1, span3);
        Assert.assertNotSame(span1, span3);
        Assert.assertFalse(span1.equals(span2));
    }

    @Test
    public void testHashcode() {
        Assert.assertEquals(728139, span1.hashCode());
        Assert.assertEquals(315246, span2.hashCode());
        Assert.assertEquals(span1.hashCode(), span3.hashCode());
    }
}
