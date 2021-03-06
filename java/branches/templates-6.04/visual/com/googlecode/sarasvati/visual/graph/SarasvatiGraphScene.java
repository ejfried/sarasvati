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
package com.googlecode.sarasvati.visual.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import org.netbeans.api.visual.layout.LayoutFactory.ConnectionWidgetLayoutAlignment;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.visual.GraphLookAndFeel;
import com.googlecode.sarasvati.visual.common.GraphSceneImpl;

public class SarasvatiGraphScene extends GraphSceneImpl<Node, Arc>
{
  protected GraphLookAndFeel lookAndFeel;

  protected static final Font ARC_LABEL_FONT = Font.decode( "serif bold 11" );

  public SarasvatiGraphScene (Graph graph, GraphLookAndFeel lookAndFeel)
  {
    this.lookAndFeel = lookAndFeel;

    if ( graph != null )
    {
      GraphTree graphTree = new GraphTree( graph );
      for ( Node node : graph.getNodes() )
      {
        Widget widget = addNode( node );

        GraphTreeNode treeNode = graphTree.getTreeNode( node );
        Point origin = new Point( treeNode.getOriginX(), treeNode.getOriginY() );
        widget.setPreferredLocation( origin );
        widget.resolveBounds( origin, null );
      }

      for ( Arc arc : graph.getArcs() )
      {
        if ( lookAndFeel.drawSelfArcs() || !arc.getStartNode().equals( arc.getEndNode() ) )
        {
          ConnectionWidget widget = (ConnectionWidget)addEdge( arc );
          setEdgeSource( arc, arc.getStartNode() );
          setEdgeTarget( arc, arc.getEndNode() );

          widget.resolveBounds( null, null );

          if ( lookAndFeel.drawArcLabels() && arc.getName() != null )
          {
            LabelWidget arcLabel = new LabelWidget( this, arc.getName() );
            arcLabel.setFont( ARC_LABEL_FONT );
            arcLabel.setForeground( Color.BLUE );
            arcLabel.setOpaque( true );
            widget.addChild( arcLabel );
            widget.setConstraint( arcLabel, ConnectionWidgetLayoutAlignment.CENTER, 30 );
          }
        }
      }

      revalidate();
    }
  }

  @Override
  protected Widget widgetForNode (Node node)
  {
    return lookAndFeel.newWidget( node, this );
  }
}