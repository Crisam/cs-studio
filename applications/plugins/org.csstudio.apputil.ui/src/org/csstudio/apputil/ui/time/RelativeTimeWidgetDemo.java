package org.csstudio.apputil.ui.time;

import org.csstudio.apputil.time.RelativeTime;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/** Demo of TimestampWidget.
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class RelativeTimeWidgetDemo implements RelativeTimeWidgetListener
{
    public void updatedTime(RelativeTimeWidget source, RelativeTime time)
    {
        System.out.println("Time: " + time);
    }

    public void run()
    {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        
        RelativeTimeWidget w = new RelativeTimeWidget(shell, 0);
        w.addListener(this);

        shell.pack();
        shell.open();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        w.removeListener(this);
        display.dispose();
    }
    
    public static void main(String[] args)
    {
        new RelativeTimeWidgetDemo().run();
    }
}