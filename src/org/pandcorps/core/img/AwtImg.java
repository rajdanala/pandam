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
package org.pandcorps.core.img;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import org.pandcorps.core.*;

public final class AwtImg extends Img {
	private BufferedImage raw;
	
	public AwtImg(final BufferedImage raw) {
		this.raw = raw;
	}
	
	@Override
	public final Object getRaw() {
		return raw;
	}
	
	@Override
	public final void swapRaw(final Img img) {
		final AwtImg o = (AwtImg) img;
		final BufferedImage t = o.raw;
		o.raw = this.raw;
		this.raw = t;
	}
	
	@Override
	public final int getWidth() {
		return raw.getWidth();
	}
	
	@Override
	public final int getHeight() {
		return raw.getHeight();
	}
	
	@Override
	public final int getRGB(final int x, final int y) {
		return raw.getRGB(x, y);
	}
	
	@Override
	public final void setRGB(final int x, final int y, final int rgb) {
		raw.setRGB(x, y, rgb);
	}
	
	@Override
	public final Img getSubimage(final int x, final int y, final int w, final int h) {
		return new AwtImg(raw.getSubimage(x, y, w, h));
	}
	
	@Override
	public final void save(final String location) throws Exception {
		ImageIO.write(raw, "png", new File(location));
	}
	
	@Override
	public final void close() {
		if (raw == null) {
			return;
		}
		raw.flush();
		raw = null;
	}
	
	@Override
	public final boolean isClosed() {
		return raw == null;
	}
}
