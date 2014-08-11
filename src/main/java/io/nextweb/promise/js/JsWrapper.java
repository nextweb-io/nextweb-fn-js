package io.nextweb.promise.js;

public interface JsWrapper<OriginalType> {

	public OriginalType getOriginal();

	public void setOriginal(OriginalType original);

}
