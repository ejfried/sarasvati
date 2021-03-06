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
package com.googlecode.sarasvati.example;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSetMember;

/**
 * Example node to generate approvals
 *
 * @author Paul Lorenz
 */
public class ApprovalNode extends CustomNode
{
  @Override
  public void execute (Engine engine, NodeToken token)
  {
    TokenSetMember setMember = token.getTokenSetMember( "approvals" );
    System.out.println( "Hit approval " + setMember.getMemberIndex() );
    engine.complete( token, Arc.DEFAULT_ARC );
  }
}