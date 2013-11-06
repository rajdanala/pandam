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
package org.pandcorps.platform;

import java.awt.image.*;

import org.pandcorps.core.*;
import org.pandcorps.core.img.*;
import org.pandcorps.game.core.*;
import org.pandcorps.pandam.*;
import org.pandcorps.pandax.tile.*;
import org.pandcorps.pandax.tile.Tile.*;

public class Castle {
    private static Panroom room = null;
    private static TileMap tm = null;
    private static Panmage timg = null;
    private static TileMapImage[][] imgMap = null;
    
    protected final static class ThroneScreen extends Panscreen {
        @Override
        protected final void load() throws Exception {
            final Pangine engine = Pangine.getEngine();
            engine.setBgColor(Pancolor.BLACK);
            room = PlatformGame.createRoom(256, 192);
            room.center();
            
            tm = new TileMap(Pantil.vmid(), room, ImtilX.DIM, ImtilX.DIM);
            Level.tm = tm;
            final BufferedImage buf = ImtilX.loadImage("org/pandcorps/platform/res/bg/ThroneRoom.png", 128, null);
            timg = engine.createImage("img.throne", buf);
            imgMap = tm.splitImageMap(timg);
            room.addActor(tm);
            
            tm.fillBackground(imgMap[0][4], 0, 0, 16, 1);
            for (int i = 0; i < 16; i += 2) {
                tm.rectangleBackground(3, 2, i, 1, 2, 2);
            }
            tm.fillBackground(imgMap[1][0], 0, 3, 16, 7);
            tm.fillBackground(imgMap[3][1], 0, 10, 16, 1);
            tm.fillBackground(imgMap[0][3], 0, 11, 16, 1);
            
            pillar(1);
            window(3);
            pillar(5);
            window(8);
            window(11);
            pillar(14);
            
            tm.rectangleBackground(5, 2, 8, 2, 2, 2);
            tm.initTile(10, 2).setBackground(imgMap[2][7]);
            for (int i = 11; i < 15; i += 2) {
                tm.rectangleBackground(6, 2, i, 2, 2, 1);
            }
            tm.initTile(15, 2).setBackground(imgMap[2][6]);
            tm.fillBackground(imgMap[1][6], 10, 3, 6, 1);
            tm.rectangleBackground(0, 7, 12, 3, 2, 4);
        }
    }
    
    private final static void pillar(final int i) {
        tm.initTile(i, 2).setBackground(imgMap[3][2]);
        tm.initTile(i, 3).setBackground(imgMap[2][2]);
        tm.fillBackground(imgMap[1][2], i, 4, 1, 6);
        tm.initTile(i, 10).setBackground(imgMap[0][2]);
        
        tm.fillBackground(imgMap[3][0], i - 1, 3, 1, 7);
        tm.initTile(i - 1, 10).setBackground(imgMap[2][0]);
    }
    
    private final static void window(final int i) {
        tm.initTile(i, 6).setBackground(imgMap[2][1]);
        tm.fillBackground(imgMap[1][1], i, 7, 1, 2);
        tm.initTile(i, 9).setBackground(imgMap[0][1]);
    }
}