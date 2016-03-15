/*******************************************************************************
 * Copyright (c) 2016 Gilles Querret
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gilles Querret - initial API and implementation and/or initial documentation
 *******************************************************************************/ 
package org.prorefactor.core.unittest;

import java.io.File;

import org.prorefactor.core.unittest.util.UnitTestSports2000Module;
import org.prorefactor.refactor.RefactorSession;
import org.prorefactor.treeparser.ParseUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;

import junit.framework.TestCase;

public class MetricsTest extends TestCase {
  private RefactorSession session;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    Injector injector = Guice.createInjector(new UnitTestSports2000Module());
    session = injector.getInstance(RefactorSession.class);
  }

  public void test01() throws Exception {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/data/include.p"), session);
    unit.treeParser01();

    assertEquals(2, unit.getMetrics().getLoc());
    assertEquals(6, unit.getMetrics().getComments());
  }

  public void test02() throws Exception {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/data/inc3.i"), session);
    unit.lex();

    assertEquals(1, unit.getMetrics().getLoc());
    assertEquals(2, unit.getMetrics().getComments());
  }

}
