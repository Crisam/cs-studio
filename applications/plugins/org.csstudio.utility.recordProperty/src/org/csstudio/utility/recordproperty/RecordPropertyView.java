package org.csstudio.utility.recordproperty;

import org.csstudio.utility.recordproperty.rdb.data.RecordPropertyGetRDB;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * RecordPropertyView creates view for the plugin.
 * 
 * @author Rok Povsic
 */
public class RecordPropertyView extends ViewPart {
	
	public static final String ID = "org.csstudio.utility.recordproperty";
	
	static final int COL_PV = 0;
	static final int COL_RDB = 1;
	static final int COL_VAL = 2;
	static final int COL_RMI = 3;
	
	private TableViewer tableViewer;
		
	/**
	 * Column names.
	 */
	static final String[] COLUMN_NAMES =
		new String[] { "pv", "rdb", "val", "rmi" };
	
	/**
	 * Data, that is filled in table.
	 */
	public RecordPropertyEntry[] entries;
	
	private Text record;
	
	public String recordName;
	
	public RecordPropertyView() {
	}
	
	/**
	 * Creates a GUI.
	 */
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		/**
		 * Temporary text and button - will be automatic later
		 */
		Group g = new Group(parent, SWT.NONE);
		g.setText("Record");
		
		g.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		record = new Text(g, SWT.WRAP | SWT.BORDER);
		record.setText("---- type or copy record name here ----");
		
		Button button = new Button(g, SWT.PUSH);
		button.setText("Get data");
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				// ignore (not called by buttons)
			}
			
			public void widgetSelected(final SelectionEvent e) {
				
				// Gets text (a record name) from Text Field.
				recordName = record.getText();
				
				// Deletes all spaces before and after real text.
				recordName = recordName.trim();
				
				/**
				 * New thread (Job) is created, so GUI does not freeze
				 * when it is collecting data.
				 */
				Job j = new Job("") {
					
					protected IStatus run(IProgressMonitor monitor) {
						
						/**
						 * Variable entries gets data, but does not print it in
						 * GUI yet, there would be Invalid thread access.
						 */
						RecordPropertyGetRDB rdb = new RecordPropertyGetRDB();
						entries = rdb.getData(recordName);
						
						/**
						 * asyncExec makes possible that GUI-changing can be done
						 * in separate thread that GUI.
						 */
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								/**
								 * Here data is printed in GUI.
								 */
								tableViewer.setInput(entries);
								
							}		
						});
						
						return new Status(IStatus.OK, Activator.PLUGIN_ID, "");
					}
				};
				
				j.setPriority(Job.SHORT);
				j.schedule();
			}
			
		});
		
		createTableViewer(parent);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.setContentProvider(new RecordPropertyContentProvider());
		tableViewer.setLabelProvider(new RecordPropertyLabelProvider());
				
		getSite().setSelectionProvider(tableViewer);
	}
		
	/**
	 * Inserts data to table columns.
	 * @deprecated Adding just one function to another is not needed. 
	 */
	/*
	private void startRecordPropertyRequest() {
		
		tableViewer.setInput(entries);
		
	}
	*/
	
	/**
	 * Creates a table.
	 * @param parent
	 */
	private void createTableViewer(final Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.setColumnProperties(COLUMN_NAMES);
		
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		addColumn(table, COL_PV,  Messages.RecordPropertyView_PV_COLUMN, 180);
		addColumn(table, COL_RDB, Messages.RecordPropertyView_RDB_COLUMN, 150);
		addColumn(table, COL_VAL, Messages.RecordPropertyView_VAL_COLUMN, 150);
		addColumn(table, COL_RMI, Messages.RecordPropertyView_RMI_COLUMN, 150);
		
	}
	
	/**
	 * Adds a column into a table.
	 * @param table a table
	 * @param index number of column
	 * @param text name of column
	 * @param width width of column
	 */
	private void addColumn(final Table table, final int index, final String text, final int width) {
		TableColumn column = new TableColumn(table, SWT.LEFT, index);
		column.setText(text);
		column.setWidth(width);
	}
		
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}
}
