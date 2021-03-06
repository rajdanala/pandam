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
package org.pandcorps.pandam;

public abstract class Pansound {
	protected static Pansound currentMusic = null;
	
	public final boolean changeMusic() {
		if (currentMusic != this) {
			startMusic();
			return true;
		}
		return false;
	}
	
	public final void startMusic() {
		final Panaudio audio = Pangine.getEngine().getAudio();
		if (!audio.isMusicEnabled()) {
			currentMusic = this; // Change even if disabled; if sound is enabled later, it should play what was picked now
			return;
		}
		audio.stopMusic();
		currentMusic = this; // Must set after stopping; stopMusic will null this
		try {
			runMusic();
		} catch (final Exception e) {
			throw Panception.get(e);
		}
	}
	
	public final void startSound() {
		if (!Pangine.getEngine().getAudio().isSoundEnabled()) {
			return;
		}
		try {
			runSound();
		} catch (final Exception e) {
			throw Panception.get(e);
		}
	}
	
	protected abstract void runMusic() throws Exception;
	
	protected abstract void runSound() throws Exception;
	
	public final static void startMusic(final Pansound music) {
		if (music != null) {
			music.startMusic();
		}
	}
	
	public final static void startSound(final Pansound sound) {
		if (sound != null) {
			sound.startSound();
		}
	}
}
