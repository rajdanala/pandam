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

import org.pandcorps.pandam.*;
import org.pandcorps.pandam.event.*;
import org.pandcorps.pandam.event.action.*;
import org.pandcorps.pandam.impl.FinPanple;
import org.pandcorps.pandax.tile.*;

public class Player extends Panctor implements StepListener {
	private final static int OFF_GROUNDED = -1;
	private final static int OFF_BUTTING = 17;
	private final static int VEL_WALK = 3;
	
	protected static int g = -1;
	private int v = 0;
	
	public Player() {
		final Pangine engine = Pangine.getEngine();
		setView(engine.createImage("guy", new FinPanple(8, 0, 0), null, null, "org/pandcorps/demo/res/img/SquareGuy.gif"));
		final Panteraction interaction = engine.getInteraction();
		interaction.register(this, interaction.KEY_SPACE, new ActionStartListener() {
			@Override public final void onActionStart(final ActionStartEvent event) { jump(); }});
		interaction.register(this, interaction.KEY_SPACE, new ActionEndListener() {
			@Override public final void onActionEnd(final ActionEndEvent event) { releaseJump(); }});
		interaction.register(this, interaction.KEY_RIGHT, new ActionListener() {
			@Override public final void onAction(final ActionEvent event) { right(); }});
		interaction.register(this, interaction.KEY_LEFT, new ActionListener() {
			@Override public final void onAction(final ActionEvent event) { left(); }});
	}
	
	private final void jump() {
		if (isGrounded()) {
			v = 10;
		}
	}
	
	private final void releaseJump() {
		if (v > 0) {
			v = 0;
		}
	}
	
	private final void right() {
		getPosition().addX(VEL_WALK);
	}
	
	private final void left() {
		getPosition().addX(-VEL_WALK);
	}

	@Override
	public final void onStep(final StepEvent event) {
		//if (Pangine.getEngine().getClock() == 10) {
		//	jump();
		//}
		final Panple pos = getPosition();
		final int offSol, mult, n;
		if (v > 0) {
			offSol = OFF_BUTTING;
			mult = 1;
		} else {
			offSol = OFF_GROUNDED;
			mult = -1;
		}
		n = v * mult;
		for (int i = 0; i < n; i++) {
			if (isSolid(offSol)) {
				//System.out.println("SOLID");
				v = 0;
				break;
			}
			pos.addY(mult);
		}
		if (!isGrounded()) {
			//System.out.println("JUMPING");
			v += g;
		}
	}
	
	private boolean isGrounded() {
		return isSolid(OFF_GROUNDED);
	}
	
	protected boolean isButting() {
		return isSolid(OFF_BUTTING);
	}
	
	private boolean isSolid(final int off) {
		final Panple pos = getPosition();
		final float y = pos.getY() + off;
		return isSolid(PlatformGame.tm.getContainer(pos.getX() - 8, y)) || isSolid(PlatformGame.tm.getContainer(pos.getX() + 7, y));
	}
	
	private boolean isSolid(final Tile tile) {
		return tile != null && tile.isSolid();
	}
}