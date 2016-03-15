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
package org.prorefactor.core.unittest;

import java.io.File;

import org.prorefactor.core.JPNode;
import org.prorefactor.core.NodeTypes;
import org.prorefactor.core.nodetypes.ProparseDirectiveNode;
import org.prorefactor.core.unittest.util.UnitTestSports2000Module;
import org.prorefactor.macrolevel.IncludeRef;
import org.prorefactor.refactor.RefactorSession;
import org.prorefactor.treeparser.ParseUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;

import junit.framework.TestCase;

/**
 * For testing API and Backwards API access to the parser.
 */
public class ApiTest1 extends TestCase {
  private RefactorSession session;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    Injector injector = Guice.createInjector(new UnitTestSports2000Module());
    session = injector.getInstance(RefactorSession.class);
  }

  public void test01() throws Exception {
    File f = new File("src/test/resources/data/hello.p");
    ParseUnit pu = new ParseUnit(f, session);
    pu.treeParser01();
    int numDisplay = pu.getTopNode().query("DISPLAY").length;
    assertEquals(1, numDisplay);
  }

  public void test02() throws Exception {
    File f = new File("src/test/resources/data/no-undo.p");
    ParseUnit pu = new ParseUnit(f, session);
    pu.treeParser01();
    JPNode node = pu.getTopNode().findDirectChild(NodeTypes.DEFINE);
    assertEquals("VARIABLE", node.attrGetS("state2"));
  }

  public void test03() throws Exception {
    File f = new File("src/test/resources/data/include.p");
    ParseUnit pu = new ParseUnit(f, session);
    pu.treeParser01();
    // Three include file (including main file)
    assertEquals(3, pu.getMacroSourceArray().length);
    // First is inc.i, at line 3
    assertEquals("inc.i", ((IncludeRef) pu.getMacroSourceArray()[1]).getFileRefName());
    assertEquals(4, ((IncludeRef) pu.getMacroSourceArray()[1]).getPosition().getLine());
    // Second is inc2.i, at line 2 (in inc.i)
    assertEquals("inc2.i", ((IncludeRef) pu.getMacroSourceArray()[2]).getFileRefName());
    assertEquals(2, ((IncludeRef) pu.getMacroSourceArray()[2]).getPosition().getLine());
  }

  public void test04() throws Exception {
    File f = new File("src/test/resources/data/nowarn.p");
    ParseUnit pu = new ParseUnit(f, session);
    pu.parse();

    // Looking for the DEFINE node
    JPNode node1 = (JPNode) pu.getTopNode().findDirectChild(NodeTypes.DEFINE);
    assertNotNull(node1);
    assertTrue(node1.isStateHead());

    // Looking for the NO-UNDO node, and trying to get the state-head node
    JPNode node2 = (JPNode) pu.getTopNode().query(NodeTypes.NOUNDO).get(0);
    JPNode parent = node2;
    while (!parent.isStateHead()) {
      parent = parent.prevNode();
    }
    assertEquals(node1, parent);

    // Looking for the proparse directive
    JPNode left = node1.prevSibling();
    assertNotNull(left);
    assertTrue(left instanceof ProparseDirectiveNode);
    assertEquals("prolint-nowarn(shared)", ((ProparseDirectiveNode) left).getDirectiveText());

    left = left.prevSibling();
    assertNotNull(left);
    assertTrue(left instanceof ProparseDirectiveNode);
    assertEquals("prolint-nowarn(something)", ((ProparseDirectiveNode) left).getDirectiveText());
  }

  public void test05() throws Exception {
    File f = new File("src/test/resources/data/bugsfixed/bug19.p");
    ParseUnit pu = new ParseUnit(f, session);
    pu.parse();
    assertEquals("MESSAGE \"Hello\".", pu.getTopNode().toStringFulltext().trim());
  }

  public void test06() throws Exception {
    File f = new File("src/test/resources/data/abbrev.p");
    ParseUnit pu = new ParseUnit(f, session);
    pu.parse();
    assertFalse(pu.getTopNode().query(NodeTypes.LC).get(0).isAbbreviated());
    assertFalse(pu.getTopNode().query(NodeTypes.LC).get(0).isAbbreviated());
    assertTrue(pu.getTopNode().query(NodeTypes.FILEINFORMATION).get(0).isAbbreviated());
    assertFalse(pu.getTopNode().query(NodeTypes.FILEINFORMATION).get(1).isAbbreviated());
    assertTrue(pu.getTopNode().query(NodeTypes.SUBSTITUTE).get(0).isAbbreviated());
    assertFalse(pu.getTopNode().query(NodeTypes.SUBSTITUTE).get(1).isAbbreviated());
  }

}
