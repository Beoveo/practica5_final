package es.ucm.fdi.tp.exception;

import es.ucm.fdi.tp.base.model.GameError;


public class NotValidArgumentException extends GameError{
	private static final long serialVersionUID = 1L;

	public NotValidArgumentException(String msg) {
		super(msg);
	}

}