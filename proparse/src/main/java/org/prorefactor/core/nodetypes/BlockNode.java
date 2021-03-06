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
package org.prorefactor.core.nodetypes;

import org.prorefactor.core.IConstants;
import org.prorefactor.core.JPNode;
import org.prorefactor.core.ProToken;
import org.prorefactor.treeparser.Block;

public class BlockNode extends JPNode {
  private static final long serialVersionUID = 6062037678978630381L;

  /** For creating from persistent storage */
  public BlockNode() {
    super();
  }

  public BlockNode(ProToken t) {
    super(t);
  }

  public BlockNode(int file, int line, int column) {
    super(file, line, column);
  }

  public Block getBlock() {
    Block block = (Block) getLink(IConstants.BLOCK);
    assert block != null;
    return block;
  }

  /**
   * Every JPNode subtype has its own index. Used for persistent storage.
   */
  @Override
  public int getSubtypeIndex() {
    return 2;
  }

  public void setBlock(Block block) {
    setLink(IConstants.BLOCK, block);
  }

}
