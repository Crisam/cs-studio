
/* 
 * Copyright (c) 2010 Stiftung Deutsches Elektronen-Synchrotron, 
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS. 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND 
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE 
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR 
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE. 
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION, 
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS 
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY 
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 *
 */

package org.csstudio.archive.sdds.server.conversion;

import java.util.ArrayList;
import org.csstudio.archive.sdds.server.Activator;
import org.csstudio.archive.sdds.server.command.header.DataRequestHeader;
import org.csstudio.archive.sdds.server.conversion.handler.AlgorithmHandler;
import org.csstudio.archive.sdds.server.conversion.handler.AlgorithmHandlerException;
import org.csstudio.archive.sdds.server.conversion.handler.MethodNotImplementedException;
import org.csstudio.archive.sdds.server.conversion.handler.MinMaxAverageHandler;
import org.csstudio.archive.sdds.server.conversion.handler.AverageHandler;
import org.csstudio.archive.sdds.server.conversion.handler.NoFilterHandler;
import org.csstudio.archive.sdds.server.conversion.handler.TailRawHandler;
import org.csstudio.archive.sdds.server.data.EpicsRecordData;
import org.csstudio.archive.sdds.server.internal.ServerPreferenceKey;
import org.csstudio.archive.sdds.server.util.DataException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

/**
 * @author Markus Moeller
 *
 */
public class ConversionExecutor {
    
    /** Array of conversion algorithms */
    // private ConversionAlgorithm[] conversionMethod;
    
    /** Array of conversion handlers */
    private AlgorithmHandler[] conversionHandler;
    
    /**
     * 
     */
    public ConversionExecutor() {
        
        IPreferencesService pref = Platform.getPreferencesService();
        int maxSamples = pref.getInt(Activator.PLUGIN_ID,
                                     ServerPreferenceKey.P_MAX_SAMPLES_PER_REQUEST,
                                     10000, null);
        
        conversionHandler = new AlgorithmHandler[] {
                
        		new AverageHandler(maxSamples),
                new TailRawHandler(maxSamples),
                new AverageHandler(maxSamples),
                new AverageHandler(maxSamples),
                new AverageHandler(maxSamples),
                new NoFilterHandler(maxSamples),
                new MinMaxAverageHandler(maxSamples),
                new AverageHandler(maxSamples),
                new AverageHandler(maxSamples),
                new AverageHandler(maxSamples)
                
                /*
                new AverageHandler(),
                new RawHandler(),
                new SharpHandler(),
                new SplineHandler(),
                new FftHandler(),
                new NoFilterHandler(),
                new MinMaxAverageHandler(),
                new AverageHandler(),
                new AverageHandler(),
                new AverageHandler()
                 */
        };
    }
    
    /**
     * 
     * @param method
     * @param data
     * @return
     */
    public Iterable<EpicsRecordData> convertData(String name, EpicsRecordData[] data, DataRequestHeader header) {
        
        ArrayList<EpicsRecordData> result = null;
        
        try {
            result = (ArrayList<EpicsRecordData>) conversionHandler[header.getConversionTag() - 1].handle(header, data);
        } catch(DataException de) {
            de.printStackTrace();
        } catch(MethodNotImplementedException mnie) {
            mnie.printStackTrace();
        } catch (AlgorithmHandlerException ahe) {
            ahe.printStackTrace();
        }
        
        return result;
    }
}