/*
Copyright (c) 2009-2014, Andrew M. Martin
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
   disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Pandam nor the names of its contributors may be used to endorse or promote products derived from this
   software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/
package org.pandcorps.pandax.tile;

import org.pandcorps.pandam.*;
import org.pandcorps.pandax.tile.Tile.*;

public class TileActor extends Panctor {
    private TileMap map = null;
    private Object view = null;
    
    public void setViewFromBackground(final TileMap map, final Tile t) {
        this.map = map;
        view = t.background;
    }
    
    public void setViewFromForeground(final TileMap map, final Tile t) {
        this.map = map;
        view = t.foreground;
    }
    
    public void setView(final TileActor actor) {
    	map = actor.map;
    	view = actor.view;
    }
    
    public void setView(final TileMap map, final TileMapImage view) {
        this.map = map;
        this.view = view;
    }
    
    @Override
    protected void updateView() {       
    }

    @Override
    public Pansplay getCurrentDisplay() {
        return map.tileDisplay;
    }

    @Override
    protected void renderView(final Panderer renderer) {
        if (map != null) {
            final Panple pos = getPosition();
            map.render(renderer, map.getLayer(), view, pos.getX(), pos.getY(), pos.getZ());
        }
    }
}
