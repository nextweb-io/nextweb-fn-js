package io.nextweb.fn.js;

public interface JsWrapper<OriginalType> {

	public OriginalType getOriginal();

	public void setOriginal(OriginalType original);

}
