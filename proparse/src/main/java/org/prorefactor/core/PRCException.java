/*******************************************************************************
 * Copyright (c) 2003-2015 John Green
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Green - initial API and implementation and/or initial documentation
 *******************************************************************************/ 
package org.prorefactor.core;

/**
 * ProRefactor Core Exception
 */
public class PRCException extends Exception {
  private static final long serialVersionUID = -5030805228326542339L;

  public PRCException() {
    super();
  }

  public PRCException(String message) {
    super(message);
  }

  public PRCException(Throwable cause) {
    super(cause);
  }

  public PRCException(String message, Throwable cause) {
    super(message, cause);
  }

}
