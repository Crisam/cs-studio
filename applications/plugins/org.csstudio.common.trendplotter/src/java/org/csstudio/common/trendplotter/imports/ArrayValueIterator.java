/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.common.trendplotter.imports;

import java.util.Iterator;
import java.util.List;

import org.csstudio.archive.reader.ValueIterator;
import org.csstudio.data.values.IValue;
import org.epics.vtype.VType;

/** {@link ValueIterator} for {@link List} of {@link IValue}
 *  @author Kay Kasemir
 */
//TODO (jhatje): implement vType
public class ArrayValueIterator //implements ValueIterator
{
    final private Iterator<IValue> iter;

    public ArrayValueIterator(final List<IValue> values)
    {
        iter = values.iterator();
    }

    public boolean hasNext()
    {
        return iter.hasNext();
    }

    public IValue next() throws Exception
    {
        return iter.next();
    }

    public void close()
    {
        // NOP
    }
}
