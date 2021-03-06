/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.jdbc;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GuardResponse;
import com.googlecode.sarasvati.JoinStrategy;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;

public class JdbcNodeRef implements Node,JdbcObject
{
  protected Long   id;

  protected JdbcNode node;
  protected JdbcGraph graph;

  protected String instance;

  public JdbcNodeRef (JdbcGraph graph, JdbcNode node, String instance )
  {
    this( null, graph, node, instance );
  }

  public JdbcNodeRef (Long id, JdbcGraph graph, JdbcNode node, String instance )
  {
    this.id       = id;
    this.graph    = graph;
    this.node     = node;
    this.instance = instance;
  }

  public Long getId ()
  {
    return id;
  }

  public void setId (Long id)
  {
    this.id = id;
  }

  public JdbcNode getNode ()
  {
    return node;
  }

  public void setNode (JdbcNode node)
  {
    this.node = node;
  }

  @Override
  public JdbcGraph getGraph ()
  {
    return graph;
  }

  public void setGraph (JdbcGraph graph)
  {
    this.graph = graph;
  }

  public String getGuard ()
  {
    return node.getGuard();
  }

  public String getInstance ()
  {
    return instance;
  }

  public void setInstance (String instance)
  {
    this.instance = instance;
  }

  public String getName ()
  {
    return node.getName();
  }

  public String getType ()
  {
    return node.getType();
  }

  @Override
  public JoinType getJoinType ()
  {
    return node.getJoinType();
  }

  @Override
  public JoinStrategy getJoinStrategy ()
  {
    return node.getJoinStrategy();
  }

  @Override
  public boolean isStart ()
  {
    return node.isStart() && getGraph().equals( node.getGraph() );
  }

  @Override
  public void backtrack (Engine engine, NodeToken token)
  {
    node.backtrack( engine, token );
  }

  @Override
  public boolean isBacktrackable(Engine engine, NodeToken token)
  {
    return node.isBacktrackable( engine, token );
  }

  @Override
  public GuardResponse guard (Engine engine, NodeToken token)
  {
    return node.guard( engine, token );
  }

  @Override
  public void execute (Engine engine, NodeToken token)
  {
    node.execute( engine, token );
  }

  @Override
  public boolean isExternal ()
  {
    return !graph.equals( getNode().getGraph() );
  }

  @Override
  public <T> T getAdaptor (Class<T> clazz)
  {
    return node.getAdaptor (clazz);
  }

  @Override
  public boolean isMutable ()
  {
    return false;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null )
        ? 0 : id.hashCode() );
    return result;
  }

  @Override
  public boolean equals (Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof JdbcNodeRef ) ) return false;
    final JdbcNodeRef other = (JdbcNodeRef)obj;
    if ( id == null )
    {
      if ( other.getId() != null ) return false;
    }
    else if ( !id.equals( other.getId() ) ) return false;
    return true;
  }
}