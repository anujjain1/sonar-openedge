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
package org.prorefactor.refactor.unittest;

import java.io.File;
import java.util.ArrayList;

import org.prorefactor.core.JPNode;
import org.prorefactor.core.NodeTypes;
import org.prorefactor.core.unittest.util.UnitTestSports2000Module;
import org.prorefactor.refactor.PUB;
import org.prorefactor.refactor.RefactorSession;

import com.google.inject.Guice;
import com.google.inject.Injector;

import junit.framework.TestCase;

/**
 * Test "Parse Unit Binary" files.
 * 
 * All tests currently disabled (in pom.xml)
 */
public class PUBTest extends TestCase {

  private RefactorSession session;
  private String relPath = "src/test/resources/data/pub/test01.p";
  private File parseFile;
  private PUB pub;

  public void setUp() throws Exception {
    Injector injector = Guice.createInjector(new UnitTestSports2000Module());
    session = injector.getInstance(RefactorSession.class);

    session.setProjectBinariesEnabledOn();
    parseFile = new File(relPath);
    pub = new PUB(session, parseFile.getCanonicalPath());
  }

  public void testBuild() throws Exception {
    pub.build();
    pub = new PUB(session, parseFile.getCanonicalPath());
    assertTrue(pub.load());
  }

  public void testIncludeName() throws Exception {
    assertTrue(pub.load());
    String[] fileIndex = pub.getTree().getFilenames();

    // Test that file at index 1 matches the include file name that we expect
    File iGet = new File(fileIndex[1]);
    File iBase = new File("src/test/resources/data/pub/test01.i");
    assertTrue(iGet.getCanonicalPath().equals(iBase.getCanonicalPath()));
  }

  public void testTimeStamp() throws Exception {
    // Test that the file timestamp checking works
    long origTime = parseFile.lastModified();
    assertTrue(parseFile.setLastModified(System.currentTimeMillis() + 10000));
    assertFalse(pub.load());
    assertTrue(parseFile.setLastModified(origTime));
    assertTrue(pub.load());
  }

  public void testIncludeTimeStamp() throws Exception {
    // Test that the file timestamp checking works on included files
    File iBase = new File("src/test/resources/data/pub/test01.i");
    long origTime = iBase.lastModified();
    iBase.setLastModified(System.currentTimeMillis() + 10000);
    assertFalse(pub.load());
    iBase.setLastModified(origTime);
    assertTrue(pub.load());

  }

  public void testSchemaLoad() throws Exception {
    assertTrue(pub.load());

    // Test that the schema load works
    ArrayList tables = new ArrayList();
    pub.copySchemaTableLowercaseNamesInto(tables);
    assertTrue(tables.size() == 1);
    assertTrue(tables.get(0).toString().equals("sports2000.customer"));
    ArrayList fields = new ArrayList();
    pub.copySchemaFieldLowercaseNamesInto(fields, "sports2000.customer");
    assertTrue(fields.size() == 1);
    assertTrue(fields.get(0).toString().equals("name"));

  }

  public void testImportTable() throws Exception {
    assertTrue(pub.load());

    // Test the import table.
    PUB.SymbolRef[] imports = pub.getImportTable();
    PUB.SymbolRef imp = imports[0];
    assertTrue(imp.progressType == NodeTypes.VARIABLE);
    assertTrue(imp.symbolName.equals("sharedChar"));

  }

  public void testExportTable() throws Exception {
    assertTrue(pub.load());

    // Test the export table.
    PUB.SymbolRef[] exports = pub.getExportTable();
    PUB.SymbolRef exp = exports[0];
    assertTrue(exp.progressType == NodeTypes.FRAME);
    assertTrue(exp.symbolName.equals("myFrame"));

  }

  public void testComments() throws Exception {
    assertTrue(pub.load());

    // Test that there are comments in front of the first real node
    JPNode topNode = pub.getTree();
    assertTrue(topNode.firstNaturalChild().getComments().length() > 2);

  }

  public void testText() throws Exception {
    assertTrue(pub.load());

    // Test that the ID nodes have text.
    JPNode topNode = pub.getTree();
    for (JPNode node : topNode.query(NodeTypes.ID)) {
      assertTrue(node.getText().length() > 0);
    }

  }

}
