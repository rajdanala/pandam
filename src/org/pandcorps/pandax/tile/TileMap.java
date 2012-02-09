/*
Copyright (c) 2009-2011, Andrew M. Martin
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

import org.pandcorps.pandam.Panctor;
import org.pandcorps.pandam.Panderer;
import org.pandcorps.pandam.Panlayer;
import org.pandcorps.pandam.Panmage;
import org.pandcorps.pandam.Panple;
import org.pandcorps.pandam.Panroom;
import org.pandcorps.pandam.Pansplay;

public class TileMap extends Panctor {
    
    private final Tile[] tiles;
    
    private final int w;
    
    private final int h;
    
    /*package*/ final int tw;
    
    /*package*/ final int th;
    
    /*package*/ Object occupantDepth = null;
    
    public TileMap(final String id, final int w, final int h, final int tw, final int th) {
        super(id);
        tiles = new Tile[w * h];
        this.w = w;
        this.h = h;
        this.tw = tw;
        this.th = th;
    }
    
    public TileMap(final String id, final Panroom room, final int tw, final int th) {
        this(id, (int) (room.getSize().getX() / tw), (int) (room.getSize().getY() / th), tw, th);
    }
    
    private final boolean isBad(final int i, final int j) {
        return i < 0 || j < 0 || i >= w || j >= h;
    }
    
    private final int getIndex(final int i, final int j) {
        return j * w + i;
    }
    
    public final Tile getTile(final int i, final int j) {
        if (isBad(i, j)) {
            return null;
        }
        return tiles[getIndex(i, j)];
    }
    
    ///*package*/ final void setTile(final int i, final int j, final Tile tile) {
    //    tiles[getIndex(i, j)] = tile;
    //    tile.map = this;
    //}
    
    public final void fillBackground(final Panmage background) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                Tile tile = getTile(i, j);
                if (tile == null) {
                    //tile = new Tile(this, i, j);
                    tile = initTile(i, j);
                }
                tile.setBackground(background);
            }
        }
    }
    
    public final Tile initTile(final int i, final int j) {
        if (isBad(i, j)) {
            throw new IllegalArgumentException(i + ", " + j + " is out of bounds");
        }
        final int index = getIndex(i, j);
        if (tiles[index] != null) {
            throw new IllegalArgumentException(i + ", " + j + " is already initialized");
        }
        final Tile tile = new Tile(this, i, j);
        tiles[index] = tile;
        return tile;
    }
    
    @Override
    protected void updateView() {       
    }

    @Override
    public Pansplay getCurrentDisplay() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void renderView(final Panderer renderer) {
        //System.out.println(new java.util.Date());
        final Panple pos = getPosition();
        final float x = pos.getX();
        final float y = pos.getY();
        final float z = pos.getZ();
        final float foregroundDepth = getForegroundDepth();
        final Panlayer layer = getLayer();
        for (int j = 0; j < h; j++) {
            final int off = j * w;
            final float yjth = y + j * th;
            for (int i = 0; i < w; i++) {
                final Tile tile = tiles[off + i];
                if (tile == null) {
                    continue;
                }
                final float xitw = x + (i * tw);
                if (tile.foreground != null) {
                    renderer.render(layer, tile.foreground, xitw, yjth, foregroundDepth);
                }
                if (tile.background != null) {
                    renderer.render(layer, tile.background, xitw, yjth, z);
                }
            }
        }
    }
    
    public void setOccupantDepth(final float occupantDepth) {
        this.occupantDepth = Float.valueOf(occupantDepth);
    }
    
    public void setOccupantDepth(final DepthMode occupantDepth) {
        this.occupantDepth = occupantDepth;
    }
    
    public float getForegroundDepth() {
        //return Float.MAX_VALUE;
        //return z + 1;
        return getPosition().getZ() + (h * th);
    }
    
    public final int getWidth() {
        return w;
    }
    
    public final int getHeight() {
        return h;
    }
}
