/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package org.springframework.web.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>Abstract base class for custom command controllers. Autopopulates a
 * command bean from the request. For command validation, a validator
 * (property inherited from BaseCommandController) can be used.</p>
 * <p>This command controller should preferrable not be used to handle form
 * submission, because functionality for forms is more offered in more
 * detail by the {@link org.springframework.web.servlet.mvc.AbstractFormController
 * AbstractFormController} and its corresponding implementations.</p>
 *
 * <p><b><a name="config">Exposed configuration properties</a>
 * (<a href="AbstractController.html#config">and those defined by superclass</a>):</b><br>
 * <i>none</i> (so only those available in superclass).</p>
 *
 * <p><b><a name="workflow">Workflow
 * (<a name="BaseCommandController.html#workflow">and that defined by superclass</a>):</b><br>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see #setCommandClass
 * @see #setBeanName
 * @see #setValidator
 */
public abstract class AbstractCommandController extends BaseCommandController {

	/**
	 * Create a new AbstractCommandController.
	 */
	public AbstractCommandController() {
	}

	/**
	 * Create a new AbstractCommandController.
	 * @param commandClass class of the command bean
	 */
	public AbstractCommandController(Class commandClass) {
		setCommandClass(commandClass);
	}

	/**
	 * Create a new AbstractCommandController.
	 * @param commandClass class of the command bean
	 * @param beanName name of the command bean
	 */
	public AbstractCommandController(Class commandClass, String beanName) {
		setCommandClass(commandClass);
		setBeanName(beanName);
	}
	
	protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object command = getCommand(request);
		ServletRequestDataBinder binder = bindAndValidate(request, command);
		return handle(request, response, command, binder.getErrors());
	}

	/**
	 * Template method for request handling, providing a populated and validated instance
	 * of the command class, and an Errors object containing binding and validation errors.
	 * <p>Can call errors.getModel() to populate the ModelAndView model with the command
	 * and the Errors instance, under the specified bean name.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param command the populated command object
	 * @param errors Errors instance containing errors
	 * @return a ModelAndView to render, or null if handled directly
	 * @see org.springframework.validation.Errors
	 */
	protected abstract ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
	                                       Object command, BindException errors)
	    throws ServletException, IOException;

}
