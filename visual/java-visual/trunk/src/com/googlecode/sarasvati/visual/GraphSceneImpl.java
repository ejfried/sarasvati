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
package com.googlecode.sarasvati.visual;

import java.util.HashMap;
import java.util.Map;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

public abstract class GraphSceneImpl<N,E> extends GraphScene<N, E>
{
  protected LayerWidget mainLayer = new LayerWidget( this );
  protected LayerWidget intrLayer = new LayerWidget( this );
  protected LayerWidget connLayer = new LayerWidget( this );

  protected ShortestPathRouterAdapter router;
  protected WidgetAction moveAction = ActionFactory.createAlignWithMoveAction( mainLayer, intrLayer, null );

  protected Map<N,Anchor> anchorMap = new HashMap<N,Anchor>();

  public GraphSceneImpl()
  {
    addChild( mainLayer );
    addChild( intrLayer );
    addChild( connLayer );

    router = new ShortestPathRouterAdapter( this );
  }

  public LayerWidget getConnectionLayer ()
  {
    return connLayer;
  }

  @Override
  protected void attachEdgeSourceAnchor(E edge, N oldSourceNode, N sourceNode)
  {
    ConnectionWidget edgeWidget = (ConnectionWidget) findWidget( edge );
    edgeWidget.setSourceAnchor( anchorMap.get( sourceNode ) );
    router.setDirty();
  }

  @Override
  protected void attachEdgeTargetAnchor(E edge, N oldTargetNode, N targetNode)
  {
    ConnectionWidget edgeWidget = (ConnectionWidget) findWidget( edge );
    edgeWidget.setTargetAnchor( anchorMap.get( targetNode ) );
    router.setDirty();
  }

  @Override
  protected Widget attachEdgeWidget(E edge)
  {
    PathTrackingConnectionWidget conn = new PathTrackingConnectionWidget( router, this );
    conn.setRouter( router );
    conn.setTargetAnchorShape( AnchorShape.TRIANGLE_FILLED );
    connLayer.addChild( conn );
    return conn;
  }

  @Override
  protected Widget attachNodeWidget(N node)
  {
    Widget widget = widgetForNode( node );
    mainLayer.addChild( widget );
    widget.getActions().addAction( moveAction );
    anchorMap.put( node, AnchorFactory.createRectangularAnchor( widget ) );
    router.addNodeWidget( widget );
    return widget;
  }

  protected abstract Widget widgetForNode (N node);

  @Override
  protected void detachNodeWidget (N node, Widget widget)
  {
    super.detachNodeWidget( node, widget );
    router.removeNodeWidget( widget );
    anchorMap.remove( node );
  }
}