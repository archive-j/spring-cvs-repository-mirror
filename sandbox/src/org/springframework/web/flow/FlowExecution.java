/*
 * Copyright 2004-2005 the original author or authors.
 */
package org.springframework.web.flow;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * Subinterface of <code>FlowExecutionInfo</code> that exposes mutable
 * operations. Designed for use by the front <code>FlowController</code> and
 * <code>FlowSessionExecutionListener</code>, which are more privleged than
 * pure for-management clients. This interface may also be used in situations
 * where other privileged objects need access to flow definition configuration
 * details during session execution.
 * 
 * @author Keith Donald
 */
public interface FlowExecution extends FlowExecutionInfo, MutableAttributesAccessor {

	/**
	 * Add a flow execution listener; the added listener will receive callbacks
	 * on events occuring in this flow execution.
	 * @param listener The execution listener to add.
	 */
	public void addFlowExecutionListener(FlowExecutionListener listener);
	
	/**
	 * Register given collection of flow execution listeners with this flow execution.
	 * The added listeners will receive callbacks on events occuring in this flow
	 * execution.
	 * @param listeners The collection of listeners to add.
	 */
	public void addFlowExecutionListeners(FlowExecutionListener[] listeners);

	/**
	 * Remove an existing flow execution listener; the removed listener will no
	 * longer receive callbacks and if left unreferenced will be eligible for
	 * garbage collection.
	 * @param listener The execution listener to remove.
	 */
	public void removeFlowExecutionListener(FlowExecutionListener listener);

	/**
	 * Returns an iterator looping over the list of listeners registered with
	 * this flow execution.
	 */
	public Iterator getFlowExecutionListenersIterator();
	
	/**
	 * Returns this flow execution's active flow definition.
	 * @return The active flow definition
	 */
	public Flow getActiveFlow();

	/**
	 * Returns this flow execution's root flow definition.
	 * @return The root flow definition.
	 */
	public Flow getRootFlow();

	/**
	 * Returns this flow execution's current state definition.
	 * @return the current state definition.
	 */
	public AbstractState getCurrentState();

	/**
	 * Start this flow execution, transitioning it to the start state and
	 * returning the starting model and view descriptor.
	 * @param input Model input attributes to the flow execution
	 * @param request The current http request
	 * @param response The current http response
	 * @return The starting model and view.
	 */
	public ModelAndView start(Map input, HttpServletRequest request, HttpServletResponse response);

	/**
	 * Signal an occurence of the specified event in the (optionally) provided
	 * state of this flow execution.
	 * @param eventId The event that occured
	 * @param stateId The state the event occured in
	 * @param request The current http request
	 * @param response The current http response
	 * @return The next model and view descriptor to display for this flow.
	 */
	public ModelAndView signalEvent(String eventId, String stateId, HttpServletRequest request,
			HttpServletResponse response);

}