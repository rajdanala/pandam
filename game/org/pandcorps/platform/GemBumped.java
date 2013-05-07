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

import org.pandcorps.game.core.ImtilX;
import org.pandcorps.pandam.Panple;
import org.pandcorps.pandam.event.*;
import org.pandcorps.pandax.Pandy;
import org.pandcorps.pandax.tile.Tile;

public class GemBumped extends Pandy {
	int age = 0;
	
	public GemBumped(final Player player, final Tile tile) {
		super(Tiles.g);
		Gem.collect(player);
		setView(PlatformGame.gemAnm);
		final Panple pos = tile.getPosition();
		PlatformGame.setPosition(this, pos.getX(), pos.getY() + ImtilX.DIM, PlatformGame.DEPTH_SHATTER);
		getVelocity().set(0, 6);
        PlatformGame.room.addActor(this);
	}
	
	@Override
	public final void onStep(final StepEvent event) {
		super.onStep(event);
		age++;
		if (age >= 12) {
			Gem.spark(this);
		}
	}
}