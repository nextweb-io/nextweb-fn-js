package io.nextweb.fn.js;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

import de.mxro.fn.Closure;

@Export
@ExportClosure
public interface JsClosure extends Exportable, Closure<Object> {

	@Override
	public void apply(Object result);

}
