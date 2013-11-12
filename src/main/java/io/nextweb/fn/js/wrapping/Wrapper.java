package io.nextweb.fn.js.wrapping;

public interface Wrapper {

	public boolean canWrap(Object input);

	public Object wrap(Object input);

	public boolean canUnwrap(Object input);

	public Object unwrap(Object input);

}
