package org.springframework.web.servlet.view;

import java.util.Locale;

import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.View;

/**
 * Simple implementation of ViewResolver that allows for direct resolution of
 * symbolic view names to URLs, without explicit mapping definition. This is
 * appropriate if your symbolic names match the names of your view resources
 * in a straightforward manner, without the need for arbitrary mappings.
 *
 * <p>Supports InternalResourceView (i.e. Servlets and JSPs), and subclasses
 * like JstlView. The view class for all views generated by this resolver
 * can be specified via setViewClass.
 *
 * <p>View names can either be resource URLs themselves, or get augmented by a
 * specified prefix and/or suffix. Exporting an attribute that holds the
 * RequestContext to all views is explicitly supported.
 *
 * <p>Example: prefix="/WEB-INF/jsp/", suffix=".jsp", viewname="test" ->
 * "/WEB-INF/jsp/test.jsp"
 *
 * <p>BTW, it's good practice to put JSP files that just serve as views under
 * WEB-INF, to hide them from direct access (e.g. via a manually entered URL).
 * Only controllers will be able to access them then.
 *
 * <p>Note: This class does not supported localized resolution, i.e. resolving
 * a symbolic view name to different resources depending on the current locale.
 *
 * @author Juergen Hoeller
 * @since 17.02.2003
 * @see #setViewClass
 * @see #setPrefix
 * @see #setSuffix
 * @see #setRequestContextAttribute
 * @see InternalResourceView
 * @see JstlView
 */
public class InternalResourceViewResolver extends AbstractCachingViewResolver {

	private Class viewClass = InternalResourceView.class;

	private String prefix = "";

	private String suffix = "";

	private String requestContextAttribute = null;

	/**
	 * Set the view class that should be used to create views.
	 * @param viewClass class that is assignable to InternalResourceView
	 */
	public void setViewClass(Class viewClass) {
		if (viewClass == null || !InternalResourceView.class.isAssignableFrom(viewClass)) {
			throw new IllegalArgumentException("View class must be an InternalResourceView: " + viewClass);
		}
		this.viewClass = viewClass;
	}

	/**
	 * Set the prefix that gets applied to view names when building a URL.
	 * @param prefix view name prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Set the suffix that gets applied to view names when building a URL.
	 * @param suffix view name suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Set the name of the RequestContext attribute for all views,
	 * or null if not needed.
	 * @param requestContextAttribute name of the RequestContext attribute
	 */
	public void setRequestContextAttribute(String requestContextAttribute) {
		this.requestContextAttribute = requestContextAttribute;
	}

	/**
	 * This implementation returns just the view name,
	 * as InternalResourceViewResolver doesn't support localized resolution.
	 */
	protected String getCacheKey(String viewName, Locale locale) {
		return viewName;
	}

	protected View loadView(String viewName, Locale locale) {
		InternalResourceView view = (InternalResourceView) BeanUtils.instantiateClass(this.viewClass);
		view.setUrl(this.prefix + viewName + this.suffix);
		view.setRequestContextAttribute(this.requestContextAttribute);
		return view;
	}

}
