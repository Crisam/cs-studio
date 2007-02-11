/* 
 * Copyright (c) 2006 Stiftung Deutsches Elektronen-Synchroton, 
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
package org.csstudio.sds.components.ui.internal.feedback;

import org.csstudio.sds.components.model.AbstractPolyElement;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.commands.Command;

/**
 * A command, that changes the point list of a poly element.
 * @author Sven Wende
 *
 */
public final class ChangePolyPointsCommand extends Command {
	/**
	 * The polyline element, whose points should be changed.
	 */
	private AbstractPolyElement _polyElement;

	/**
	 * The old point list.
	 */
	private PointList _oldPoints;

	/**
	 * The new point list.
	 */
	private PointList _newPoints;

	/**
	 * Constructor.
	 * @param polyElement the polyline element, whose points should be changed
	 * @param newPoints the new point list
	 */
	public ChangePolyPointsCommand(final AbstractPolyElement polyElement,
			final PointList newPoints) {
		assert polyElement != null;
		assert newPoints != null;
		_polyElement = polyElement;
		_newPoints = newPoints;
	}

	/**
	* {@inheritDoc}
	 */
	
	@Override
	public void execute() {
		_oldPoints = _polyElement.getPoints();
		_polyElement.setPoints(_newPoints);
	}

	/**
	* {@inheritDoc}
	 */
	@Override
	public void undo() {
		_polyElement.setPoints(_oldPoints);
	}

}
