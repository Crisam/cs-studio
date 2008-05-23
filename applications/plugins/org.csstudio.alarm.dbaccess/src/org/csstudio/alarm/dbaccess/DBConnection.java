/* 
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron, 
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
 */
package org.csstudio.alarm.dbaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;

import org.csstudio.platform.logging.CentralLogger;

import oracle.jdbc.OracleDriver;

/**
 * Creates and holds the SQL connection. If the timer terminates
 * the connection will be closed. The timer is reseted with each 
 * DB access.
 * 
 * @author jhatje
 * @author $Author$
 * @version $Revision$
 * @since 15.05.2008
 */
public class DBConnection {

    private String _url = "jdbc:oracle:thin:@(DESCRIPTION = "
        + "(ADDRESS = (PROTOCOL = TCP)(HOST = dbsrv01.desy.de)(PORT = 1521)) "
        + "(ADDRESS = (PROTOCOL = TCP)(HOST = dbsrv02.desy.de)(PORT = 1521)) "
        + "(ADDRESS = (PROTOCOL = TCP)(HOST = dbsrv03.desy.de)(PORT = 1521)) "
        + "(LOAD_BALANCE = yes) " + "(CONNECT_DATA = " + "(SERVER = DEDICATED) "
        + "(SERVICE_NAME = desy_db.desy.de) " + "(FAILOVER_MODE = " + "(TYPE = NONE) "
        + "(METHOD = BASIC) " + "(RETRIES = 180) " + "(DELAY = 5) " + ")" + ")" + ")";

    private String _user = "KRYKAMS";
    private String _password = "krykams";

	private static DBConnection _instance;
	
	private Connection _dbConnection;
	
	private Timer _timer;
	
	private CloseConnectionTimerTask _timerTask;
	
	private DBConnection() {
		_timer = new Timer();
        try {
            DriverManager.registerDriver(new OracleDriver());

        } catch (Exception e) {
        	CentralLogger.getInstance().error(this, "SQL Driver error " + e.getMessage());
        }
	}
	
	public static DBConnection getInstance() {
        if(_instance == null){
            _instance = new DBConnection();
        }
        return _instance;
	}
	
	public Connection getConnection() {
		try {
			if ((_dbConnection == null) || (_dbConnection.isClosed() == true)) {
			    try {
			        _dbConnection = DriverManager.getConnection(_url, _user, _password);
			        _timerTask = new CloseConnectionTimerTask(_dbConnection);
			        _timer.schedule(_timerTask, 60 * 1000, 60 * 1000);
			    } catch (SQLException e) {
			    	CentralLogger.getInstance().error(this, "SQL Connection error " + e.getMessage());
			    }
			}
		} catch (SQLException e) {
	    	CentralLogger.getInstance().error(this, "SQL Connection error " + e.getMessage());
		}
		_timerTask.set_lastDBAcccessInMillisec(System.currentTimeMillis());
		return _dbConnection;
	}
}
