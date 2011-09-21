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
package example;

import spantable.DefaultSpanModel;
import spantable.Span;
import spantable.SpanModel;
import spantable.SpanTableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MyTableModel extends AbstractTableModel implements SpanTableModel {
    private SpanModel spanModel = new DefaultSpanModel();
    private List<MyData> data = new ArrayList<MyData>();
	private List<Index> indices = new ArrayList<Index>();

    class MyData {
        String string;
        private boolean flag;

        MyData(String string, boolean flag) {
            this.string = string;
            this.flag = flag;
        }

		void setFlag(boolean b) {
			boolean update = flag != b;
			flag = b;
			if (update) {
				rebuildIndices();
			}
		}
    }

	static class Index {
		int index;
		boolean firstRow;

		Index(int index, boolean firstRow) {
			this.index = index;
			this.firstRow = firstRow;
		}
	}

    MyTableModel() {
		super();
    }

    public void addString(String string) {
        data.add(new MyData(string, false));
		rebuildIndices();
    }

    public SpanModel getSpanModel() {
        return spanModel;
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return indices.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object rval = null;

		Index ndx = indices.get(rowIndex);
        MyData item = data.get(ndx.index);

		if (ndx.firstRow) {
            switch (columnIndex) {
                case 0:
                    rval = item.flag;
                    break;
                case 1:
                    rval = item.string.length();
                    break;
                case 2:
                    rval = item.string.charAt(0);
                    break;
                case 3:
                    rval = item.string.charAt(item.string.length() - 1);
                    break;
			}
		} else {
            if (columnIndex == 1) {
                rval = item.string;
            }
		}
        return rval;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
		Index ndx = indices.get(rowIndex);
        return ndx.firstRow && columnIndex == 0;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Index ndx = indices.get(rowIndex);
        MyData item = data.get(ndx.index);

		if (ndx.firstRow) {
			switch (columnIndex) {
				case 0:
					item.setFlag((Boolean)value);
					break;
			}
		}
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> rval = Object.class;
        switch (columnIndex) {
            case 0:
                rval = Boolean.class;
                break;
            case 1:
                rval = Object.class;
                break;
            case 2:
            case 3:
                rval = Character.class;
                break;
        }
        return rval;
	}

	private void rebuildIndices() {
		indices.clear();
		for (int i = 0; i < data.size(); ++i) {
			indices.add(new Index(i, true));

			if (!data.get(i).flag) {
				indices.add(new Index(i, false));
			}
		}

		// Rebuild the span model:
		spanModel.clear();
		for (int row = 0; row < indices.size(); ++row) {
			Index ndx = indices.get(row);
			if (!ndx.firstRow) {
				spanModel.addSpan(new Span(row, 0, row, 1, 1, getColumnCount()));
			}
		}

		fireTableStructureChanged();
	}
}

